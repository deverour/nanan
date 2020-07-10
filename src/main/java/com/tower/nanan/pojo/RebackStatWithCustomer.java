package com.tower.nanan.pojo;


import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "rebackstat_customer")
public class RebackStatWithCustomer {
    private String region;
    private String verifyCode;
    private String payDate;
    private String customer;
    private String verifyMoney;
    private String rebackMoney;
    private Double difference;
    private String statDate;
}
