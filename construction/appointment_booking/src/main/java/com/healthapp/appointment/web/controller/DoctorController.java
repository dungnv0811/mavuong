package com.healthapp.appointment.web.controller;

import com.healthapp.appointment.application.dto.*;
import com.healthapp.appointment.application.service.DoctorApplicationService;
import com.healthapp.appointment.domain.model.doctor.DoctorSchedule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/doctors")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Doctors", description = "Doctor information and availability operations")
public class DoctorController {
    private final DoctorApplicationService doctorService;
    
    public DoctorController(DoctorApplicationService doctorService) {
        this.doctorService = doctorService;
    }
    
    @GetMapping
    @Operation(summary = "Get all doctors", description = "Get all doctors for listing page with pagination")
    public ResponseEntity<PaginatedDoctorsResponse> getAllDoctors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "") String specialty,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            System.out.println("🏥 DoctorController: Getting doctors - page: " + page + ", size: " + size + ", search: " + search);
            
            List<DoctorListingDto> allDoctors = doctorService.getAllDoctorsForListing();
            
            // Apply search filter
            List<DoctorListingDto> filteredDoctors = allDoctors.stream()
                .filter(doctor -> {
                    if (search.isEmpty()) return true;
                    String searchLower = search.toLowerCase();
                    return doctor.name().toLowerCase().contains(searchLower) ||
                           doctor.specialty().toLowerCase().contains(searchLower);
                })
                .filter(doctor -> {
                    if (specialty.isEmpty()) return true;
                    return doctor.specialty().toLowerCase().contains(specialty.toLowerCase());
                })
                .toList();
            
            // Apply sorting
            List<DoctorListingDto> sortedDoctors = filteredDoctors.stream()
                .sorted((a, b) -> {
                    int comparison = switch (sortBy) {
                        case "rating" -> Double.compare(
                            sortDir.equals("desc") ? b.rating() : a.rating(),
                            sortDir.equals("desc") ? a.rating() : b.rating()
                        );
                        case "reviews" -> Integer.compare(
                            sortDir.equals("desc") ? b.reviews() : a.reviews(),
                            sortDir.equals("desc") ? a.reviews() : b.reviews()
                        );
                        case "specialty" -> sortDir.equals("desc") ? 
                            b.specialty().compareTo(a.specialty()) : 
                            a.specialty().compareTo(b.specialty());
                        default -> sortDir.equals("desc") ? 
                            b.name().compareTo(a.name()) : 
                            a.name().compareTo(b.name());
                    };
                    return comparison;
                })
                .toList();
            
            // Apply pagination
            int totalElements = sortedDoctors.size();
            int totalPages = (int) Math.ceil((double) totalElements / size);
            int startIndex = page * size;
            int endIndex = Math.min(startIndex + size, totalElements);
            
            List<DoctorListingDto> paginatedDoctors = startIndex < totalElements ? 
                sortedDoctors.subList(startIndex, endIndex) : List.of();
            
            PaginatedDoctorsResponse response = new PaginatedDoctorsResponse(
                paginatedDoctors,
                page,
                size,
                totalElements,
                totalPages,
                page < totalPages - 1,
                page > 0
            );
            
            System.out.println("🏥 DoctorController: Returning " + paginatedDoctors.size() + " doctors (page " + page + " of " + totalPages + ")");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("🏥 DoctorController: Error getting doctors: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/{doctorID}/booking-info")
    @Operation(summary = "Get doctor booking info", description = "Get doctor basic info for booking")
    public ResponseEntity<DoctorListingDto> getDoctorBookingInfo(@PathVariable String doctorID) {
        try {
            DoctorListingDto doctor = doctorService.getDoctorBookingInfo(doctorID);
            return ResponseEntity.ok(doctor);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/{doctorID}/profile")
    @Operation(summary = "Get doctor profile", description = "Get detailed doctor profile")
    public ResponseEntity<DoctorProfileDto> getDoctorProfile(@PathVariable String doctorID) {
        try {
            DoctorProfileDto profile = doctorService.getDoctorProfile(doctorID);
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{doctorID}/profile")
    @Operation(summary = "Update doctor profile", description = "Update doctor profile")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<UpdateProfileResponse> updateDoctorProfile(
            @PathVariable String doctorID,
            @RequestBody DoctorProfileDto profile) {
        try {
            DoctorProfileDto updatedProfile = doctorService.updateDoctorProfile(doctorID, profile);
            return ResponseEntity.ok(new UpdateProfileResponse(updatedProfile));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    public record DoctorsResponse(List<DoctorListingDto> doctors) {}
    public record PaginatedDoctorsResponse(
        List<DoctorListingDto> doctors,
        int page,
        int size,
        int totalElements,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious
    ) {}
    public record UpdateProfileResponse(DoctorProfileDto profile) {}
}