package com.tower.nanan.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ElectricQueryBean implements Serializable {
    private Integer currentPage;//页码
    private Integer pageSize;//每页记录数

    private List<String> regions;
    private List<String> customers;
    private String verifyCode;
    private String startAccountPeriod;
    private String endAccountPeriod;



}
