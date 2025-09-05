package com.cs203.core.TariffCalculator;


public class TariffCalculator {
    // takes in the ID from the database
    Double tariff;

    public TariffCalculator(String originCountry, String destinationCountry, Long itemId, Integer itemCount, Double itemValue){
        this.tariff = calculateTariff(originCountry, destinationCountry, itemId, itemCount, itemValue);
    }

    public Double calculateTariff(String originCountry, String destinationCountry, Long itemId, Integer itemCount, Double itemValue){
        //Temporary placeholder for method to obtain tariff between countries
        return 0.20;
    }

    public double finalPrice(Integer itemCount, Double itemValue){
        double initialPrice = itemValue * itemCount;
        return initialPrice + (initialPrice * tariff);
    }
}
