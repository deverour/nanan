package com.tower.nanan.pojo;


import lombok.Data;

import javax.persistence.Table;

@Data
@Table(name = "rebackstat")
public class RebackStat {
    private String region;
    private String verifyCode;
    private String payDate;
    private String siteCode;
    private String siteName;
    private String customer;
    private String verifyMoney;
    private String rebackMoney;
    private Double difference;
    private String statDate;
}
