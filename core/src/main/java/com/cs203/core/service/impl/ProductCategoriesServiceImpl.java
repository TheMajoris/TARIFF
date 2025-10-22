package com.cs203.core.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cs203.core.dto.CreateProductCategoriesDto;
import com.cs203.core.dto.GenericResponse;
import com.cs203.core.dto.ProductCategoriesDto;
import com.cs203.core.entity.ProductCategoriesEntity;
import com.cs203.core.repository.NationalTariffLinesRepository;
import com.cs203.core.repository.ProductCategoriesRepository;
import com.cs203.core.repository.TariffRateRepository;
import com.cs203.core.service.ProductCategoriesService;

@Service
@Transactional
public class ProductCategoriesServiceImpl implements ProductCategoriesService {

    @Autowired
    ProductCategoriesRepository productCategoriesRepository;
    @Autowired
    TariffRateRepository tariffRateRepository;
    @Autowired
    NationalTariffLinesRepository nationalTariffLinesRepository;
    @Override
    
    public List<ProductCategoriesDto> getProductCategories() {
        return productCategoriesRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProductCategoriesDto> getProductCategories(Pageable pageable) {
        return productCategoriesRepository.findAll(pageable)
                .map(entity -> {
                    return convertToDto(entity);
                });
    }

    @Override
    public GenericResponse<ProductCategoriesDto> getProductCategoriesById(Long id) {
        Optional<ProductCategoriesEntity> entity = productCategoriesRepository.findById(id);
        if (entity.isEmpty()) {
            return new GenericResponse<ProductCategoriesDto>(HttpStatus.NOT_FOUND, "Product category not found with id", null);
        }

        return new GenericResponse<ProductCategoriesDto>(HttpStatus.OK, "Product category successfully retrieved", convertToDto(entity.get()));
    }

    @Override
    public GenericResponse<ProductCategoriesDto> createProductCategories(CreateProductCategoriesDto dto) {
        // Check if HSCode exists first
        if (productCategoriesRepository.existsByCategoryCode(dto.getCategoryCode())) {
            ProductCategoriesDto responseDto = new ProductCategoriesDto(null, dto.getCategoryCode(), dto.getCategoryName(), dto.getDescription(), dto.getIsActive());
            return new GenericResponse<ProductCategoriesDto>(HttpStatus.CONFLICT, "Category code (HSCode) already exists", responseDto);
        }

        ProductCategoriesEntity entity = convertToEntity(dto);
        entity = productCategoriesRepository.save(entity);
        return new GenericResponse<ProductCategoriesDto>(HttpStatus.CREATED, "Product category created", convertToDto(entity));
    }

    @Override
    public GenericResponse<ProductCategoriesDto> updateProductCategories(Long id, ProductCategoriesDto dto) {
        Optional<ProductCategoriesEntity> entity = productCategoriesRepository.findById(id);
        if (entity.isEmpty()) {
            return new GenericResponse<ProductCategoriesDto>(HttpStatus.NOT_FOUND, "Product category not found with id", dto);
        }

        if (productCategoriesRepository.existsByCategoryCodeAndIdNot(dto.getCategoryCode(), entity.get().getId())) {
            return new GenericResponse<ProductCategoriesDto>(HttpStatus.CONFLICT, "Product category's code already exists", dto);
        }

        ProductCategoriesEntity updatedEntity = entity.get();
        updatedEntity.setCategoryCode(dto.getCategoryCode());
        updatedEntity.setCategoryName(dto.getCategoryName());
        updatedEntity.setDescription(dto.getDescription());
        updatedEntity.setIsActive(dto.getIsActive());
        // No updatedAt attribute to update

        updatedEntity = productCategoriesRepository.save(updatedEntity);

        return new GenericResponse<ProductCategoriesDto>(HttpStatus.OK, "Successfully updated Product category", convertToDto(updatedEntity));
    }

    @Override
    public GenericResponse<Void> deleteProductCategories(Long id) {
        Optional<ProductCategoriesEntity> optional = productCategoriesRepository.findById(id);
        if (optional.isEmpty()) {
            return new GenericResponse<Void>(HttpStatus.NOT_FOUND, "Product category not found with id", null);
        }

        long relations = tariffRateRepository.countByHsCode(optional.get().getCategoryCode());
        relations += nationalTariffLinesRepository.countByHsCode(optional.get().getCategoryCode());
        if (relations > 0) {
            return new GenericResponse<Void>(HttpStatus.CONFLICT, "Product categories has " + relations + " related tariff rates and national tariff lines. Set request params flag to true to cascade delete", null);
        }

        productCategoriesRepository.deleteById(id);
        return new GenericResponse<Void>(HttpStatus.OK, "Successfully deleted Product category", null);
    }

    @Override
    public GenericResponse<Void> forceDeleteProductCategories(Long id) {
        Optional<ProductCategoriesEntity> optional = productCategoriesRepository.findById(id);
        if (optional.isEmpty()) {
            return new GenericResponse<Void>(HttpStatus.NOT_FOUND, "Product category not found with id", null);
        }

        productCategoriesRepository.deleteById(id);
        return new GenericResponse<Void>(HttpStatus.OK, "Successfully deleted Product category and all related tariff rates and national tariff lines", null);
    }

    private ProductCategoriesDto convertToDto(ProductCategoriesEntity entity) {
        return new ProductCategoriesDto(entity.getId(), entity.getCategoryCode(), entity.getCategoryName(), entity.getDescription(), entity.getIsActive());
    }

    private ProductCategoriesEntity convertToEntity(CreateProductCategoriesDto dto) {
        return new ProductCategoriesEntity(dto.getCategoryCode(), dto.getCategoryName(), dto.getDescription(), dto.getIsActive());
    }
    
}
