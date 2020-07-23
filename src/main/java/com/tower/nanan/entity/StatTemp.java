package com.tower.nanan.entity;

import lombok.Data;

@Data
public class StatTemp {

    private String region;
    private String verifyCode;
    private String siteCode;
    private String payDate;
    private String customer;
    private Double taxMoney;

    public StatTemp(String region, String verifyCode, String payDate,String customer, String siteCode, Double taxMoney) {
        this.region = region;
        this.verifyCode = verifyCode;
        this.payDate = payDate;
        this.siteCode = siteCode;
        this.taxMoney = taxMoney;
        this.customer = customer;
    }
    


}
