package com.cs203.core.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

import org.hibernate.annotations.CreationTimestamp;

import com.cs203.core.enums.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
    private String username;

    @Column(nullable = false, unique = true, length = 254)
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    @NotBlank(message = "Password is required")
    private String passwordHash;

    @Column(name = "is_admin", nullable = false)
    private boolean isAdmin = false;

    @Column(name = "first_name", length = 100)
    @Size(max = 100, message = "First name cannot exceed 100 characters")
    private String firstName;

    @Column(name = "last_name", length = 100)
    @Size(max = 100, message = "Last name cannot exceed 100 characters")
    private String lastName;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean enabled = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Relationship mappings for cascade operations
    @OneToMany(mappedBy = "user", cascade = { CascadeType.MERGE, CascadeType.REFRESH })
    private List<SavedCalculationsEntity> savedCalculations = new ArrayList<>();

    @OneToMany(mappedBy = "createdBy", cascade = { CascadeType.MERGE, CascadeType.REFRESH })
    private List<NationalTariffLinesEntity> createdTariffLines = new ArrayList<>();

    @OneToMany(mappedBy = "updatedBy", cascade = { CascadeType.MERGE, CascadeType.REFRESH })
    private List<NationalTariffLinesEntity> updatedTariffLines = new ArrayList<>();

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<SavedCalculationsEntity> getSavedCalculations() {
        return savedCalculations;
    }

    public void setSavedCalculations(List<SavedCalculationsEntity> savedCalculations) {
        this.savedCalculations = savedCalculations;
    }

    public List<NationalTariffLinesEntity> getCreatedTariffLines() {
        return createdTariffLines;
    }

    public void setCreatedTariffLines(List<NationalTariffLinesEntity> createdTariffLines) {
        this.createdTariffLines = createdTariffLines;
    }

    public List<NationalTariffLinesEntity> getUpdatedTariffLines() {
        return updatedTariffLines;
    }

    public void setUpdatedTariffLines(List<NationalTariffLinesEntity> updatedTariffLines) {
        this.updatedTariffLines = updatedTariffLines;
    }

    @Transient
    public UserRole getUserRole() {
        return getIsAdmin() ? UserRole.ADMIN : UserRole.NOT_ADMIN;
    }

    @PrePersist
    void setUp() {
        this.enabled = true;
    }
}
