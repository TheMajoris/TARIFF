package com.cs203.core.dto.requests;

import com.cs203.core.entity.CountryEntity;
import com.cs203.core.entity.ProductCategoriesEntity;

        //req Dto for NationalTariffLines
public record NationalTariffLinesRequestDto(
        CountryEntity country,
        String tariffLineCode,
        String description,
        ProductCategoriesEntity parentHsCode,
        Integer level) { }
