package com.tower.nanan.pojo;


import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "rebackstat_site")
public class RebackStatWithSite {
    private String region;
    private String verifyCode;
    private String payDate;
    private String siteCode;
    private String siteName;
    private Double verifyMoney;
    private Double rebackMoney;
    private Double difference;
    private String statDate;
}
