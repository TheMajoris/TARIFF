package com.cs203.core.TariffCalculator;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs203.core.entity.TariffRateEntity;
import com.cs203.core.repository.TariffRateRepository;

@Service
public class TariffCalculatorService {
    // changed to Autowired & removed constructor
    @Autowired
    private TariffRateRepository tariffRateRepository;

    // get tariffRate
    public BigDecimal getFinalPrice(Long importingCountryId, Long exportingCountryId, Integer hsCode, BigDecimal initialPrice) {
        // get List of rates based on input and attributed data in repo
        List<TariffRateEntity> tariffRates = tariffRateRepository
                .findByImportingCountryIdAndExportingCountryIdAndHsCode(importingCountryId, exportingCountryId, hsCode);
        // get n set lowest rate
        BigDecimal tariffRate = tariffRates
                                .stream()
                                .map(TariffRateEntity::getTariffRate)
                                .min(Comparable::compareTo)
                                .orElse(BigDecimal.ZERO);
        BigDecimal finalPrice = initialPrice.add(initialPrice.multiply(tariffRate));
        return finalPrice;
    }

    

}
