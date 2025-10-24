package com.cs203.core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "national_tariff_lines")
public class NationalTariffLinesEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tariff_line_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false)
    @NotNull(message = "Country is required")
    private CountryEntity country;

    @Column(name = "tariff_line_code", nullable = false, length = 20)
    @NotBlank(message = "Tariff line code is required")
    @Size(max = 20, message = "Tariff line code cannot exceed 20 characters")
    private String tariffLineCode;

    @Column(name = "description", length = 500)
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "parent_hs_code", 
        referencedColumnName = "hs_code",
        foreignKey = @ForeignKey(
            name = "fk_national_tariff_lines_product_category",
            foreignKeyDefinition = "FOREIGN KEY (parent_hs_code) REFERENCES product_categories(hs_code) ON UPDATE CASCADE ON DELETE RESTRICT"
        )
    )
    private ProductCategoriesEntity parentHsCode;

    @Column(name = "level")
    @Min(value = 1, message = "Level must be at least 1")
    @Max(value = 10, message = "Level cannot exceed 10")
    private Integer level;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private UserEntity createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private UserEntity updatedBy;

    // Constructors
    public NationalTariffLinesEntity() {}

    public NationalTariffLinesEntity(CountryEntity country, String tariffLineCode, String description, ProductCategoriesEntity parentHsCode, Integer level) {
        this.country = country;
        this.tariffLineCode = tariffLineCode;
        this.description = description;
        this.parentHsCode = parentHsCode;
        this.level = level;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public CountryEntity getCountry() { return country; }
    public void setCountry(CountryEntity country) { this.country = country; }

    public String getTariffLineCode() { return tariffLineCode; }
    public void setTariffLineCode(String tariffLineCode) { this.tariffLineCode = tariffLineCode; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public ProductCategoriesEntity getParentHsCode() { return parentHsCode; }
    public void setParentHsCode(ProductCategoriesEntity parentHsCode) { this.parentHsCode = parentHsCode; }

    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }

    public UserEntity getCreatedBy() { return createdBy; }
    public void setCreatedBy(UserEntity createdBy) { this.createdBy = createdBy; }

    public UserEntity getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(UserEntity updatedBy) { this.updatedBy = updatedBy; }
}
