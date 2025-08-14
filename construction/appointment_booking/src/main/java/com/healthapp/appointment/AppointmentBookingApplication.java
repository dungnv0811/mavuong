package com.healthapp.appointment;

import com.healthapp.appointment.domain.model.doctor.Doctor;
import com.healthapp.appointment.domain.model.doctor.DoctorSchedule;
import com.healthapp.appointment.infrastructure.repository.JpaDoctorRepository;
import com.healthapp.appointment.infrastructure.repository.JpaDoctorScheduleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.util.Arrays;
import java.util.List;
import java.time.DayOfWeek;
import java.time.LocalTime;

@SpringBootApplication
public class AppointmentBookingApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(AppointmentBookingApplication.class, args);
    }
    
    @Bean
    CommandLineRunner initData(JpaDoctorRepository doctorRepository, JpaDoctorScheduleRepository scheduleRepository) {
        // Data is seeded via schema.sql and data.sql now
        return args -> {};
    }
}