package com.cs203.core.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tariff_rates")
public class TariffRateEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tariff_id")
    private Long id;

    @Column(name = "tariff_rate", nullable = false, precision = 10, scale = 4)
    @NotNull(message = "Tariff rate is required")
    @DecimalMin(value = "0.0", message = "Tariff rate must be non-negative")
    @DecimalMax(value = "999.9999", message = "Tariff rate cannot exceed 999.9999")
    private BigDecimal tariffRate;

    @Column(name = "tariff_type", nullable = false, length = 50)
    @NotBlank(message = "Tariff type is required")
    @Size(max = 50, message = "Tariff type cannot exceed 50 characters")
    private String tariffType;

    @Column(name = "rate_unit", length = 20)
    @Size(max = 20, message = "Rate unit cannot exceed 20 characters")
    private String rateUnit;

    @Column(name = "effective_date", nullable = false)
    @NotNull(message = "Effective date is required")
    private LocalDate effectiveDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "preferential_tariff", nullable = false)
    private Boolean preferentialTariff = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Foreign Key Relationships (REMOVED insertable = false, updatable = false)
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
    @NotNull(message = "HS code is required")
    private ProductCategoriesEntity productCategory;

    // Constructors
    public TariffRateEntity() {}

    public TariffRateEntity(CountryEntity importingCountry, CountryEntity exportingCountry, 
                           ProductCategoriesEntity productCategory, BigDecimal tariffRate, 
                           String tariffType, String rateUnit, LocalDate effectiveDate, 
                           LocalDate expiryDate, Boolean preferentialTariff) {
        this.importingCountry = importingCountry;
        this.exportingCountry = exportingCountry;
        this.productCategory = productCategory;
        this.tariffRate = tariffRate;
        this.tariffType = tariffType;
        this.rateUnit = rateUnit;
        this.effectiveDate = effectiveDate;
        this.expiryDate = expiryDate;
        this.preferentialTariff = preferentialTariff;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public CountryEntity getImportingCountry() { return importingCountry; }
    public void setImportingCountry(CountryEntity importingCountry) { this.importingCountry = importingCountry; }

    public CountryEntity getExportingCountry() { return exportingCountry; }
    public void setExportingCountry(CountryEntity exportingCountry) { this.exportingCountry = exportingCountry; }

    public ProductCategoriesEntity getProductCategory() { return productCategory; }
    public void setProductCategory(ProductCategoriesEntity productCategory) { this.productCategory = productCategory; }

    public BigDecimal getTariffRate() { return tariffRate; }
    public void setTariffRate(BigDecimal tariffRate) { this.tariffRate = tariffRate; }

    public String getTariffType() { return tariffType; }
    public void setTariffType(String tariffType) { this.tariffType = tariffType; }

    public String getRateUnit() { return rateUnit; }
    public void setRateUnit(String rateUnit) { this.rateUnit = rateUnit; }

    public LocalDate getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(LocalDate effectiveDate) { this.effectiveDate = effectiveDate; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public Boolean getPreferentialTariff() { return preferentialTariff; }
    public void setPreferentialTariff(Boolean preferentialTariff) { this.preferentialTariff = preferentialTariff; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Convenience getters for backward compatibility with your repository queries
    public Long getImportingCountryId() {
        return importingCountry != null ? importingCountry.getId() : null;
    }

    public Long getExportingCountryId() {
        return exportingCountry != null ? exportingCountry.getId() : null;
    }

    public Integer getHsCode() {
        return productCategory != null ? productCategory.getCategoryCode() : null;
    }

    // Helper methods
    public boolean isCurrentlyActive() {
        LocalDate now = LocalDate.now();
        return (effectiveDate == null || !effectiveDate.isAfter(now)) && 
               (expiryDate == null || !expiryDate.isBefore(now));
    }

    public boolean isExpired() {
        return expiryDate != null && expiryDate.isBefore(LocalDate.now());
    }

    public boolean isEffective() {
        return effectiveDate != null && !effectiveDate.isAfter(LocalDate.now());
    }
}
