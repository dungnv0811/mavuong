package com.healthapp.appointment.infrastructure.repository;

import com.healthapp.appointment.domain.model.doctor.DoctorSchedule;
import com.healthapp.appointment.domain.model.doctor.DoctorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface JpaDoctorScheduleRepository extends JpaRepository<DoctorSchedule, Long> {
    List<DoctorSchedule> findByDoctorId(Long doctorId);
    List<DoctorSchedule> findByDoctorIdAndDayOfWeek(Long doctorId, DayOfWeek dayOfWeek);
    
    // Add the missing method for finding schedules by doctor ID and day of week
    @Query("SELECT ds FROM DoctorSchedule ds WHERE ds.doctorId = :doctorId AND ds.dayOfWeek = :dayOfWeek")
    List<DoctorSchedule> findByDoctorIdAndDate(@Param("doctorId") Long doctorId, @Param("dayOfWeek") DayOfWeek dayOfWeek);
}


