package com.tower.nanan.pojo;


import lombok.Data;

import javax.persistence.Table;

@Data
@Table(name = "rebackstat_cpy")
public class RebackStatWithCpy {
    private String region;
    private String payDate;
    private String siteCode;
    private String siteName;
    private String verifyMoney;
    private String rebackMoney;
    private Double difference;
    private String statDate;
}
