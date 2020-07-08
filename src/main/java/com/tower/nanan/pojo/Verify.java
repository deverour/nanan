package com.tower.nanan.pojo;

import lombok.Data;

@Data
public class Verify {
    private String region;
    private String verifyCode;
    private String payDate;
    private String siteCode;
    private String customer;
    private String billId;
    private String billState;
    private String taxMoney;

}
