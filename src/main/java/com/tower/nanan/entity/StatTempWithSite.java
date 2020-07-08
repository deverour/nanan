package com.tower.nanan.entity;

import lombok.Data;

@Data
public class StatTempWithSite {

    private String region;
    private String verifyCode;
    private String siteCode;
    private String payDate;
    private Double taxMoney;

    public StatTempWithSite(String region, String verifyCode, String payDate, String siteCode, Double taxMoney) {
        this.region = region;
        this.verifyCode = verifyCode;
        this.payDate = payDate;
        this.siteCode = siteCode;
        this.taxMoney = taxMoney;
    }
    


}
