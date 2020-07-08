package com.tower.nanan.entity;

import lombok.Data;

@Data
public class StatTempWithCustomer {

    private String region;
    private String verifyCode;
    private String payDate;
    private String customer;
    private Double taxMoney;

    public StatTempWithCustomer(String region, String verifyCode, String payDate, String customer, Double taxMoney) {
        this.region = region;
        this.verifyCode = verifyCode;
        this.payDate = payDate;
        this.customer = customer;
        this.taxMoney = taxMoney;
    }



}
