package com.healthapp.appointment.application.service;

import com.healthapp.appointment.application.dto.DoctorScheduleDto;
import com.healthapp.appointment.application.dto.TimeSlotDto;
import com.healthapp.appointment.domain.model.doctor.DoctorSchedule;
import com.healthapp.appointment.domain.model.doctor.DoctorId;
import com.healthapp.appointment.infrastructure.repository.JpaDoctorScheduleRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScheduleApplicationService {
    
    private final JpaDoctorScheduleRepository scheduleRepository;
    // In-memory schedule store for demo purposes
    private final Map<String, Map<String, List<TimeSlotDto>>> schedules = new HashMap<>();
    
    public ScheduleApplicationService(JpaDoctorScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
        initializeDemoSchedules();
    }
    
    private void initializeDemoSchedules() {
        // Initialize doctor-1 schedule for today and tomorrow
        Map<String, List<TimeSlotDto>> doctor1Schedule = new HashMap<>();
        
        List<TimeSlotDto> todaySlots = generateTimeSlots();
        List<TimeSlotDto> tomorrowSlots = generateTimeSlots();
        
        doctor1Schedule.put("2024-01-15", todaySlots);
        doctor1Schedule.put("2024-01-16", tomorrowSlots);
        
        schedules.put("doctor-uuid-456", doctor1Schedule);
    }
    
    private List<TimeSlotDto> generateTimeSlots() {
        List<TimeSlotDto> slots = new ArrayList<>();
        
        // Generate slots from 9 AM to 5 PM
        for (int hour = 9; hour < 17; hour++) {
            for (int minute = 0; minute < 60; minute += 30) {
                LocalTime time = LocalTime.of(hour, minute);
                String timeStr = time.format(DateTimeFormatter.ofPattern("h:mm a"));
                boolean isBooked = Math.random() < 0.3; // 30% chance of being booked
                slots.add(new TimeSlotDto(timeStr, isBooked, false));
            }
        }
        
        return slots;
    }
    
    public DoctorScheduleDto getDoctorSchedule(String doctorID, String date) {
        try {
            // Try to get schedule from database first
            Long doctorIdLong = Long.parseLong(doctorID);
            LocalDate scheduleDate = LocalDate.parse(date);
            DayOfWeek dayOfWeek = scheduleDate.getDayOfWeek();
            
            List<DoctorSchedule> dbSchedules = scheduleRepository.findByDoctorIdAndDate(
                doctorIdLong, dayOfWeek);
            
            if (!dbSchedules.isEmpty()) {
                List<TimeSlotDto> slots = new ArrayList<>();
                for (DoctorSchedule schedule : dbSchedules) {
                    String timeStr = schedule.getStartTime().format(DateTimeFormatter.ofPattern("h:mm a"));
                    // Generate time slots based on the schedule
                    LocalTime currentTime = schedule.getStartTime();
                    LocalTime endTime = schedule.getEndTime();
                    int duration = schedule.getAppointmentDuration();
                    
                    while (currentTime.isBefore(endTime)) {
                        String slotTime = currentTime.format(DateTimeFormatter.ofPattern("h:mm a"));
                        boolean isBooked = Math.random() < 0.3; // Random booking status for demo
                        slots.add(new TimeSlotDto(slotTime, isBooked, false));
                        currentTime = currentTime.plusMinutes(duration);
                    }
                }
                return new DoctorScheduleDto(doctorID, date, slots);
            }
        } catch (Exception e) {
            System.out.println("🕐 ScheduleApplicationService: Error getting schedule from DB, falling back to mock: " + e.getMessage());
        }
        
        // Fallback to mock data
        Map<String, List<TimeSlotDto>> doctorSchedule = schedules.get(doctorID);
        if (doctorSchedule == null) {
            // Generate default schedule if not found
            schedules.put(doctorID, new HashMap<>());
            doctorSchedule = schedules.get(doctorID);
        }
        
        List<TimeSlotDto> slots = doctorSchedule.get(date);
        if (slots == null) {
            // Generate default schedule if not found
            slots = generateTimeSlots();
            doctorSchedule.put(date, slots);
        }
        
        return new DoctorScheduleDto(doctorID, date, slots);
    }
    
    public boolean bookTimeSlot(String doctorID, String date, String time) {
        try {
            Map<String, List<TimeSlotDto>> doctorSchedule = schedules.get(doctorID);
            if (doctorSchedule == null) {
                return false;
            }
            
            List<TimeSlotDto> slots = doctorSchedule.get(date);
            if (slots == null) {
                return false;
            }
            
            // Find and book the time slot
            for (int i = 0; i < slots.size(); i++) {
                TimeSlotDto slot = slots.get(i);
                if (slot.time().equals(time) && !slot.isBooked()) {
                    slots.set(i, new TimeSlotDto(time, true, false));
                    return true;
                }
            }
            
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean releaseTimeSlot(String doctorID, String date, String time) {
        try {
            Map<String, List<TimeSlotDto>> doctorSchedule = schedules.get(doctorID);
            if (doctorSchedule == null) {
                return false;
            }
            
            List<TimeSlotDto> slots = doctorSchedule.get(date);
            if (slots == null) {
                return false;
            }
            
            // Find and release the time slot
            for (int i = 0; i < slots.size(); i++) {
                TimeSlotDto slot = slots.get(i);
                if (slot.time().equals(time) && slot.isBooked()) {
                    slots.set(i, new TimeSlotDto(time, false, false));
                    return true;
                }
            }
            
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
