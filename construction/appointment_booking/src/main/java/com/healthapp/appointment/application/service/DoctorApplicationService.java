package com.healthapp.appointment.application.service;

import com.healthapp.appointment.application.dto.*;
import com.healthapp.appointment.domain.model.doctor.DoctorSchedule;
import com.healthapp.appointment.domain.model.doctor.Doctor;
import com.healthapp.appointment.domain.model.doctor.DoctorId;
import com.healthapp.appointment.domain.repository.DoctorRepository;
import com.healthapp.appointment.infrastructure.repository.JpaDoctorScheduleRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DoctorApplicationService {
    private final DoctorRepository doctorRepository;
    private final JpaDoctorScheduleRepository scheduleRepository;
    
    // In-memory stores for demo data to match frontend expectations
    private final Map<String, DoctorListingDto> doctorListings = new HashMap<>();
    private final Map<String, DoctorProfileDto> doctorProfiles = new HashMap<>();
    
    public DoctorApplicationService(DoctorRepository doctorRepository,
                                   JpaDoctorScheduleRepository scheduleRepository) {
        this.doctorRepository = doctorRepository;
        this.scheduleRepository = scheduleRepository;
        initializeDemoData();
    }
    
    private void initializeDemoData() {
        // Doctor 1
        DoctorListingDto doctor1 = new DoctorListingDto(
            "doctor-uuid-456",
            "Dr. Jane Smith",
            "Cardiology",
            4.8,
            125,
            "Today at 2:00 PM",
            "/assets/images/doctor1.png"
        );
        doctorListings.put("doctor-uuid-456", doctor1);
        
        DoctorProfileDto profile1 = new DoctorProfileDto(
            "doctor-uuid-456",
            "Jane",
            "Smith",
            "MD, FACC",
            "Cardiology",
            15,
            "Dr. Smith is a board-certified cardiologist with over 15 years of experience...",
            "doctor1@test.com",
            "+1234567891",
            new DoctorProfileDto.AddressDto("456 Medical Ave", "New York", "NY", "10001", "USA"),
            "MD123456",
            "Board Certified in Cardiology",
            "American College of Cardiology",
            "/assets/images/doctor1.png"
        );
        doctorProfiles.put("doctor-uuid-456", profile1);
        
        // Doctor 2
        DoctorListingDto doctor2 = new DoctorListingDto(
            "doctor-uuid-789",
            "Dr. Mike Johnson",
            "Dermatology",
            4.6,
            89,
            "Tomorrow at 9:00 AM",
            "/assets/images/doctor2.png"
        );
        doctorListings.put("doctor-uuid-789", doctor2);
        
        DoctorProfileDto profile2 = new DoctorProfileDto(
            "doctor-uuid-789",
            "Mike",
            "Johnson",
            "MD, FAAD",
            "Dermatology",
            12,
            "Dr. Johnson specializes in dermatology and cosmetic procedures...",
            "doctor2@test.com",
            "+1234567892",
            new DoctorProfileDto.AddressDto("789 Skin Clinic", "Los Angeles", "CA", "90001", "USA"),
            "MD789123",
            "Board Certified in Dermatology",
            "American Academy of Dermatology",
            "/assets/images/doctor2.png"
        );
        doctorProfiles.put("doctor-uuid-789", profile2);
    }
    
    public List<DoctorDto> getAllDoctors() {
        return doctorRepository.findAll().stream()
            .map(this::toDto)
            .toList();
    }
    
    public List<DoctorDto> getDoctorsBySpecialty(String specialty) {
        return doctorRepository.findBySpecialty(specialty).stream()
            .map(this::toDto)
            .toList();
    }

    public DoctorDto getDoctorById(Long id) {
        return doctorRepository.findById(new DoctorId(id))
            .map(this::toDto)
            .orElseThrow(() -> new RuntimeException("Doctor not found"));
    }
    
    public List<String> getDoctorAvailability(Long doctorId) {
        return scheduleRepository.findByDoctorId(doctorId).stream()
            .map(this::formatSchedule)
            .collect(Collectors.toList());
    }
    
    private String formatSchedule(DoctorSchedule schedule) {
        return schedule.getDayOfWeek() + " " + schedule.getStartTime() + "-" + schedule.getEndTime() + " (" + schedule.getAppointmentDuration() + "m)";
    }

    public DoctorSchedule saveSchedule(DoctorSchedule schedule) {
        return scheduleRepository.save(schedule);
    }
    
    // New methods for frontend compatibility
    public List<DoctorListingDto> getAllDoctorsForListing() {
        // Get doctors from database and convert to listing format
        List<Doctor> doctors = doctorRepository.findAll();
        List<DoctorListingDto> doctorListingDtos = doctors.stream()
            .map(doctor -> new DoctorListingDto(
                String.valueOf(doctor.getDoctorId().value()),
                doctor.getName(),
                doctor.getSpecialties().isEmpty() ? "General Practice" : doctor.getSpecialties().get(0),
                doctor.getAverageRating(),
                doctor.getTotalReviews(),
                "Available Today", // Default availability
                determineAvatar(doctor.getName())
            ))
            .toList();
            
        // If no doctors from database, fall back to mock data
        if (doctorListingDtos.isEmpty()) {
            return new ArrayList<>(doctorListings.values());
        }
        
        return doctorListingDtos;
    }
    
    private String determineAvatar(String doctorName) {
        // Simple logic to assign avatars based on name
        if (doctorName.contains("Elena") || doctorName.contains("Sarah") || 
            doctorName.contains("Emily") || doctorName.contains("Lisa") || 
            doctorName.contains("Olivia") || doctorName.contains("Sofia")) {
            return "/assets/images/female_doctor_avatar.png";
        }
        return "/assets/images/male_doctor_avatar.png";
    }
    
    public DoctorListingDto getDoctorBookingInfo(String doctorID) {
        try {
            // Try to find doctor in database first
            Long id = Long.parseLong(doctorID);
            Doctor doctor = doctorRepository.findById(new DoctorId(id))
                .orElse(null);
                
            if (doctor != null) {
                return new DoctorListingDto(
                    String.valueOf(doctor.getDoctorId().value()),
                    doctor.getName(),
                    doctor.getSpecialties().isEmpty() ? "General Practice" : doctor.getSpecialties().get(0),
                    doctor.getAverageRating(),
                    doctor.getTotalReviews(),
                    "Available Today",
                    determineAvatar(doctor.getName())
                );
            }
        } catch (NumberFormatException e) {
            // doctorID is not a number, try mock data
        }
        
        // Fallback to mock data
        DoctorListingDto mockDoctor = doctorListings.get(doctorID);
        if (mockDoctor == null) {
            throw new RuntimeException("Doctor not found");
        }
        return mockDoctor;
    }
    
    public DoctorProfileDto getDoctorProfile(String doctorID) {
        DoctorProfileDto profile = doctorProfiles.get(doctorID);
        if (profile == null) {
            throw new RuntimeException("Doctor profile not found");
        }
        return profile;
    }
    
    public DoctorProfileDto updateDoctorProfile(String doctorID, DoctorProfileDto profileUpdate) {
        DoctorProfileDto existingProfile = doctorProfiles.get(doctorID);
        if (existingProfile == null) {
            throw new RuntimeException("Doctor profile not found");
        }
        
        // Update profile with new values
        DoctorProfileDto updatedProfile = new DoctorProfileDto(
            doctorID,
            profileUpdate.firstName() != null ? profileUpdate.firstName() : existingProfile.firstName(),
            profileUpdate.lastName() != null ? profileUpdate.lastName() : existingProfile.lastName(),
            profileUpdate.professionalTitle() != null ? profileUpdate.professionalTitle() : existingProfile.professionalTitle(),
            profileUpdate.primarySpecialty() != null ? profileUpdate.primarySpecialty() : existingProfile.primarySpecialty(),
            profileUpdate.yearsExperience() != null ? profileUpdate.yearsExperience() : existingProfile.yearsExperience(),
            profileUpdate.biography() != null ? profileUpdate.biography() : existingProfile.biography(),
            profileUpdate.email() != null ? profileUpdate.email() : existingProfile.email(),
            profileUpdate.phone() != null ? profileUpdate.phone() : existingProfile.phone(),
            profileUpdate.address() != null ? profileUpdate.address() : existingProfile.address(),
            profileUpdate.medicalLicenseNumber() != null ? profileUpdate.medicalLicenseNumber() : existingProfile.medicalLicenseNumber(),
            profileUpdate.certifications() != null ? profileUpdate.certifications() : existingProfile.certifications(),
            profileUpdate.memberships() != null ? profileUpdate.memberships() : existingProfile.memberships(),
            profileUpdate.avatar() != null ? profileUpdate.avatar() : existingProfile.avatar()
        );
        
        doctorProfiles.put(doctorID, updatedProfile);
        return updatedProfile;
    }
    
    private DoctorDto toDto(Doctor doctor) {
        return new DoctorDto(
            doctor.getDoctorId().value(),
            doctor.getName(),
            doctor.getEmail(),
            doctor.getPhone(),
            doctor.getQualifications(),
            doctor.getSpecialties(),
            doctor.getAverageRating(),
            doctor.getTotalReviews()
        );
    }
}