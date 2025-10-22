package com.cs203.core.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cs203.core.dto.CreateTariffRateDto;
import com.cs203.core.dto.GenericResponse;
import com.cs203.core.dto.ProductCategoriesDto;
import com.cs203.core.dto.TariffRateDto;
import com.cs203.core.entity.CountryEntity;
import com.cs203.core.entity.ProductCategoriesEntity;
import com.cs203.core.entity.TariffRateEntity;
import com.cs203.core.repository.CountryRepository;
import com.cs203.core.repository.ProductCategoriesRepository;
import com.cs203.core.repository.TariffRateRepository;
import com.cs203.core.service.TariffRateService;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class TariffRateServiceImpl implements TariffRateService {

    @Autowired
    private TariffRateRepository tariffRateRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private ProductCategoriesRepository productCategoriesRepository;

    // Get all tariff rates
    public List<TariffRateDto> getAllTariffRates() {
        return tariffRateRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Get tariff rate by ID
    public GenericResponse<TariffRateDto> getTariffRateById(Long id) {
        TariffRateEntity entity = tariffRateRepository.findById(id)
                .orElse(null);
        if (entity == null) {
            return new GenericResponse<TariffRateDto>(HttpStatus.NOT_FOUND, "Tariff Rate not found with id: " + id,
                    null);
        }
        return new GenericResponse<TariffRateDto>(HttpStatus.OK, "Found", convertToDto(entity));
    }

    // Create a new tariff rate
    public GenericResponse<TariffRateDto> createTariffRate(CreateTariffRateDto dto) {
        if (!countryRepository.existsByCountryCode(dto.getExportingCountryCode())) {
            return new GenericResponse<TariffRateDto>(HttpStatus.NOT_FOUND, "Exporting Country code does not exist", null);
        }
        if (!countryRepository.existsByCountryCode(dto.getImportingCountryCode())) {
            return new GenericResponse<TariffRateDto>(HttpStatus.NOT_FOUND, "Importing Country code does not exist", null);
        }
        if (!productCategoriesRepository.existsByCategoryCode(dto.getHsCode())) {
            return new GenericResponse<TariffRateDto>(HttpStatus.NOT_FOUND, "HS code does not exist", null);
        }

        TariffRateEntity entity = new TariffRateEntity();
        Optional<CountryEntity> importingCountry = countryRepository.findByCountryCode(dto.getImportingCountryCode());
        Optional<CountryEntity> exportingCountry = countryRepository.findByCountryCode(dto.getExportingCountryCode());
        Optional<ProductCategoriesEntity> productCategory = productCategoriesRepository.findByCategoryCode(dto.getHsCode());

        entity.setTariffRate(dto.getTariffRate());
        entity.setTariffType(dto.getTariffType());
        entity.setRateUnit(dto.getRateUnit());
        entity.setEffectiveDate(dto.getEffectiveDate());
        entity.setExpiryDate(dto.getExpiryDate());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setPreferentialTariff(dto.getPreferentialTariff());
        entity.setImportingCountry(importingCountry.get());
        entity.setExportingCountry(exportingCountry.get());
        entity.setProductCategory(productCategory.get());

        TariffRateEntity savedEntity = tariffRateRepository.save(entity);
        return new GenericResponse<TariffRateDto>(HttpStatus.CREATED, "Successfully created Tariff Rate", convertToDto(savedEntity));
    }

    // Update tariff rate
    public GenericResponse<TariffRateDto> updateTariffRate(TariffRateDto dto, Long tariffRateId) {
        TariffRateEntity existingEntity = tariffRateRepository.findById(tariffRateId)
                .orElse(null);

        if (existingEntity == null) {
            return new GenericResponse<TariffRateDto>(HttpStatus.NOT_FOUND,
                    "Tariff Rate not found with id: " + dto.getId(), null);
        }

        // Verify country code exists
        CountryEntity importingCountry = getCountryEntityByCode(dto.getImportingCountryCode());
        if (importingCountry == null) {
            return new GenericResponse<TariffRateDto>(HttpStatus.NOT_FOUND,
                    "Importing Country not found with code: " + dto.getImportingCountryCode(), null);
        }
        CountryEntity exportingCountry = getCountryEntityByCode(dto.getExportingCountryCode());
        if (exportingCountry == null) {
            return new GenericResponse<TariffRateDto>(HttpStatus.NOT_FOUND,
                    "Exporting Country not found with code: " + dto.getExportingCountryCode(), null);
        }

        // Verify product category exists
        // User has to create new product category separately if tariff needs a new one.
        ProductCategoriesEntity productCategory = productCategoriesRepository
                .findByCategoryCode(dto.getProductCategory().getCategoryCode())
                .orElse(null);
                
        if (productCategory == null) {
            return new GenericResponse<TariffRateDto>(HttpStatus.NOT_FOUND,
                    "Product Category not found with code: " + dto.getProductCategory().getCategoryCode(), null);
        }

        // Update fields
        existingEntity.setTariffRate(dto.getTariffRate());
        existingEntity.setTariffType(dto.getTariffType());
        existingEntity.setRateUnit(dto.getRateUnit());
        existingEntity.setEffectiveDate(dto.getEffectiveDate());
        existingEntity.setExpiryDate(dto.getExpiryDate());
        // CreatedAt should not be updated
        existingEntity.setUpdatedAt(LocalDateTime.now());
        existingEntity.setPreferentialTariff(dto.getPreferentialTariff());
        existingEntity.setImportingCountry(importingCountry);
        existingEntity.setExportingCountry(exportingCountry);
        existingEntity.setProductCategory(productCategory);

        TariffRateEntity updatedEntity = tariffRateRepository.save(existingEntity);
        return new GenericResponse<TariffRateDto>(HttpStatus.OK, "Successfully updated Tariff Rate",
                convertToDto(updatedEntity));
    }

    public GenericResponse<Void> deleteTariffRate(Long tariffRateId) {
        if (!tariffRateRepository.existsById(tariffRateId)) {
            return new GenericResponse<Void>(HttpStatus.NOT_FOUND, "Tariff Rate not found with id: " + tariffRateId,
                    null);
        }
        tariffRateRepository.deleteById(tariffRateId);
        // Eventually, handle national tariff lines associated with this tariff rate
        return new GenericResponse<Void>(HttpStatus.OK, "Successfully deleted Tariff Rate", null);
    }

    // get tariffRate
    public BigDecimal getFinalPrice(Long importingCountryId, Long exportingCountryId, Integer hsCode,
            BigDecimal initialPrice, LocalDate date) {
        // get List of rates based on input and attributed data in repo
        BigDecimal tariffRate = getLowestActiveTariffRate(importingCountryId, exportingCountryId, hsCode, initialPrice, date);
        
        // If no tariff data found (tariffRate = -1), return initial price
        if (tariffRate.compareTo(new BigDecimal("-1")) == 0) {
            return initialPrice;
        }
        
        // Convert percentage to decimal (divide by 100)
        BigDecimal tariffRateDecimal = tariffRate.divide(new BigDecimal("100"));
        BigDecimal finalPrice = initialPrice.add(initialPrice.multiply(tariffRateDecimal));
        return finalPrice;
    }

    // get LowestTariffRate
    public BigDecimal getLowestActiveTariffRate(Long importingCountryId, Long exportingCountryId, Integer hsCode,
            BigDecimal initialPrice, LocalDate date) {
        
        // API Endpoint for Country-specific tariffs is not ready yet, Use first 6 digits only
        Integer sixDigitHsCode = Integer.parseInt(String.valueOf(hsCode).substring(0, Math.min(6, String.valueOf(hsCode).length())));
        Optional<TariffRateEntity> currentTariffRates = tariffRateRepository
                .findCurrentTariffRate(importingCountryId, exportingCountryId, sixDigitHsCode, date);
        
        // Check if any tariff data exists
        if (!currentTariffRates.isPresent()) {
            // Return -1 to indicate no data found (instead of 0 which could be a valid tariff rate)
            return new BigDecimal("-1");
        }
        
        // get n set lowest rate
        BigDecimal tariffRate = currentTariffRates
                .stream()
                .map(TariffRateEntity::getTariffRate)
                .min(Comparable::compareTo)
                .orElse(BigDecimal.ZERO);
        return tariffRate;
    }

    // get TariffCost
    public BigDecimal getTariffCost(BigDecimal finalPrice, BigDecimal initialPrice) {
        return finalPrice.subtract(initialPrice);
    }

    /*
     * Helper section
     */

    private TariffRateDto convertToDto(TariffRateEntity entity) {
        TariffRateDto dto = new TariffRateDto();
        dto.setId(entity.getId());
        dto.setTariffRate(entity.getTariffRate());
        dto.setTariffType(entity.getTariffType());
        dto.setRateUnit(entity.getRateUnit());
        dto.setEffectiveDate(entity.getEffectiveDate());
        dto.setExpiryDate(entity.getExpiryDate());
        dto.setPreferentialTariff(entity.getPreferentialTariff());
        dto.setImportingCountryCode(entity.getImportingCountry().getCountryCode());
        dto.setExportingCountryCode(entity.getExportingCountry().getCountryCode());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setProductCategory(convertToDto(entity.getProductCategory()));
        return dto;
    }

    // private TariffRateEntity convertToEntity(CreateTariffRateDto dto) {
    //     TariffRateEntity entity = new TariffRateEntity(dto.get)

    //     if (productCategory.getId() == null) {
    //         productCategory = productCategoriesRepository.save(productCategory);
    //     }

    //     // // Handle National Tariff Line
    //     // if (dto.getNationalTariffLine() != null) {
    //     // // Check if national tariff line already exists for this country and tariff
    //     // line code
    //     // if (!nationalTariffLinesRepository.existsByCountryAndTariffLineCode(
    //     // importingCountry,
    //     // dto.getNationalTariffLine().getTariffLineCode())) {

    //     // // Create new national tariff line if it doesn't exist
    //     // NationalTariffLinesEntity nationalTariffLine = new NationalTariffLinesEntity(
    //     // importingCountry,
    //     // dto.getNationalTariffLine().getTariffLineCode(),
    //     // dto.getNationalTariffLine().getDescription(),
    //     // productCategory, // Use the product category as parent HS code
    //     // dto.getNationalTariffLine().getLevel()
    //     // );
    //     // nationalTariffLinesRepository.save(nationalTariffLine);
    //     // }
    //     // }

    //     // Create and return the TariffRate entity
    //     return new TariffRateEntity(
    //             dto.getImportingCountryCode(),
    //             exportingCountry,
    //             productCategory,
    //             dto.getTariffRate(),
    //             dto.getTariffType(),
    //             dto.getRateUnit(),
    //             dto.getEffectiveDate(),
    //             dto.getExpiryDate(),
    //             dto.getPreferentialTariff());
    // }

    private ProductCategoriesDto convertToDto(ProductCategoriesEntity entity) {
        ProductCategoriesDto dto = new ProductCategoriesDto();
        dto.setId(entity.getId());
        dto.setCategoryCode(entity.getCategoryCode());
        dto.setCategoryName(entity.getCategoryName());
        dto.setDescription(entity.getDescription());
        dto.setIsActive(entity.getIsActive());
        return dto;
    }

    private CountryEntity getCountryEntityByCode(String countryCode) {
        return countryRepository.findByCountryCode(countryCode)
                .orElseThrow(() -> new EntityNotFoundException("Country not found with code: " + countryCode));
    }
}
