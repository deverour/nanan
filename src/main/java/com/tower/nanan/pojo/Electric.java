package com.tower.nanan.pojo;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Id;

@Getter
@Setter
@ToString
public class Electric {

    @Id
    private String id;
    private String region;//区域
    private String siteCode;//站点编码
    private String ammeterCode;//电表编码
    private String directSupply;//是否直供电
    private String accountCode;//户号
    private String startDegrees;//起度
    private String endDegrees;//止度
    private String electricQuantity;//电量
    private String startDate;//始期
    private String endDate;//终期
    private String payMoney;//垫资总额
    private String shareCustomer;//共享运营商
    private String proportion;//分摊比例
    private Double settlement;//结算金额
    private String customer;//结算运营商
    private String verifyCode;//核销单号
    private String accountPeriod;//账期
    private String rebackCode;//回款编号

    private String uploadDate;//上传日期
    private String settlementModel;//结算模式



}
