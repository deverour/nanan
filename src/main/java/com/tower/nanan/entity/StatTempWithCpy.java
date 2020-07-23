package com.tower.nanan.entity;

import lombok.Data;

@Data
public class StatTempWithCpy {

    private String region;
    private String accountPeriod;
    private Double notaxMoney;


    public StatTempWithCpy(String region, String accountPeriod, Double notaxMoney) {
        this.region = region;
        this.accountPeriod = accountPeriod;
        this.notaxMoney = notaxMoney;
    }
}
