package com.tower.nanan.pojo;


import lombok.Data;

import javax.persistence.Table;

@Data
@Table(name = "incomecpy")
public class IncomeCpy {

    private String Region;
    private String siteCode;
    private Double notaxMoney;
    private String accountPeriod;
    private String uploadDate;

}
