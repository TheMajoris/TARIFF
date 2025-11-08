package com.cs203.core.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.cs203.core.dto.CreateNationalTariffLinesDto;
import com.cs203.core.dto.GenericResponse;
import com.cs203.core.dto.NationalTariffLinesDto;
import com.cs203.core.dto.ProductCategoriesDto;
import com.cs203.core.entity.CountryEntity;
import com.cs203.core.entity.NationalTariffLinesEntity;
import com.cs203.core.entity.ProductCategoriesEntity;
import com.cs203.core.repository.CountryRepository;
import com.cs203.core.repository.NationalTariffLinesRepository;
import com.cs203.core.repository.ProductCategoriesRepository;
import com.cs203.core.service.NationalTariffLinesService;

import jakarta.persistence.EntityNotFoundException;

//want to create, update and delete national tariff lines
@Service
public class NationalTariffLinesServiceImpl implements NationalTariffLinesService {
    @Autowired
    private NationalTariffLinesRepository nationalTariffLinesRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private ProductCategoriesRepository productCategoriesRepository;

    // create new nationalTariffLines
    public NationalTariffLinesDto createNationalTariffLines(CreateNationalTariffLinesDto createDto) {
        NationalTariffLinesEntity entity = convertToEntity(createDto);
        NationalTariffLinesEntity savedEntity = nationalTariffLinesRepository.save(entity);
        return convertToDto(savedEntity);
    }

    // update NationalTariffLines by id
    public GenericResponse<NationalTariffLinesDto> updateNationalTariffLines(NationalTariffLinesDto dto,
            Long nationalTariffLinesId) {
        // uses passed in nationalTariffLinesId to find if the id exists in the db or
        // not
        NationalTariffLinesEntity existingEntity = nationalTariffLinesRepository.findById(nationalTariffLinesId)
                .orElse(null);
        if (existingEntity == null) {
            return new GenericResponse<NationalTariffLinesDto>(HttpStatus.NOT_FOUND,
                    "National Tariff Line not found with id: " + nationalTariffLinesId, null);
        }

        // Verify Country Code exists
        CountryEntity country = getCountryByCode(dto.getCountryCode());

        // Verify Parent HS Code exists if provided
        Optional<ProductCategoriesEntity> parentHs = productCategoriesRepository
                .findByCategoryCode(dto.getParentHsCode().getCategoryCode());
        if (parentHs == null) {
            return new GenericResponse<NationalTariffLinesDto>(HttpStatus.NOT_FOUND,
                    "Parent HS Code not found with code: " + dto.getParentHsCode().getCategoryCode(), null);
        }

        // Update fields, excluding createdBy and createdAt
        existingEntity.setCountry(country);
        existingEntity.setTariffLineCode(dto.getTariffLineCode());
        existingEntity.setDescription(dto.getDescription());
        existingEntity.setParentHsCode(parentHs.get());
        existingEntity.setLevel(dto.getLevel());
        // for the Dto to return as a response
        NationalTariffLinesEntity updatedEntity = nationalTariffLinesRepository.save(existingEntity);

        return new GenericResponse<NationalTariffLinesDto>(HttpStatus.OK,
                "Successfully updated National Tariff Lines", convertToDto(updatedEntity));

    }

    // delete nationalTariffLines by id
    public GenericResponse<Void> deleteNationalTariffLines(Long nationalTariffLinesId) {
        if (!nationalTariffLinesRepository.existsById(nationalTariffLinesId)) {
            return new GenericResponse<Void>(HttpStatus.NOT_FOUND, "National Tariff Line not found with id: " + nationalTariffLinesId, null);
        }
        // if found, delete it
        nationalTariffLinesRepository.deleteById(nationalTariffLinesId);
        return new GenericResponse<Void>(HttpStatus.OK, "Successfully deleted National Tariff Line", null);
    }

    // convert the Lines entity to dto
    private NationalTariffLinesDto convertToDto(NationalTariffLinesEntity entity) {
        NationalTariffLinesDto dto = new NationalTariffLinesDto();
        dto.setId(entity.getId());
        dto.setCountryCode(entity.getCountry().getCountryCode());
        dto.setTariffLineCode(entity.getTariffLineCode());
        dto.setDescription(entity.getDescription());
        dto.setLevel(entity.getLevel());
        dto.setCreatedBy(entity.getCreatedBy().getId());
        dto.setUpdatedBy(entity.getUpdatedBy().getId());
        if (entity.getParentHsCode() != null) {
            dto.setParentHsCode(convertToDto(entity.getParentHsCode()));
        }
        return dto;
    }

    // convert ProductCategoriesEntity to ProductCategoriesDto for ConvertToDto for
    // NationalTariffLinesDto for ParentHsCode
    private ProductCategoriesDto convertToDto(ProductCategoriesEntity entity) {
        ProductCategoriesDto dto = new ProductCategoriesDto();
        dto.setId(entity.getId());
        dto.setCategoryCode(entity.getCategoryCode());
        dto.setCategoryName(entity.getCategoryName());
        dto.setDescription(entity.getDescription());
        dto.setIsActive(entity.getIsActive());
        return dto;
    }

    private NationalTariffLinesEntity convertToEntity(CreateNationalTariffLinesDto dto) {
        CountryEntity country = getCountryByCode(dto.getCountryCode());
        ProductCategoriesEntity parentHs = null;
        if (dto.getParentHsCode() != null) {
            parentHs = productCategoriesRepository.findByCategoryCode(dto.getParentHsCode())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Product Category not found with code: " + dto.getParentHsCode()));
        }
        String tariffLineCode = dto.getTariffLineCode();
        String description = dto.getDescription();
        Integer level = dto.getLevel();
        return new NationalTariffLinesEntity(country, tariffLineCode, description, parentHs, level);
    }

    private CountryEntity getCountryByCode(String countryCode) {
        return countryRepository.findByCountryCode(countryCode)
                .orElseThrow(() -> new EntityNotFoundException("Country not found with code: " + countryCode));
    }
}
