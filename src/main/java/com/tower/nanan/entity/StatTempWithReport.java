package com.tower.nanan.entity;

import lombok.Data;

@Data
public class StatTempWithReport {

    private String region;
    private String accountPeriod;
    private String customer;
    private Double taxMoney;

    public StatTempWithReport(String region, String accountPeriod, String customer, Double taxMoney) {
        this.region = region;
        this.accountPeriod = accountPeriod;
        this.customer = customer;
        this.taxMoney = taxMoney;
    }



}
