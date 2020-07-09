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
    private String verifyMoney;
    private String rebackMoney;
    private String difference;
    private String statDate;
}
