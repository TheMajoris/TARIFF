package com.cs203.core.TariffCalculator;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class TariffCalculatorController {
    @GetMapping("/Calculator")
    
    // public TariffCalculator calculator(Long importingCountryId, Long exportingCountryId, Integer hsCode, Integer itemCount,
    // Double itemValue) {
    //     return new TariffCalculator(importingCountryId, exportingCountryId, hsCode, itemCount,
    //             itemValue);
    // }

}
