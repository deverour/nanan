package com.tower.nanan.utils;

import com.tower.nanan.pojo.*;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyUtils {
    public static String getRealName(String name){
        int lastindex =name.lastIndexOf("\\");
        if(lastindex>0){

            return name.substring(lastindex+1);
        }else {
            return name;
        }
    }

    public static String to2Round(String numStr){
        DecimalFormat format = new DecimalFormat("#0.##");
        return format.format(new BigDecimal(numStr));
    }
    public static Double to2Round(Double numStr){
        DecimalFormat format = new DecimalFormat("#0.##");
        return NumberUtils.toDouble(format.format(numStr));
    }
    public static String to6Round(String numStr){
        DecimalFormat format = new DecimalFormat("#0.######");
        return format.format(new BigDecimal(numStr));
    }

    public static String percent(String numStr){
        double doubleNum = NumberUtils.toDouble(numStr)/100;
        String newNumStr = String.valueOf(doubleNum);


        return to6Round(newNumStr);
    }


    public static ArrayList<String> getList(Electric electric){
        ArrayList<String> list=new ArrayList<String>();
        list.add(electric.getId());
        list.add(electric.getRegion());
        list.add(electric.getSiteCode());
        list.add(electric.getAmmeterCode());
        list.add(electric.getDirectSupply());
        list.add(electric.getAccountCode());
        list.add(electric.getStartDegrees());
        list.add(electric.getEndDegrees());
        list.add(electric.getElectricQuantity());
        list.add(electric.getStartDate());
        list.add(electric.getEndDate());
        list.add(electric.getPayMoney());
        list.add(electric.getShareCustomer());
        list.add(electric.getProportion());
        list.add(electric.getSettlement());
        list.add(electric.getCustomer());
        list.add(electric.getVerifyCode());
        list.add(electric.getAccountPeriod());
        list.add(electric.getRebackCode());
        list.add(electric.getUploadDate());
        list.add(electric.getSettlementModel());
        return list;
    }
    public static ArrayList<String> getList(RebackStatWithCustomer rebackStatWithCustomer){
        ArrayList<String> list=new ArrayList<String>();
        list.add(rebackStatWithCustomer.getVerifyCode());
        list.add(rebackStatWithCustomer.getCustomer());
        list.add(rebackStatWithCustomer.getVerifyMoney());
        list.add(rebackStatWithCustomer.getRebackMoney());
        list.add(rebackStatWithCustomer.getStatDate());

        return list;
    }

    public static ArrayList<String> getList(Verify verify){
        ArrayList<String> list=new ArrayList<String>();
        list.add(verify.getRegion());
        list.add(verify.getVerifyCode());
        list.add(verify.getBillId());
        list.add(verify.getBillState());
        list.add(verify.getCustomer());
        list.add(verify.getTaxMoney());
        return list;
    }

    public static ArrayList<String> getList(Reback reback){
        ArrayList<String> list=new ArrayList<String>();
        list.add(reback.getRegion());
        list.add(reback.getAccountPeriod());
        list.add(reback.getCustomer());
        list.add(reback.getRebackCode());
        list.add(reback.getSettlement());
        list.add(reback.getRebackDate());
        list.add(reback.getRebacked());
        list.add(reback.getUploadDate());
        list.add(reback.getSettlement());

        return list;
    }

    public static ArrayList<String> getList(Percentage percentage){
        ArrayList<String> list=new ArrayList<String>();
        list.add(percentage.getSiteCode());
        list.add(percentage.getAmmeterCode());
        list.add(percentage.getNewProportion1());
        list.add(percentage.getNewProportion2());
        list.add(percentage.getNewProportion3());
      ;

        return list;
    }

    public static String getExcelDate(Date date){
        return FastDateFormat.getInstance("yyyy/MM/dd").format(date);
    }

    public static String getExcelDate(String dateStr){
        int dateInt = NumberUtils.toInt(dateStr)-1;
        Date date0 = new Date(Date.parse("1/1/1900"));
        Date newDate = addDate(date0, dateInt);
        return FastDateFormat.getInstance("yyyy/MM/dd").format(newDate);
    }

    public static Date addDate(Date date,long day) {
        long time = date.getTime(); // 得到指定日期的毫秒数
        day = day*24*60*60*1000; // 要加上的天数转换成毫秒数
        time+=day; // 相加得到新的毫秒数
        return new Date(time); // 将毫秒数转换成日期
    }



}
