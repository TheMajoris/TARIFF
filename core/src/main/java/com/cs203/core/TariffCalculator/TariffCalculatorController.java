package com.cs203.core.TariffCalculator;

import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/Calculator")
public class TariffCalculatorController {
    private final TariffCalculatorService tariffCalculatorService;

    public TariffCalculatorController(TariffCalculatorService tariffCalculatorService) {
        this.tariffCalculatorService = tariffCalculatorService;
    }

    //creating a new calculator object based on the input params, & setting the rates and finalPrice
    @GetMapping("/Calculator/{importingCountryId}/{exportingCountryId}/{hsCode}/{initialPrice}")
    public TariffCalculator calculator(
            @PathVariable Long importingCountryId,
            @PathVariable Long exportingCountryId,
            @PathVariable Integer hsCode,
            @PathVariable BigDecimal initialPrice) {

        TariffCalculator calculator = new TariffCalculator(null, null);
        tariffCalculatorService.setTariffRate(calculator, importingCountryId, exportingCountryId, hsCode);
        tariffCalculatorService.setFinalPrice(initialPrice, calculator);
        return calculator;
    }

}
