package com.cs203.core.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.cs203.core.dto.GenericResponse;
import com.cs203.core.dto.SavedCalculationDto;
import com.cs203.core.dto.requests.SaveCalculationRequestDTO;
import com.cs203.core.entity.CountryEntity;
import com.cs203.core.entity.ProductCategoriesEntity;
import com.cs203.core.entity.SavedCalculationsEntity;
import com.cs203.core.entity.UserEntity;
import com.cs203.core.repository.CountryRepository;
import com.cs203.core.repository.ProductCategoriesRepository;
import com.cs203.core.repository.SavedCalculationsRepository;
import com.cs203.core.repository.UserRepository;
import com.cs203.core.service.CalculationHistoryService;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CalculationHistoryImpl implements CalculationHistoryService {
    @Autowired
    private SavedCalculationsRepository savedCalculationsRepository;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private ProductCategoriesRepository productCategoriesRepository;
    @Autowired
    private UserRepository userRepository;

    // save
    /*
     * First check if its valid user, if role ≠ TRADER return 403
     * Dto to obtain all params needed to save, return 400 if missing fields
     * All correct, save to user entity, get UserId from Dto and call userEntity
     * from repo - linked to correct user ID
     */
    @Override
    @Transactional
    public GenericResponse<SavedCalculationDto> saveCalculation(SaveCalculationRequestDTO requestDTO, Long userId) {
        SavedCalculationsEntity entity = convertToEntity(requestDTO, userId); // use converToEntity to map Dto to Entity
        SavedCalculationsEntity savedEntity = savedCalculationsRepository.save(entity); // then save the entity to repo
        return new GenericResponse<SavedCalculationDto>(HttpStatus.CREATED, "Calculation saved successfully",
                convertToDto(savedEntity)); // send a message that saving to repo is successful

    }

    // get
    /*
     * Return 401 if token missing/invalid
     * First check if valid user, if role ≠ TRADER return 403
     * Returns the calculation by SavedCalculationId and the user that created it
     * 
     */
    @Override
    @Transactional(readOnly = true) //makes sure we dun accidentally write to DB
    public GenericResponse<List<SavedCalculationDto>> getCalculationsByUserId(Long userId) {
        // obtains the list of all enitites created by this user
        List<SavedCalculationsEntity> entities = savedCalculationsRepository.findByUserId(userId);
        List<SavedCalculationDto> dtos = entities.stream() // Stream all the entities
                .map(this::convertToDto) // map all the entities into a dto using convertToDto
                .toList(); // then collect into a list of dtos
        return new GenericResponse<List<SavedCalculationDto>>(HttpStatus.OK, "Calculations fetched successfully", dtos); //returns list successfully
    }

    // Converts the Dto to an Entity to be put into the repository
    private SavedCalculationsEntity convertToEntity(SaveCalculationRequestDTO requestDto, Long userId) {
        //Entities to be fetched using respective repos and passed into Entity
        ProductCategoriesEntity productCategory = productCategoriesRepository
                .findByCategoryCode(requestDto.productCategoryCode())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Product category not found with code: " + requestDto.productCategoryCode()));
        CountryEntity importingCountry = getCountryByCode(requestDto.importingCountryCode());
        CountryEntity exportingCountry = getCountryByCode(requestDto.exportingCountryCode());
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        SavedCalculationsEntity entity = new SavedCalculationsEntity();
        entity.setCalculationName(requestDto.calculationName());
        entity.setProductValue(requestDto.productValue());
        entity.setCurrencyCode(requestDto.currencyCode());
        entity.setTariffRate(requestDto.tariffRate());
        entity.setTariffType(requestDto.tariffType());
        entity.setCalculatedTariffCost(requestDto.calculatedTariffCost());
        entity.setTotalCost(requestDto.totalCost());
        entity.setNotes(requestDto.notes());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setUser(user);
        entity.setImportingCountry(importingCountry);
        entity.setExportingCountry(exportingCountry);
        entity.setProductCategory(productCategory);
        return entity;
    }

    // Converts the Entity to a Dto to be sent as a response
    private SavedCalculationDto convertToDto(SavedCalculationsEntity entity) {
        //grab all the relevant datatypes from the entity and pass into dto to set
        SavedCalculationDto dto = new SavedCalculationDto();
        dto.setId(entity.getId());
        dto.setCalculationName(entity.getCalculationName());
        dto.setProductValue(entity.getProductValue());
        dto.setCurrencyCode(entity.getCurrencyCode());
        dto.setTariffRate(entity.getTariffRate());
        dto.setTariffType(entity.getTariffType());
        dto.setCalculatedTariffCost(entity.getCalculatedTariffCost());
        dto.setTotalCost(entity.getTotalCost());
        dto.setNotes(entity.getNotes());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setUserId(entity.getUser().getId());
        dto.setImportingCountryCode(entity.getImportingCountry().getCountryCode());
        dto.setExportingCountryCode(entity.getExportingCountry().getCountryCode());
        dto.setProductCategoryCode(entity.getProductCategory().getCategoryCode());
        return dto;
    }

    private CountryEntity getCountryByCode(String countryCode) {
        return countryRepository.findByCountryCode(countryCode)
                .orElseThrow(() -> new EntityNotFoundException("Country not found with code: " + countryCode));
    }
}
