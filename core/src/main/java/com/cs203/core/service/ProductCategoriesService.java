package com.cs203.core.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cs203.core.dto.CreateProductCategoriesDto;
import com.cs203.core.dto.GenericResponse;
import com.cs203.core.dto.ProductCategoriesDto;

public interface ProductCategoriesService {
    public List<ProductCategoriesDto> getProductCategories();

    public Page<ProductCategoriesDto> getProductCategories(Pageable pageable);

    public GenericResponse<ProductCategoriesDto> getProductCategoriesById(Long id);

    public GenericResponse<ProductCategoriesDto> createProductCategories(CreateProductCategoriesDto dto);

    public GenericResponse<ProductCategoriesDto> updateProductCategories(Long id, ProductCategoriesDto dto);

    public GenericResponse<Void> deleteProductCategories(Long id);

    public GenericResponse<Void> forceDeleteProductCategories(Long id);
}
