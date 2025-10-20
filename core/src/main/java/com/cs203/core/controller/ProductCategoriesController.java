package com.cs203.core.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cs203.core.dto.CreateProductCategoriesDto;
import com.cs203.core.dto.GenericResponse;
import com.cs203.core.dto.ProductCategoriesDto;
import com.cs203.core.service.ProductCategoriesService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Product Categories")
@RestController
@RequestMapping("/api/v1/product-categories")
public class ProductCategoriesController {
    @Autowired
    ProductCategoriesService productCategoriesService;

    @Operation(summary = "Get all product categories")
    @ApiResponse(responseCode = "200", description = "Product categories successfuly retrieved.")
    @GetMapping("/all")
    public ResponseEntity<List<ProductCategoriesDto>> getAllProductCategories() {
        return ResponseEntity.ok(productCategoriesService.getProductCategories());
    }

    @Operation(summary = "Get product categories with page and limit")
    @ApiResponse(responseCode = "200", description = "Product categories successfuly retrieved.")
    @GetMapping
    public ResponseEntity<Page<ProductCategoriesDto>> getProductCategories(
            @RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "40") Integer size) {
        Pageable pageable = PageRequest.of(pageNo, size);
        return ResponseEntity.ok(productCategoriesService.getProductCategories(pageable));
    }

    @Operation(summary = "Get product categories by id")
    @ApiResponse(responseCode = "200", description = "Product category successfully retrieved")
    @ApiResponse(responseCode = "404", description = "Product category not found with id")
    @GetMapping("/{productCategoryId}")
    public ResponseEntity<GenericResponse<ProductCategoriesDto>> getTariffRateById(
            @PathVariable Long productCategoryId) {
        GenericResponse<ProductCategoriesDto> response = productCategoriesService
                .getProductCategoriesById(productCategoryId);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @Operation(summary = "Create a new product category with the response body")
    @ApiResponse(responseCode = "201", description = "Product category created")
    @ApiResponse(responseCode = "400", description = "Data is invalid")
    @ApiResponse(responseCode = "400", description = "Category code (HSCode) already exists")
    @PostMapping
    public ResponseEntity<GenericResponse<ProductCategoriesDto>> createProductCategories(
            @Valid @RequestBody CreateProductCategoriesDto createProductCategoriesDto) {
        GenericResponse<ProductCategoriesDto> response = productCategoriesService
                .createProductCategories(createProductCategoriesDto);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @Operation(summary = "Update product category by id")
    @ApiResponse(responseCode = "200", description = "Successfully updated Product category")
    @ApiResponse(responseCode = "400", description = "Data is invalid")
    @ApiResponse(responseCode = "404", description = "Product category not found with id")
    @PutMapping("/{productCategoryId}")
    public ResponseEntity<GenericResponse<ProductCategoriesDto>> updateProductCategories(
            @Valid @RequestBody ProductCategoriesDto productCategoriesDto, @PathVariable Long productCategoryId) {
        GenericResponse<ProductCategoriesDto> response = productCategoriesService.updateProductCategories(
                productCategoryId,
                productCategoriesDto);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @Operation(summary = "Delete product category by id if it has no relations. Otherwise, requires a flag to cascade delete")
    @ApiResponse(responseCode = "200", description = "Successfully deleted Product category")
    @ApiResponse(responseCode = "200", description = "Successfully deleted Product category and all related tariff rates and national tariff lines")
    @ApiResponse(responseCode = "404", description = "Product category not found with id")
    @ApiResponse(responseCode = "409", description = "Product categories has ? related tariff rates. Set request params flag to true to cascade delete")
    @DeleteMapping("/{productCategoryId}")
    public ResponseEntity<GenericResponse<Void>> deleteProductCategories(@PathVariable Long productCategoryId,
            @RequestParam(required = false, defaultValue = "false") Boolean forceDelete) {
        GenericResponse<Void> response;
        if (forceDelete) {
            response = productCategoriesService.forceDeleteProductCategories(productCategoryId);
        } else {
            response = productCategoriesService.deleteProductCategories(productCategoryId);
        }
        return new ResponseEntity<>(response, response.getStatus());
    }

}
