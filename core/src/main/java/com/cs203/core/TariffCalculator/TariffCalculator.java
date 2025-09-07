package com.cs203.core.TariffCalculator;

import java.math.BigDecimal;

public class TariffCalculator {
    // takes in the ID from the database
    private BigDecimal tariffRate;
    private BigDecimal finalPrice;

    public TariffCalculator(BigDecimal tariffRate, BigDecimal finalPrice) {
        this.tariffRate = tariffRate;
        this.finalPrice = finalPrice;
    }

    public void setTariffRate(BigDecimal Tariff) {
        this.tariffRate = Tariff;
    }

    public void setFinalPrice(BigDecimal finalPrice) {
        this.finalPrice = finalPrice;
    }

    public BigDecimal getTariffRate(){
        return tariffRate;
    }

    public BigDecimal getFinalPrice(){
        return finalPrice;
    }
}
