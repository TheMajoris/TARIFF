package com.cs203.core.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

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

    // Constructors
    public CountryEntity() {}

    public CountryEntity(String countryCode, String countryName, String region, String currencyCode) {
        this.countryCode = countryCode;
        this.countryName = countryName;
        this.region = region;
        this.currencyCode = currencyCode;
    }

    // Getters and Setters
    public long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCountryCode() { return countryCode; }
    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }

    public String getCountryName() { return countryName; }
    public void setCountryName(String countryName) { this.countryName = countryName; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
}
