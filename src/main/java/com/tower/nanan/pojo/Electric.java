package com.tower.nanan.pojo;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Electric {

    private Integer id;
    private String verifyCode;
    private String branch;
    private String region;
    private String siteCode;
    private String ammeterCode;
    private String directSupply;
    private String accountCode;
    private String startDegrees;//起度
    private String endDegrees;//止度
    private String electricQuantity;//电量
    private String unitPrice;//单价
    private String startDate;//始期
    private String endDate;//终期
    private String payMoney;//垫资总额
    private String shareCustomer;//共享运营商
    private String proportion;//分摊比例
    private String customer;//结算运营商
    private String rebackCode;//回款编号
    private String settlement;//结算金额


}
