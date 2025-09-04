package com.cs203.core.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "product_categories")
public class ProductCategoriesEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(name = "hs_code", nullable = false, unique = true, length = 20)
    @NotBlank(message = "HS code is required")
    @Size(min = 2, max = 20, message = "HS code must be between 2 and 6 characters")
    private int categoryCode;

    @Column(name = "category_name", nullable = false, length = 100)
    @NotBlank(message = "Category name is required")
    @Size(max = 100, message = "Category name cannot exceed 100 characters")
    private String categoryName;

    @Column(name = "description", length = 500)
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @Column(name = "tariff_base_rate")
    private Double tariffBaseRate;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    // Constructors
    public ProductCategoriesEntity() {}

    public ProductCategoriesEntity(int categoryCode, String categoryName, String description, Double tariffBaseRate) {
        this.categoryCode = categoryCode;
        this.categoryName = categoryName;
        this.description = description;
        this.tariffBaseRate = tariffBaseRate;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getCategoryCode() { return categoryCode; }
    public void setCategoryCode(int categoryCode) { this.categoryCode = categoryCode; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getTariffBaseRate() { return tariffBaseRate; }
    public void setTariffBaseRate(Double tariffBaseRate) { this.tariffBaseRate = tariffBaseRate; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}
