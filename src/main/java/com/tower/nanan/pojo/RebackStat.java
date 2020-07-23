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
    private Double verifyMoney;
    private Double rebackMoney;
    private Double difference;
    private String statDate;
}
