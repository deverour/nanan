package com.tower.nanan.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RebackStatQueryBean implements Serializable {

    private List<String> regions;
    private List<String> customers;
    private String siteCode;
    private String verifyCode;
    private String startPayDate;
    private String endPayDate;
    private String difference;



}
