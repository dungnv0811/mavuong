package com.healthapp.appointment;

import com.healthapp.appointment.domain.model.doctor.Doctor;
import com.healthapp.appointment.infrastructure.repository.JpaDoctorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.util.Arrays;

@SpringBootApplication
public class AppointmentBookingApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(AppointmentBookingApplication.class, args);
    }
    
    @Bean
    CommandLineRunner initData(JpaDoctorRepository doctorRepository) {
        return args -> {
            // Seed doctors
            doctorRepository.save(new Doctor("Dr. John Smith", "john.smith@hospital.com", 
                "555-0101", "MD Cardiology", Arrays.asList("cardiology")));
            doctorRepository.save(new Doctor("Dr. Sarah Johnson", "sarah.johnson@hospital.com", 
                "555-0102", "MD Dermatology", Arrays.asList("dermatology")));
            doctorRepository.save(new Doctor("Dr. Mike Wilson", "mike.wilson@hospital.com", 
                "555-0103", "MD Orthopedics", Arrays.asList("orthopedics")));
            doctorRepository.save(new Doctor("Dr. Lisa Brown", "lisa.brown@hospital.com", 
                "555-0104", "MD Neurology", Arrays.asList("neurology")));
            doctorRepository.save(new Doctor("Dr. David Lee", "david.lee@hospital.com", 
                "555-0105", "MD General Practice", Arrays.asList("general")));
        };
    }
}