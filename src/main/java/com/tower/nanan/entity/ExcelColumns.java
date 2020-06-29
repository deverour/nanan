package com.tower.nanan.entity;

import com.tower.nanan.pojo.Electric;

import java.util.ArrayList;
import java.util.List;

public class ExcelColumns {
    //public static final int INDEX_ELECTRIC_BRANCH = 0;
    public static final int INDEX_ELECTRIC_REGION = 0;//区域
    public static final int INDEX_ELECTRIC_SITECODE = 1;//站点编码
    public static final int INDEX_ELECTRIC_AMMETERCODE = 2;//电表编码
    public static final int INDEX_ELECTRIC_DIRECTSUPPLY = 3;//是否直供电
    public static final int INDEX_ELECTRIC_ACCOUNTCODE = 4;//户号
    public static final int INDEX_ELECTRIC_STARTDEGRESS = 5;//起度
    public static final int INDEX_ELECTRIC_ENDDEGRESS = 6;//止度
    public static final int INDEX_ELECTRIC_ELECTRICQUANTITY = 7;//电量
    public static final int INDEX_ELECTRIC_STARTDATE = 8;//始期
    public static final int INDEX_ELECTRIC_ENDDATE = 9;//终期
    public static final int INDEX_ELECTRIC_PAYMONEY = 10;//垫资总额
    public static final int INDEX_ELECTRIC_SHARECUSTOMER = 11;//共享运营商
    public static final int INDEX_ELECTRIC_PROPORTION = 12;//分摊比例
    public static final int INDEX_ELECTRIC_SETTLEMENT = 13;//结算金额

    public static final int INDEX_ELECTRIC_CUSTOMER = 14;//结算运营商
    public static final int INDEX_ELECTRIC_VERIFYCODE = 15;//核销单号
    public static final int INDEX_ELECTRIC_ACCOUNTPERIOD =16;//账期
    public static final int INDEX_ELECTRIC_REBACKCODE = 17; //回款编号




    public static Electric getElectric(List<String> list){
        Electric electric = new Electric();
        electric.setVerifyCode(list.get(0));

        return electric;
    }

    public static ArrayList<String> getElectricTitle() {
        ArrayList<String> namelist = new ArrayList<String>() ;
        namelist.add("结算编号(唯一值)");
        namelist.add("区域");
        namelist.add("支付单号");
        namelist.add("站址编码");
        namelist.add("电表编码");
        namelist.add("电表倍率");
        namelist.add("是否直供电");
        namelist.add("户号");
        namelist.add("始期");
        namelist.add("终期");
        namelist.add("起度");
        namelist.add("止度");
        namelist.add("电损");
        namelist.add("电量");
        namelist.add("垫资总额");
        namelist.add("共享运营商");
        namelist.add("分摊比例");
        namelist.add("结算金额");
        namelist.add("账期");
        namelist.add("结算运营商");
        namelist.add("制表时间");
        namelist.add("回款编号");
        namelist.add("上传日期");
        return namelist;
    }


}
