package com.cs203.core.TariffCalculator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/tariff-rate/calculate")
public class TariffCalculatorController {
    @Autowired
    private TariffCalculatorService tariffCalculatorService;



    //using the params from request DTO, create final price n return response as DTO
    @PostMapping
    public ResponseEntity<TariffCalculatorResponseDto> getTariffCalculation(@RequestBody TariffCalculatorRequestDto requestBodyDTO) {

        BigDecimal finalPrice = tariffCalculatorService.getFinalPrice(
                requestBodyDTO.importingCountryId(),
                requestBodyDTO.exportingCountryId(),
                requestBodyDTO.hsCode(),
                requestBodyDTO.initialPrice()
        );
        return ResponseEntity.ok(new TariffCalculatorResponseDto(finalPrice));
    }

}
