package com.cs203.core.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "country")
public class CountryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "country_id")
    private Long id;

    @Column(name = "country_code", nullable = false, unique = true, length = 2)
    @NotBlank(message = "Country code is required")
    @Size(min = 2, max = 2, message = "Country code must be 2 characters")
    private String countryCode;

    @Column(name = "country_name", nullable = false, length = 100)
    @NotBlank(message = "Country name is required")
    @Size(max = 100, message = "Country name cannot exceed 100 characters")
    private String countryName;

    @Column(name = "region", length = 100)
    @Size(max = 100, message = "Region cannot exceed 100 characters")
    private String region;

    @Column(name = "currency_code", length = 3)
    @Size(min = 3, max = 3, message = "Currency code must be 3 characters")
    private String currencyCode;

    // Relationship mappings for cascade operations
    @OneToMany(mappedBy = "importingCountry", cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private List<SavedCalculationsEntity> importingCalculations = new ArrayList<>();

    @OneToMany(mappedBy = "exportingCountry", cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private List<SavedCalculationsEntity> exportingCalculations = new ArrayList<>();

    @OneToMany(mappedBy = "importingCountry", cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private List<TariffRateEntity> importingTariffRates = new ArrayList<>();

    @OneToMany(mappedBy = "exportingCountry", cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private List<TariffRateEntity> exportingTariffRates = new ArrayList<>();

    @OneToMany(mappedBy = "country", cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private List<NationalTariffLinesEntity> nationalTariffLines = new ArrayList<>();

    // Constructors
    public CountryEntity() {}

    public CountryEntity(String countryCode, String countryName, String region, String currencyCode) {
        this.countryCode = countryCode;
        this.countryName = countryName;
        this.region = region;
        this.currencyCode = currencyCode;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCountryCode() { return countryCode; }
    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }

    public String getCountryName() { return countryName; }
    public void setCountryName(String countryName) { this.countryName = countryName; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }

    public List<SavedCalculationsEntity> getImportingCalculations() { return importingCalculations; }
    public void setImportingCalculations(List<SavedCalculationsEntity> importingCalculations) { this.importingCalculations = importingCalculations; }

    public List<SavedCalculationsEntity> getExportingCalculations() { return exportingCalculations; }
    public void setExportingCalculations(List<SavedCalculationsEntity> exportingCalculations) { this.exportingCalculations = exportingCalculations; }

    public List<TariffRateEntity> getImportingTariffRates() { return importingTariffRates; }
    public void setImportingTariffRates(List<TariffRateEntity> importingTariffRates) { this.importingTariffRates = importingTariffRates; }

    public List<TariffRateEntity> getExportingTariffRates() { return exportingTariffRates; }
    public void setExportingTariffRates(List<TariffRateEntity> exportingTariffRates) { this.exportingTariffRates = exportingTariffRates; }

    public List<NationalTariffLinesEntity> getNationalTariffLines() { return nationalTariffLines; }
    public void setNationalTariffLines(List<NationalTariffLinesEntity> nationalTariffLines) { this.nationalTariffLines = nationalTariffLines; }
}
