package com.tower.nanan.utils;

import com.tower.nanan.entity.ErrorMessage;
import com.tower.nanan.pojo.Electric;
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
        list.add(electric.getRebackCode());
        list.add(electric.getCustomer());
        list.add(electric.getVerifyCode());
        list.add(electric.getAccountPeriod());
        return list;
    }

    public static String getExcelDate(Date date){
        return FastDateFormat.getInstance("yyyy/MM/dd").format(date);
    }



}
