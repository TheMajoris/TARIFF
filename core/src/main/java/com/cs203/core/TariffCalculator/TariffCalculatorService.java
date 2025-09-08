package com.cs203.core.TariffCalculator;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.grammars.hql.HqlParser.TargetFieldsContext;
import org.springframework.stereotype.Service;

import com.cs203.core.entity.TariffRateEntity;
import com.cs203.core.repository.TariffRateRepository;

@Service
public class TariffCalculatorService {

    private final TariffRateRepository tariffRateRepository;;

        public TariffCalculatorService(TariffRateRepository tariffRateRepository) {
            this.tariffRateRepository = tariffRateRepository;
        }   

        //sets the tariffRate attribute in the TariffCalculator class based origin country, destination country & hscode
        public void setTariffRate(TariffCalculator tariffCalculator, Long importingCountryId, Long exportingCountryId, Integer hsCode){
            //get the tariff rate from the database using the repository, based on the importing country, exporting country, hs code and current date
            List<TariffRateEntity> tariffRates = tariffRateRepository.findByImportingCountryIdAndExportingCountryIdAndHsCode(importingCountryId, exportingCountryId, hsCode);
            //sort the tariff rates by lowest to highest
            tariffRates.sort((a, b) -> a.getTariffRate().compareTo(b.getTariffRate()));
            //get the lowest rate from the list
            BigDecimal tariffRate = tariffRates.get(0).getTariffRate();
            //set the tariffRate attribute in the TariffCalculator class
            tariffCalculator.setTariffRate(tariffRate);
        }

        public void setFinalPrice(BigDecimal initialPrice, TariffCalculator tariffCalculator){
            //calculate the final price based on the tariff rate and the price
            //obtain the value of the item and the number of items
            BigDecimal finalPrice = initialPrice.add(initialPrice.multiply(tariffCalculator.getTariffRate()));
            tariffCalculator.setFinalPrice(finalPrice);
        }


}
