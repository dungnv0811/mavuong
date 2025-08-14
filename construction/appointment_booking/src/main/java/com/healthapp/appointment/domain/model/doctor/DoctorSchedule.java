package com.healthapp.appointment.domain.model.doctor;

import jakarta.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "doctor_schedules")
public class DoctorSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "appointment_duration")
    private Integer appointmentDuration = 30;

    @Column(name = "is_day_off")
    private boolean dayOff;

    @Column(name = "holiday_date")
    private LocalDate holidayDate;

    protected DoctorSchedule() {}

    public DoctorSchedule(Long doctorId, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        this.doctorId = doctorId;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Long getDoctorId() { return doctorId; }
    public DayOfWeek getDayOfWeek() { return dayOfWeek; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    public boolean isDayOff() { return dayOff; }
    public LocalDate getHolidayDate() { return holidayDate; }
    public Integer getAppointmentDuration() { return appointmentDuration; }

    public void setDayOff(boolean dayOff) { this.dayOff = dayOff; }
    public void setHolidayDate(LocalDate holidayDate) { this.holidayDate = holidayDate; }
    public void setAppointmentDuration(Integer appointmentDuration) { this.appointmentDuration = appointmentDuration; }
}


