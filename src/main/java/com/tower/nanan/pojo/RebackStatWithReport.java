package com.tower.nanan.pojo;


import lombok.Data;

import javax.persistence.Table;

@Data
@Table(name = "rebackstat_report")
public class RebackStatWithReport {
    private String region;
    private String accountPeriod;
    private String customer;
    private Double verifyMoney;
    private Double rebackMoney;
    private Double difference;
    private String statDate;
}
