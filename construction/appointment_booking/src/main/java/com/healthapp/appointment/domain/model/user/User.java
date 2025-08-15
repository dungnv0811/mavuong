package com.healthapp.appointment.domain.model.user;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String uuid;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    @Column(name = "first_name", nullable = false)
    private String firstName;
    
    @Column(name = "last_name", nullable = false)
    private String lastName;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    private String phone;
    
    @Column(name = "date_of_birth")
    private LocalDate dob;
    
    private String address;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false)
    private UserRole userRole;
    
    @Column(nullable = false)
    private String password;
    
    protected User() {}
    
    public User(String uuid, String username, String firstName, String lastName, 
               String email, String phone, LocalDate dob, String address, 
               UserRole userRole, String password) {
        this.uuid = uuid;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.dob = dob;
        this.address = address;
        this.userRole = userRole;
        this.password = password;
    }
    
    // Getters
    public Long getId() { return id; }
    public String getUuid() { return uuid; }
    public String getUsername() { return username; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public LocalDate getDob() { return dob; }
    public String getAddress() { return address; }
    public UserRole getUserRole() { return userRole; }
    public String getPassword() { return password; }
    
    // Setters for updates
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setDob(LocalDate dob) { this.dob = dob; }
    public void setAddress(String address) { this.address = address; }
    public void setPassword(String password) { this.password = password; }
}
