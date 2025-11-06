package com.cs203.core.service.impl;

import com.cs203.core.dto.CreateProductCategoriesDto;
import com.cs203.core.dto.GenericResponse;
import com.cs203.core.dto.ProductCategoriesDto;
import com.cs203.core.entity.ProductCategoriesEntity;
import com.cs203.core.repository.NationalTariffLinesRepository;
import com.cs203.core.repository.ProductCategoriesRepository;
import com.cs203.core.repository.TariffRateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductCategoriesServiceImplTest {

    @InjectMocks
    private ProductCategoriesServiceImpl service;

    @Mock
    private ProductCategoriesRepository productCategoriesRepository;
    @Mock
    private TariffRateRepository tariffRateRepository;
    @Mock
    private NationalTariffLinesRepository nationalTariffLinesRepository;

    private ProductCategoriesEntity electronics;
    private ProductCategoriesDto electronicsDto1;
    private CreateProductCategoriesDto createDto1;

    @BeforeEach
    void setUp() {
        electronics = new ProductCategoriesEntity(850110, "Electronic Equipment", "Consumer electronics");
        electronics.setId(1L);
        electronics.setIsActive(true);

        electronicsDto1 = new ProductCategoriesDto(1L, 850110, "Electronic Equipment", "Consumer electronics", true);

        createDto1 = new CreateProductCategoriesDto();
        createDto1.setCategoryCode(850110);
        createDto1.setDescription("Consumer electronics");
        createDto1.setCategoryName("Electronic Equipment");
        createDto1.setIsActive(true);

    }

    @Test
    void testGetProductCategories() {
        when(productCategoriesRepository.findAll(Sort.by("categoryCode").ascending()))
                .thenReturn(List.of(electronics));

        List<ProductCategoriesDto> result = service.getProductCategories();

        assertEquals(1, result.size());
        assertEquals(850110, result.get(0).getCategoryCode());
    }

    @Test
    void testGetProductCategoriesWithPageable() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductCategoriesEntity> page = new PageImpl<>(List.of(electronics));
        when(productCategoriesRepository.findAll(pageable)).thenReturn(page);

        Page<ProductCategoriesDto> result = service.getProductCategories(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(850110, result.getContent().get(0).getCategoryCode());
    }

    @Test
    void testGetProductCategoriesById_NotFound() {
        when(productCategoriesRepository.findById(1L)).thenReturn(Optional.empty());

        GenericResponse<ProductCategoriesDto> res = service.getProductCategoriesById(1L);

        assertEquals(HttpStatus.NOT_FOUND, res.getStatus());
        assertNull(res.getData());
    }

    @Test
    void testGetProductCategoriesById_Found() {
        when(productCategoriesRepository.findById(1L)).thenReturn(Optional.of(electronics));

        GenericResponse<ProductCategoriesDto> res = service.getProductCategoriesById(1L);

        assertEquals(HttpStatus.OK, res.getStatus());
        assertEquals(850110, res.getData().getCategoryCode());
    }

    @Test
    void testCreateProductCategories_AlreadyExists() {
        when(productCategoriesRepository.existsByCategoryCode(850110)).thenReturn(true);

        GenericResponse<ProductCategoriesDto> res = service.createProductCategories(createDto1);

        assertEquals(HttpStatus.CONFLICT, res.getStatus());
        assertEquals(850110, res.getData().getCategoryCode());
    }

    @Test
    void testCreateProductCategories_Success() {
        when(productCategoriesRepository.existsByCategoryCode(850110)).thenReturn(false);
        when(productCategoriesRepository.save(any())).thenReturn(electronics);

        GenericResponse<ProductCategoriesDto> res = service.createProductCategories(createDto1);

        assertEquals(HttpStatus.CREATED, res.getStatus());
        assertEquals(850110, res.getData().getCategoryCode());
    }

    @Test
    void testUpdateProductCategories_NotFound() {
        when(productCategoriesRepository.findById(1L)).thenReturn(Optional.empty());

        GenericResponse<ProductCategoriesDto> res = service.updateProductCategories(1L, electronicsDto1);

        assertEquals(HttpStatus.NOT_FOUND, res.getStatus());
    }

    @Test
    void testUpdateProductCategories_Conflict() {
        when(productCategoriesRepository.findById(1L)).thenReturn(Optional.of(electronics));
        when(productCategoriesRepository.existsByCategoryCodeAndIdNot(850110, 1L)).thenReturn(true);

        GenericResponse<ProductCategoriesDto> res = service.updateProductCategories(1L, electronicsDto1);

        assertEquals(HttpStatus.CONFLICT, res.getStatus());
    }

    @Test
    void testUpdateProductCategories_Success() {
        when(productCategoriesRepository.findById(1L)).thenReturn(Optional.of(electronics));
        when(productCategoriesRepository.existsByCategoryCodeAndIdNot(850110, 1L)).thenReturn(false);
        when(productCategoriesRepository.save(any())).thenReturn(electronics);

        GenericResponse<ProductCategoriesDto> res = service.updateProductCategories(1L, electronicsDto1);

        assertEquals(HttpStatus.OK, res.getStatus());
        assertEquals(850110, res.getData().getCategoryCode());
    }

    @Test
    void testDeleteProductCategories_NotFound() {
        when(productCategoriesRepository.findById(1L)).thenReturn(Optional.empty());

        GenericResponse<Void> res = service.deleteProductCategories(1L);

        assertEquals(HttpStatus.NOT_FOUND, res.getStatus());
    }

    @Test
    void testDeleteProductCategories_HasRelations() {
        when(productCategoriesRepository.findById(1L)).thenReturn(Optional.of(electronics));
        when(tariffRateRepository.countByHsCode(850110)).thenReturn(1L);
        when(nationalTariffLinesRepository.countByHsCode(850110)).thenReturn(2L);

        GenericResponse<Void> res = service.deleteProductCategories(1L);

        assertEquals(HttpStatus.CONFLICT, res.getStatus());
    }

    @Test
    void testDeleteProductCategories_Success() {
        when(productCategoriesRepository.findById(1L)).thenReturn(Optional.of(electronics));
        when(tariffRateRepository.countByHsCode(850110)).thenReturn(0L);
        when(nationalTariffLinesRepository.countByHsCode(850110)).thenReturn(0L);

        GenericResponse<Void> res = service.deleteProductCategories(1L);

        assertEquals(HttpStatus.OK, res.getStatus());
        verify(productCategoriesRepository).deleteById(1L);
    }


    @Test
    void testForceDeleteProductCategories_NotFound() {
        when(productCategoriesRepository.findById(1L)).thenReturn(Optional.empty());

        GenericResponse<Void> res = service.forceDeleteProductCategories(1L);

        assertEquals(HttpStatus.NOT_FOUND, res.getStatus());
    }

    @Test
    void testForceDeleteProductCategories_Success() {
        when(productCategoriesRepository.findById(1L)).thenReturn(Optional.of(electronics));

        GenericResponse<Void> res = service.forceDeleteProductCategories(1L);

        assertEquals(HttpStatus.OK, res.getStatus());
        verify(productCategoriesRepository).deleteById(1L);
    }

}