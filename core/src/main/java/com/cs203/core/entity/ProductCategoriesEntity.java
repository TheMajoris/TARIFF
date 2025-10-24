package com.cs203.core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "product_categories")
public class ProductCategoriesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(name = "hs_code", nullable = false, unique = true)
    @NotNull(message = "HS code is required")
    @Min(value = 100000, message = "HS code must be 6 digits")
    @Max(value = 999999, message = "HS code must be 6 digits")
    private Integer categoryCode;

    @Column(name = "category_name", nullable = false, length = 100)
    @NotBlank(message = "Category name is required")
    @Size(max = 100, message = "Category name cannot exceed 100 characters")
    private String categoryName;

    @Column(name = "description", length = 500)
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @OneToMany(mappedBy = "productCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TariffRateEntity> tariffRates = new ArrayList<>();

    @OneToMany(mappedBy = "parentHsCode", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NationalTariffLinesEntity> nationalTariffLines = new ArrayList<>();

    @OneToMany(mappedBy = "productCategory", cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private List<SavedCalculationsEntity> savedCalculations = new ArrayList<>();

    // Constructors
    public ProductCategoriesEntity() {
    }

    public ProductCategoriesEntity(Long id, Integer categoryCode, String categoryName, String description, Boolean isActive) {
        this.categoryCode = categoryCode;
        this.categoryName = categoryName;
        this.description = description;
        this.isActive = isActive;
    }


    public ProductCategoriesEntity(Integer categoryCode, String categoryName, String description, Boolean isActive) {
        this.categoryCode = categoryCode;
        this.categoryName = categoryName;
        this.description = description;
        this.isActive = isActive;
    }

    public ProductCategoriesEntity(Integer categoryCode, String categoryName, String description) {
        this.categoryCode = categoryCode;
        this.categoryName = categoryName;
        this.description = description;
        this.isActive = true;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(Integer categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<TariffRateEntity> getTariffRates() {
        return tariffRates;
    }

    public void setTariffRates(List<TariffRateEntity> tariffRates) {
        this.tariffRates = tariffRates;
    }

    public List<NationalTariffLinesEntity> getNationalTariffLines() {
        return nationalTariffLines;
    }

    public void setNationalTariffLines(List<NationalTariffLinesEntity> nationalTariffLines) {
        this.nationalTariffLines = nationalTariffLines;
    }

    public List<SavedCalculationsEntity> getSavedCalculations() {
        return savedCalculations;
    }

    public void setSavedCalculations(List<SavedCalculationsEntity> savedCalculations) {
        this.savedCalculations = savedCalculations;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
