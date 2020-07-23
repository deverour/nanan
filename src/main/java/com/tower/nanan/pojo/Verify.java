package com.tower.nanan.pojo;

import lombok.Data;

import javax.persistence.Id;

@Data
public class Verify {
    private String region;
    private String verifyCode;
    private String payDate;
    private String siteCode;
    private String siteName;
    private String customer;
    @Id
    private String billId;
    private String billState;
    private String taxMoney;

}
