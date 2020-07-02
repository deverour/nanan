package com.tower.nanan.pojo;


import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "rebackstat")
public class RebackStat {
    @Id
    private String verifyCode;
    private String verifyMoney;
    private String rebackMoney;
    private String statDate;
}
