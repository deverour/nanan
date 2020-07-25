package com.tower.nanan.entity;

import com.tower.nanan.pojo.Electric;
import com.tower.nanan.utils.MyUtils;

import java.util.ArrayList;
import java.util.List;

public class ExcelColumns {

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
    //public static final int INDEX_ELECTRIC_UPLOADDATE = 18;//上传日期


    public static final int INDEX_PERCENTAGE_SITECODE = 0;
    public static final int INDEX_PERCENTAGE_AMMETERCODE = 1;
    public static final int INDEX_PERCENTAGE_NEWPERPORTION1 = 2;
    public static final int INDEX_PERCENTAGE_NEWPERPORTION2 = 3;
    public static final int INDEX_PERCENTAGE_NEWPERPORTION3 =4;

    public static final int INDEX_CPY_REGION = 0;
    public static final int INDEX_CPY_SITECODE = 1;
    public static final int INDEX_CPY_NOTAXMONEY = 2;
    public static final int INDEX_CPY_ACCOUNTPERIOD = 3;

    public static final int INDEX_VERIFY_REGION = 0;
    public static final int INDEX_VERIFY_VERIFYCODE = 1;
    public static final int INDEX_VERIFY_BILLID = 2;
    public static final int INDEX_VERIFY_BILLSTATE = 3;
    public static final int INDEX_VERIFY_PAYDATE = 4;
    public static final int INDEX_VERIFY_SITECODE = 5;
    public static final int INDEX_VERIFY_CUSTOMER = 6;
    public static final int INDEX_VERIFY_TAXMONEY =7;




    public static Electric getElectric(List<String> list){
        Electric electric = new Electric();
        electric.setRegion(list.get(INDEX_ELECTRIC_REGION));
        electric.setSiteCode(list.get(INDEX_ELECTRIC_SITECODE));
        electric.setAmmeterCode(list.get(INDEX_ELECTRIC_AMMETERCODE));
        electric.setDirectSupply(list.get(INDEX_ELECTRIC_DIRECTSUPPLY));
        electric.setAccountCode(list.get(INDEX_ELECTRIC_ACCOUNTCODE));
        electric.setStartDegrees(list.get(INDEX_ELECTRIC_STARTDEGRESS));
        electric.setEndDegrees(list.get(INDEX_ELECTRIC_ENDDEGRESS));
        electric.setElectricQuantity(list.get(INDEX_ELECTRIC_ELECTRICQUANTITY));
        electric.setStartDate(list.get(INDEX_ELECTRIC_STARTDATE));
        electric.setEndDate(list.get(INDEX_ELECTRIC_ENDDATE));
        electric.setPayMoney(list.get(INDEX_ELECTRIC_PAYMONEY));
        electric.setShareCustomer(list.get(INDEX_ELECTRIC_SHARECUSTOMER));
        electric.setProportion(list.get(INDEX_ELECTRIC_PROPORTION));
        electric.setSettlement(MyUtils.to2Double(list.get(INDEX_ELECTRIC_SETTLEMENT)));
        electric.setCustomer(list.get(INDEX_ELECTRIC_CUSTOMER));
        electric.setVerifyCode(list.get(INDEX_ELECTRIC_VERIFYCODE));
        electric.setAccountPeriod(list.get(INDEX_ELECTRIC_ACCOUNTPERIOD));
        electric.setRebackCode(list.get(INDEX_ELECTRIC_REBACKCODE));

        return electric;
    }

    public static ArrayList<String> getElectricTitle() {
        ArrayList<String> namelist = new ArrayList<String>() ;
        namelist.add("电费编号(唯一值)");
        namelist.add("区域");
        namelist.add("站址编码");
        namelist.add("电表编码");
        namelist.add("是否直供电");
        namelist.add("户号");
        namelist.add("起度");
        namelist.add("止度");
        namelist.add("电量");
        namelist.add("始期");
        namelist.add("终期");
        namelist.add("垫资总额");
        namelist.add("共享运营商");
        namelist.add("分摊比例");
        namelist.add("结算金额");
        namelist.add("结算运营商");
        namelist.add("核销单号");
        namelist.add("账期");
        namelist.add("回款编号");
        namelist.add("上传日期");
        namelist.add("结算模式");
        return namelist;
    }




    public static ArrayList<String> getVerifyTitle() {
        ArrayList<String> namelist = new ArrayList<String>() ;
        namelist.add("区域");
        namelist.add("核销/支付单号");
        namelist.add("分摊编号");
        namelist.add("分摊状态");
        namelist.add("客户");
        namelist.add("分摊金额（含税）");
        return namelist;
    }

    public static ArrayList<String> getRebackTitle() {
        ArrayList<String> namelist = new ArrayList<String>() ;
        namelist.add("区域");
        namelist.add("账期");
        namelist.add("结算运营商");
        namelist.add("回款编号");
        namelist.add("结算金额");
        namelist.add("回款日期");
        namelist.add("是否回款");
        namelist.add("上传日期");
        namelist.add("结算模式");

        return namelist;
    }

    public static ArrayList<String> getPercentageTitle() {
        ArrayList<String> namelist = new ArrayList<String>() ;
        namelist.add("站址编码");
        namelist.add("电表编码");
        namelist.add("移动最后一次签认时间");
        namelist.add("联通最后一次签认时间");
        namelist.add("电信最后一次签认时间");
        namelist.add("移动最后一次签认比例");
        namelist.add("联通最后一次签认比例");
        namelist.add("电信最后一次签认比例");
        namelist.add("移动系统比例");
        namelist.add("联通系统比例");
        namelist.add("电信系统比例");

        return namelist;
    }

    public static ArrayList<String> getRebackStatWithCustomerTitle() {
        ArrayList<String> namelist = new ArrayList<String>() ;
        namelist.add("区域");
        namelist.add("核销单号");
        namelist.add("付款完成时间");
        namelist.add("客户");
        namelist.add("核销金额");
        namelist.add("回款金额");
        namelist.add("差异绝对值");
        namelist.add("统计时间");
        return namelist;
    }

    public static ArrayList<String> getRebackStatWithSiteTitle() {
        ArrayList<String> namelist = new ArrayList<String>() ;
        namelist.add("区域");
        namelist.add("核销单号");
        namelist.add("付款完成时间");
        namelist.add("站址编码");
        namelist.add("核销金额");
        namelist.add("回款金额");
        namelist.add("差异绝对值");
        namelist.add("统计时间");

        return namelist;

    }

    public static ArrayList<String> getRebackStatWithReportTitle() {
        ArrayList<String> namelist = new ArrayList<String>() ;
        namelist.add("区域");
        namelist.add("账期");
        namelist.add("客户");
        namelist.add("核销金额");
        namelist.add("回款金额");
        namelist.add("差异");
        namelist.add("统计时间");

        return namelist;
    }

    public static ArrayList<String> getRebackStatTitle() {
        ArrayList<String> namelist = new ArrayList<String>() ;
        namelist.add("区域");
        namelist.add("核销单号");
        namelist.add("付款完成时间");
        namelist.add("客户");
        namelist.add("站址编码");
        namelist.add("核销金额");
        namelist.add("回款金额");
        namelist.add("差异绝对值");
        namelist.add("统计时间");

        return namelist;
    }


}
