package com.cs203.core.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "saved_calculations")
public class SavedCalculationsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "calculation_id")
    private Long id;

    @Column(name = "calculation_name", nullable = false, length = 100)
    @NotBlank(message = "Calculation name is required")
    @Size(max = 100, message = "Calculation name cannot exceed 100 characters")
    private String calculationName;

    @Column(name = "product_value", nullable = false, precision = 12, scale = 2)
    @NotNull(message = "Product value is required")
    @DecimalMin(value = "0.01", message = "Product value must be greater than 0")
    private BigDecimal productValue;

    @Column(name = "currency_code", nullable = false, length = 3)
    @NotBlank(message = "Currency code is required")
    @Size(min = 3, max = 3, message = "Currency code must be 3 characters")
    private String currencyCode;

    @Column(name = "tariff_rate", nullable = false, precision = 10, scale = 4)
    @NotNull(message = "Tariff rate is required")
    @DecimalMin(value = "0.0", message = "Tariff rate must be non-negative")
    private BigDecimal tariffRate;

    @Column(name = "unit_quantity", precision = 10, scale = 4)
    @DecimalMin(value = "0.0", message = "Unit quantity must be positive")
    private BigDecimal unitQuantity; // amt of unit that tariff applies to

    @Column(name = "rate_unit", length = 20)
    @Size(max = 20, message = "Rate unit cannot exceed 20 characters")
    private String rateUnit; // eg "kg", "g", "kWh"

    @Column(name = "tariff_type", nullable = false, length = 50)
    @NotBlank(message = "Tariff type is required")
    @Size(max = 50, message = "Tariff type cannot exceed 50 characters")
    private String tariffType;

    @Column(name = "calculated_tariff_cost", nullable = false, precision = 12, scale = 2)
    @NotNull(message = "Calculated tariff cost is required")
    @DecimalMin(value = "0.0", message = "Calculated tariff cost must be non-negative")
    private BigDecimal calculatedTariffCost;

    @Column(name = "total_cost", nullable = false, precision = 12, scale = 2)
    @NotNull(message = "Total cost is required")
    @DecimalMin(value = "0.0", message = "Total cost must be non-negative")
    private BigDecimal totalCost;

    @Column(name = "notes", length = 500)
    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Foreign Key Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    @NotNull(message = "User is required")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "importing_country_id", referencedColumnName = "country_id", nullable = false)
    @NotNull(message = "Importing country is required")
    private CountryEntity importingCountry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exporting_country_id", referencedColumnName = "country_id", nullable = false)
    @NotNull(message = "Exporting country is required")
    private CountryEntity exportingCountry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hs_code", referencedColumnName = "hs_code", nullable = false)
    @NotNull(message = "Product category is required")
    private ProductCategoriesEntity productCategory;

    // Constructors
    public SavedCalculationsEntity() {
    }

    public SavedCalculationsEntity(String calculationName, UserEntity user, CountryEntity importingCountry,
            CountryEntity exportingCountry, ProductCategoriesEntity productCategory,
            BigDecimal productValue, String currencyCode, BigDecimal tariffRate, BigDecimal unitQuantity,
            String rateUnit,
            String tariffType, BigDecimal calculatedTariffCost, BigDecimal totalCost,
            String notes) {
        this.calculationName = calculationName;
        this.user = user;
        this.importingCountry = importingCountry;
        this.exportingCountry = exportingCountry;
        this.productCategory = productCategory;
        this.productValue = productValue;
        this.currencyCode = currencyCode;
        this.tariffRate = tariffRate;
        this.unitQuantity = unitQuantity;
        this.rateUnit = rateUnit;
        this.tariffType = tariffType;
        this.calculatedTariffCost = calculatedTariffCost;
        this.totalCost = totalCost;
        this.notes = notes;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCalculationName() {
        return calculationName;
    }

    public void setCalculationName(String calculationName) {
        this.calculationName = calculationName;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public CountryEntity getImportingCountry() {
        return importingCountry;
    }

    public void setImportingCountry(CountryEntity importingCountry) {
        this.importingCountry = importingCountry;
    }

    public CountryEntity getExportingCountry() {
        return exportingCountry;
    }

    public void setExportingCountry(CountryEntity exportingCountry) {
        this.exportingCountry = exportingCountry;
    }

    public ProductCategoriesEntity getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategoriesEntity productCategory) {
        this.productCategory = productCategory;
    }

    public BigDecimal getProductValue() {
        return productValue;
    }

    public void setProductValue(BigDecimal productValue) {
        this.productValue = productValue;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigDecimal getTariffRate() {
        return tariffRate;
    }

    public void setTariffRate(BigDecimal tariffRate) {
        this.tariffRate = tariffRate;
    }

    public String getTariffType() {
        return tariffType;
    }

    public void setTariffType(String tariffType) {
        this.tariffType = tariffType;
    }

    public BigDecimal getUnitQuantity() {
        return unitQuantity;
    }

    public void setUnitQuantity(BigDecimal unitQuantity) {
        this.unitQuantity = unitQuantity;
    }

    public String getRateUnit() {
        return rateUnit;
    }

    public void setRateUnit(String rateUnit) {
        this.rateUnit = rateUnit;
    }

    public BigDecimal getCalculatedTariffCost() {
        return calculatedTariffCost;
    }

    public void setCalculatedTariffCost(BigDecimal calculatedTariffCost) {
        this.calculatedTariffCost = calculatedTariffCost;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "SavedCalculationsEntity{" +
                "id=" + id +
                ", calculationName='" + calculationName + '\'' +
                ", productValue=" + productValue +
                ", currencyCode='" + currencyCode + '\'' +
                ", tariffRate=" + tariffRate +
                ", unitQuantity=" + unitQuantity +
                ", rateUnit=" + rateUnit +
                ", tariffType='" + tariffType + '\'' +
                ", calculatedTariffCost=" + calculatedTariffCost +
                ", totalCost=" + totalCost +
                ", notes='" + notes + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}