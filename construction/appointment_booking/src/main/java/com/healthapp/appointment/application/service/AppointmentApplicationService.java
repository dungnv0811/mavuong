package com.healthapp.appointment.application.service;

import com.healthapp.appointment.application.dto.AppointmentDto;
import com.healthapp.appointment.domain.event.AppointmentBooked;
import com.healthapp.appointment.domain.event.AppointmentConfirmed;
import com.healthapp.appointment.domain.model.appointment.Appointment;
import com.healthapp.appointment.domain.model.appointment.AppointmentId;
import com.healthapp.appointment.domain.model.appointment.AppointmentStatus;
import com.healthapp.appointment.domain.repository.AppointmentRepository;
import com.healthapp.appointment.domain.service.AppointmentBookingService;
import com.healthapp.appointment.infrastructure.event.EventPublisher;
import com.healthapp.appointment.web.controller.AppointmentController;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AppointmentApplicationService {
    private final AppointmentRepository appointmentRepository;
    private final AppointmentBookingService bookingService;
    private final EventPublisher eventPublisher;
    
    // In-memory store for demo appointments to match frontend expectations
    private final Map<String, AppointmentDto> appointmentsByUuid = new HashMap<>();
    private final Map<String, List<AppointmentDto>> appointmentsByUser = new HashMap<>();
    
    public AppointmentApplicationService(AppointmentRepository appointmentRepository, 
                                       AppointmentBookingService bookingService,
                                       EventPublisher eventPublisher) {
        this.appointmentRepository = appointmentRepository;
        this.bookingService = bookingService;
        this.eventPublisher = eventPublisher;
        initializeDemoAppointments();
    }
    
    private void initializeDemoAppointments() {
        // Demo upcoming appointments
        AppointmentDto upcoming1 = new AppointmentDto(
            1L,
            1L,
            1L,
            LocalDateTime.now().plusDays(1),
            30,
            "Routine checkup",
            AppointmentStatus.CONFIRMED
        );
        
        AppointmentDto upcoming2 = new AppointmentDto(
            2L,
            1L,
            2L,
            LocalDateTime.now().plusDays(3),
            45,
            "Follow-up consultation",
            AppointmentStatus.PENDING
        );
        
        // Demo past appointments - Add more historical appointments
        AppointmentDto past1 = new AppointmentDto(
            3L,
            1L,
            1L,
            LocalDateTime.now().minusDays(7),
            30,
            "Annual physical",
            AppointmentStatus.COMPLETED
        );
        
        AppointmentDto past2 = new AppointmentDto(
            4L,
            1L,
            2L,
            LocalDateTime.now().minusDays(30),
            45,
            "Cardiology consultation",
            AppointmentStatus.COMPLETED
        );
        
        AppointmentDto past3 = new AppointmentDto(
            5L,
            1L,
            1L,
            LocalDateTime.now().minusDays(90),
            30,
            "General checkup",
            AppointmentStatus.COMPLETED
        );
        
        AppointmentDto cancelled1 = new AppointmentDto(
            6L,
            1L,
            2L,
            LocalDateTime.now().minusDays(14),
            30,
            "Dental checkup",
            AppointmentStatus.CANCELLED
        );
        
        String userUuid = "patient-uuid-123";
        String actualUserUuid = "3520b368-1b1b-42ab-ab03-d44b6df78a7d"; // The actual logged-in user
        
        appointmentsByUuid.put("app-uuid-1", upcoming1);
        appointmentsByUuid.put("app-uuid-2", upcoming2);
        appointmentsByUuid.put("app-uuid-3", past1);
        appointmentsByUuid.put("app-uuid-4", past2);
        appointmentsByUuid.put("app-uuid-5", past3);
        appointmentsByUuid.put("app-uuid-6", cancelled1);
        
        // Add appointments for both user IDs to ensure compatibility
        List<AppointmentDto> allAppointments = Arrays.asList(upcoming1, upcoming2, past1, past2, past3, cancelled1);
        appointmentsByUser.put(userUuid, allAppointments);
        appointmentsByUser.put(actualUserUuid, allAppointments);
        
        System.out.println("📅 Initialized appointments for users: " + userUuid + " and " + actualUserUuid);
    }
    
    public AppointmentDto bookAppointment(Long patientId, Long doctorId, LocalDateTime dateTime, String notes) {
        Appointment appointment = bookingService.bookAppointment(patientId, doctorId, dateTime, notes);
        eventPublisher.publish(new AppointmentBooked(
            appointment.getAppointmentId().value(),
            appointment.getPatientId(),
            appointment.getDoctorId(),
            appointment.getAppointmentDateTime()
        ));
        return toDto(appointment);
    }
    
    public List<AppointmentDto> getPatientAppointments(Long patientId) {
        return appointmentRepository.findByPatientId(patientId).stream()
            .map(this::toDto)
            .toList();
    }
    
    public List<AppointmentDto> getDoctorAppointments(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId).stream()
            .map(this::toDto)
            .toList();
    }
    
    public AppointmentDto getAppointmentById(Long id) {
        return appointmentRepository.findById(new AppointmentId(id))
            .map(this::toDto)
            .orElseThrow(() -> new RuntimeException("Appointment not found"));
    }
    
    public AppointmentDto confirmAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(new AppointmentId(appointmentId))
            .orElseThrow(() -> new RuntimeException("Appointment not found"));
        appointment.confirm();
        Appointment saved = appointmentRepository.save(appointment);
        eventPublisher.publish(new AppointmentConfirmed(saved.getAppointmentId().value(), saved.getDoctorId()));
        return toDto(saved);
    }
 
    public AppointmentDto cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(new AppointmentId(appointmentId))
            .orElseThrow(() -> new RuntimeException("Appointment not found"));
        appointment.cancel();
        return toDto(appointmentRepository.save(appointment));
    }
 
    public AppointmentDto rescheduleAppointment(Long appointmentId, LocalDateTime newDateTime) {
        Appointment appointment = appointmentRepository.findById(new AppointmentId(appointmentId))
            .orElseThrow(() -> new RuntimeException("Appointment not found"));
        appointment.reschedule(newDateTime);
        return toDto(appointmentRepository.save(appointment));
    }
    
    // New methods for frontend compatibility - expects either UUID, username, or numeric ID
    public List<AppointmentDto> getUpcomingAppointments(String userID) {
        System.out.println("📅 Getting upcoming appointments for userID: " + userID);
        
        // Create mappings for both doctors and patients
        Map<String, Long> doctorMapping = createDoctorUserMapping();
        Map<String, Long> patientMapping = createPatientUserMapping();
        
        // First check if this is a doctor by checking our static mapping
        Long doctorId = doctorMapping.get(userID);
        if (doctorId != null) {
            System.out.println("📅 Found direct doctor mapping: " + userID + " -> doctorId: " + doctorId);
            List<Appointment> doctorAppointments = appointmentRepository.findByDoctorId(doctorId);
            if (!doctorAppointments.isEmpty()) {
                List<AppointmentDto> result = doctorAppointments.stream()
                    .filter(appointment -> appointment.getAppointmentDateTime().isAfter(LocalDateTime.now()))
                    .filter(appointment -> appointment.getStatus() == AppointmentStatus.CONFIRMED || 
                                          appointment.getStatus() == AppointmentStatus.PENDING)
                    .map(this::toDto)
                    .collect(Collectors.toList());
                System.out.println("📅 Returning " + result.size() + " upcoming appointments for doctor: " + userID);
                return result;
            }
        }
        
        // Check if this is a patient by checking our static mapping
        Long patientId = patientMapping.get(userID);
        if (patientId != null) {
            System.out.println("📅 Found direct patient mapping: " + userID + " -> patientId: " + patientId);
            List<Appointment> patientAppointments = appointmentRepository.findByPatientId(patientId);
            if (!patientAppointments.isEmpty()) {
                List<AppointmentDto> result = patientAppointments.stream()
                    .filter(appointment -> appointment.getAppointmentDateTime().isAfter(LocalDateTime.now()))
                    .filter(appointment -> appointment.getStatus() == AppointmentStatus.CONFIRMED || 
                                          appointment.getStatus() == AppointmentStatus.PENDING)
                    .map(this::toDto)
                    .collect(Collectors.toList());
                System.out.println("📅 Returning " + result.size() + " upcoming appointments for patient: " + userID);
                return result;
            }
        }
        
        try {
            // Try to get appointments from database first
            Long userId = Long.parseLong(userID);
            
            List<Appointment> dbAppointments = new ArrayList<>();
            
            // First try as patient
            List<Appointment> patientAppointments = appointmentRepository.findByPatientId(userId);
            dbAppointments.addAll(patientAppointments);
            
            // Also try as doctor (doctor IDs are typically 1-10 based on our data)
            if (userId >= 1 && userId <= 20) {
                List<Appointment> doctorAppointments = appointmentRepository.findByDoctorId(userId);
                dbAppointments.addAll(doctorAppointments);
                System.out.println("📅 Found " + doctorAppointments.size() + " appointments for doctor ID: " + userId);
            }
            
            if (!dbAppointments.isEmpty()) {
                List<AppointmentDto> result = dbAppointments.stream()
                    .filter(appointment -> appointment.getAppointmentDateTime().isAfter(LocalDateTime.now()))
                    .filter(appointment -> appointment.getStatus() == AppointmentStatus.CONFIRMED || 
                                          appointment.getStatus() == AppointmentStatus.PENDING)
                    .map(this::toDto)
                    .collect(Collectors.toList());
                System.out.println("📅 Returning " + result.size() + " upcoming appointments from database");
                return result;
            }
        } catch (NumberFormatException e) {
            System.out.println("📅 userID is not a number: " + userID);
        }
        
        // Fallback to mock data
        List<AppointmentDto> userAppointments = appointmentsByUser.get(userID);
        if (userAppointments == null) {
            System.out.println("📅 No appointments found for userID: " + userID);
            return new ArrayList<>();
        }
        
        List<AppointmentDto> result = userAppointments.stream()
            .filter(appointment -> appointment.appointmentDateTime().isAfter(LocalDateTime.now()))
            .filter(appointment -> appointment.status() == AppointmentStatus.CONFIRMED || 
                                  appointment.status() == AppointmentStatus.PENDING)
            .collect(Collectors.toList());
        System.out.println("📅 Returning " + result.size() + " upcoming appointments from mock data");
        return result;
    }
    
    private Map<String, Long> createDoctorUserMapping() {
        Map<String, Long> mapping = new HashMap<>();
        // Map doctor usernames to their database IDs based on data.sql
        mapping.put("elena.rodriguez", 1L);
        mapping.put("michael.chen", 2L);
        mapping.put("sarah.kim", 3L);
        mapping.put("david.green", 4L);
        mapping.put("emily.white", 5L);
        mapping.put("james.brown", 6L);
        mapping.put("olivia.perez", 7L);
        mapping.put("ben.carter", 8L);
        mapping.put("sofia.garcia", 9L);
        mapping.put("john.smith", 10L);
        mapping.put("sarah.johnson", 11L);
        mapping.put("mike.wilson", 12L);
        mapping.put("lisa.brown", 13L);
        mapping.put("david.lee", 14L);
        
        // Also map email addresses to the same IDs
        mapping.put("elena.rodriguez@hospital.com", 1L);
        mapping.put("michael.chen@hospital.com", 2L);
        mapping.put("sarah.kim@hospital.com", 3L);
        mapping.put("david.green@hospital.com", 4L);
        mapping.put("emily.white@hospital.com", 5L);
        mapping.put("james.brown@hospital.com", 6L);
        mapping.put("olivia.perez@hospital.com", 7L);
        mapping.put("ben.carter@hospital.com", 8L);
        mapping.put("sofia.garcia@hospital.com", 9L);
        mapping.put("john.smith@hospital.com", 10L);
        mapping.put("sarah.johnson@hospital.com", 11L);
        mapping.put("mike.wilson@hospital.com", 12L);
        mapping.put("lisa.brown@hospital.com", 13L);
        mapping.put("david.lee@hospital.com", 14L);
        
        return mapping;
    }
    
    private Map<String, Long> createPatientUserMapping() {
        Map<String, Long> mapping = new HashMap<>();
        // Map patient usernames to their database IDs based on data.sql
        mapping.put("patient1", 1001L);
        mapping.put("patient1@test.com", 1001L);
        
        // Add more patients as needed
        // mapping.put("patient2", 1002L);
        
        return mapping;
    }
    
    public List<AppointmentDto> getAppointmentHistory(String userID) {
        System.out.println("📅 AppointmentApplicationService: Getting history for userID: " + userID);
        System.out.println("📅 Available user keys in appointmentsByUser: " + appointmentsByUser.keySet());
        
        // Create mappings for both doctors and patients
        Map<String, Long> doctorMapping = createDoctorUserMapping();
        Map<String, Long> patientMapping = createPatientUserMapping();
        
        // First check if this is a doctor by checking our static mapping
        Long doctorId = doctorMapping.get(userID);
        if (doctorId != null) {
            System.out.println("📅 Found doctor mapping for history: " + userID + " -> doctorId: " + doctorId);
            List<Appointment> doctorAppointments = appointmentRepository.findByDoctorId(doctorId);
            if (!doctorAppointments.isEmpty()) {
                System.out.println("📅 Found " + doctorAppointments.size() + " appointments for doctor: " + userID);
                return doctorAppointments.stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());
            }
        }
        
        // Check if this is a patient by checking our static mapping
        Long patientId = patientMapping.get(userID);
        if (patientId != null) {
            System.out.println("📅 Found patient mapping for history: " + userID + " -> patientId: " + patientId);
            List<Appointment> patientAppointments = appointmentRepository.findByPatientId(patientId);
            if (!patientAppointments.isEmpty()) {
                System.out.println("📅 Found " + patientAppointments.size() + " appointments for patient: " + userID);
                return patientAppointments.stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());
            }
        }
        
        try {
            // Try to get appointments from database first
            Long userId = Long.parseLong(userID);
            
            List<Appointment> dbAppointments = new ArrayList<>();
            
            // First try as patient
            List<Appointment> patientAppointments = appointmentRepository.findByPatientId(userId);
            dbAppointments.addAll(patientAppointments);
            
            // Also try as doctor
            if (userId >= 1 && userId <= 20) {
                List<Appointment> doctorAppointments = appointmentRepository.findByDoctorId(userId);
                dbAppointments.addAll(doctorAppointments);
            }
            
            if (!dbAppointments.isEmpty()) {
                System.out.println("📅 Found " + dbAppointments.size() + " appointments in database");
                // Return ALL appointments (no filtering by date or status)
                return dbAppointments.stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());
            }
        } catch (NumberFormatException e) {
            System.out.println("📅 userID is not a number: " + userID);
        }
        
        // Fallback to mock data
        List<AppointmentDto> userAppointments = appointmentsByUser.get(userID);
        if (userAppointments == null) {
            System.out.println("📅 No appointments found for userID: " + userID);
            return new ArrayList<>();
        }
        
        System.out.println("📅 Found " + userAppointments.size() + " total appointments for user");
        
        // Debug each appointment
        LocalDateTime now = LocalDateTime.now();
        for (AppointmentDto apt : userAppointments) {
            System.out.println("📅 Appointment: " + apt.appointmentDateTime() + 
                             " | Status: " + apt.status() + 
                             " | Is Past: " + apt.appointmentDateTime().isBefore(now) +
                             " | Is Completed/Cancelled: " + (apt.status() == AppointmentStatus.COMPLETED || apt.status() == AppointmentStatus.CANCELLED));
        }
        
        // Include ALL appointments in history (pending, upcoming, completed, cancelled)
        List<AppointmentDto> historyAppointments = userAppointments.stream()
            .filter(appointment -> {
                boolean isPast = appointment.appointmentDateTime().isBefore(now);
                boolean isCompleted = appointment.status() == AppointmentStatus.COMPLETED;
                boolean isCancelled = appointment.status() == AppointmentStatus.CANCELLED;
                boolean isPending = appointment.status() == AppointmentStatus.PENDING;
                boolean isConfirmed = appointment.status() == AppointmentStatus.CONFIRMED;
                boolean isHistory = true; // Show ALL appointments in history
                
                System.out.println("📅 Filtering appointment " + appointment.id() + ": isPast=" + isPast + 
                                 ", isCompleted=" + isCompleted + ", isCancelled=" + isCancelled + 
                                 ", isPending=" + isPending + ", isConfirmed=" + isConfirmed +
                                 ", isHistory=" + isHistory);
                return isHistory;
            })
            .collect(Collectors.toList());
        
        System.out.println("📅 Returning " + historyAppointments.size() + " history appointments");
        return historyAppointments;
    }
    
    public AppointmentDto createAppointment(AppointmentController.CreateAppointmentRequest request) {
        System.out.println("📅 Creating appointment: " + request);
        
        // Parse the date-time string
        LocalDateTime dateTime;
        try {
            dateTime = LocalDateTime.parse(request.dateTime(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } catch (Exception e) {
            // Try alternative format if ISO doesn't work
            dateTime = LocalDateTime.now().plusDays(1); // Default fallback
        }
        
        // Extract doctor ID from request
        Long doctorId;
        try {
            if (request.doctorID().startsWith("doctor-uuid-")) {
                doctorId = Long.parseLong(request.doctorID().replace("doctor-uuid-", ""));
            } else {
                doctorId = Long.parseLong(request.doctorID());
            }
        } catch (NumberFormatException e) {
            System.err.println("📅 Invalid doctor ID format: " + request.doctorID());
            doctorId = 1L; // Default fallback
        }
        
        // Get patient ID using mapping
        Map<String, Long> patientMapping = createPatientUserMapping();
        Long patientId = patientMapping.get(request.userID());
        if (patientId == null) {
            // Fallback to default patient ID if mapping not found
            patientId = 1001L;
            System.out.println("📅 No patient mapping found for userID: " + request.userID() + ", using default patientId: " + patientId);
        } else {
            System.out.println("📅 Found patient mapping: " + request.userID() + " -> patientId: " + patientId);
        }
        
        System.out.println("📅 Creating appointment - doctorId: " + doctorId + ", patientId: " + patientId + ", dateTime: " + dateTime);
        
        // Create appointment using the domain service to save to database
        try {
            Appointment appointment = bookingService.bookAppointment(patientId, doctorId, dateTime, request.patientInfo().reason());
            
            // Publish event
            eventPublisher.publish(new AppointmentBooked(
                appointment.getAppointmentId().value(),
                appointment.getPatientId(),
                appointment.getDoctorId(),
                appointment.getAppointmentDateTime()
            ));
            
            AppointmentDto result = toDto(appointment);
            System.out.println("📅 Successfully created appointment in database: " + result);
            return result;
            
        } catch (Exception e) {
            System.err.println("📅 Error creating appointment in database: " + e.getMessage());
            e.printStackTrace();
            
            // Fallback to in-memory storage for compatibility
            Long newId = System.currentTimeMillis();
            AppointmentDto newAppointment = new AppointmentDto(
                newId,
                patientId,
                doctorId,
                dateTime,
                30, // Default duration
                request.patientInfo().reason(),
                AppointmentStatus.PENDING
            );
            
            String appointmentUuid = "app-uuid-" + newId;
            appointmentsByUuid.put(appointmentUuid, newAppointment);
            
            // Add to user's appointments
            String userUuid = request.userID();
            appointmentsByUser.computeIfAbsent(userUuid, k -> new ArrayList<>()).add(newAppointment);
            
            System.out.println("📅 Created appointment in memory as fallback: " + newAppointment);
            return newAppointment;
        }
    }
    
    public AppointmentDto cancelAppointment(String appointmentId) {
        AppointmentDto appointment = appointmentsByUuid.get(appointmentId);
        if (appointment == null) {
            throw new RuntimeException("Appointment not found");
        }
        
        // Create cancelled version
        AppointmentDto cancelledAppointment = new AppointmentDto(
            appointment.id(),
            appointment.patientId(),
            appointment.doctorId(),
            appointment.appointmentDateTime(),
            appointment.durationMinutes(),
            appointment.notes(),
            AppointmentStatus.CANCELLED
        );
        
        appointmentsByUuid.put(appointmentId, cancelledAppointment);
        return cancelledAppointment;
    }
    
    private AppointmentDto toDto(Appointment appointment) {
        return new AppointmentDto(
            appointment.getAppointmentId().value(),
            appointment.getPatientId(),
            appointment.getDoctorId(),
            appointment.getAppointmentDateTime(),
            appointment.getDurationMinutes(),
            appointment.getNotes(),
            appointment.getStatus()
        );
    }
}