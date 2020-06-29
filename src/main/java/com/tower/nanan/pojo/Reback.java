package com.tower.nanan.pojo;


import lombok.Data;

@Data
public class Reback {
    private String region;//区域
    private String accountPeriod;//账期
    private String customer;//结算运营商
    private String settlementModel;//结算模式
    private String rebackCode;//回款编号
    private String settlement;//结算金额
    private String rebackDate;//回款日期
    private String uploadDate;//上传日期

}
