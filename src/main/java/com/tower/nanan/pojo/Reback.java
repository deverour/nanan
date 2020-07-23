package com.tower.nanan.pojo;


import lombok.Data;

import javax.persistence.Id;

@Data
public class Reback {
    @Id
    private Integer id;
    private String region;//区域
    private String accountPeriod;//账期
    private String customer;//结算运营商
    private String settlementModel;//结算模式
    private String rebackCode;//回款编号
    private Double settlement;//结算金额
    private String rebacked;//是否回款
    private String uploadDate;//上传日期
    private String rebackDate;//回款日期


}
