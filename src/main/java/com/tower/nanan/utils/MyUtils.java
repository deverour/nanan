package com.tower.nanan.utils;

import com.tower.nanan.pojo.*;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

    public static Double to2Double(String numStr){
        Double num = NumberUtils.toDouble(numStr);
        return to2Round(num);
    }
    public static Double to2Round(Double numStr){
        DecimalFormat format = new DecimalFormat("#0.##");
        return NumberUtils.toDouble(format.format(numStr));
    }
    public static String to4Round(String numStr){
        DecimalFormat format = new DecimalFormat("#0.####");
        return format.format(new BigDecimal(numStr));
    }
    public static String to0Round(Double numStr){
        DecimalFormat format = new DecimalFormat("#0.##");
        return String.valueOf(format.format(numStr));
    }
    public static String to6Round(String numStr){
        DecimalFormat format = new DecimalFormat("#0.######");
        return format.format(new BigDecimal(numStr));
    }

    public static String percent(String numStr){
        Double doubleNum = NumberUtils.toDouble(numStr)/100;
        String newNumStr = doubleNum.toString();


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
        list.add(electric.getSettlement().toString());
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
        list.add(rebackStatWithCustomer.getRegion());
        list.add(rebackStatWithCustomer.getVerifyCode());
        list.add(rebackStatWithCustomer.getPayDate());
        list.add(rebackStatWithCustomer.getCustomer());
        list.add(rebackStatWithCustomer.getVerifyMoney().toString());
        list.add(rebackStatWithCustomer.getRebackMoney().toString());
        list.add(rebackStatWithCustomer.getDifference().toString());
        list.add(rebackStatWithCustomer.getStatDate());
        return list;
    }

    public static ArrayList<String> getList(RebackStatWithSite rebackStatWithSite){
        ArrayList<String> list=new ArrayList<String>();
        list.add(rebackStatWithSite.getRegion());
        list.add(rebackStatWithSite.getVerifyCode());
        list.add(rebackStatWithSite.getPayDate());
        list.add(rebackStatWithSite.getSiteCode());
        list.add(rebackStatWithSite.getVerifyMoney().toString());
        list.add(rebackStatWithSite.getRebackMoney().toString());
        list.add(rebackStatWithSite.getDifference().toString());
        list.add(rebackStatWithSite.getStatDate());
        return list;
    }

    public static ArrayList<String> getList(RebackStatWithReport rebackStatWithReport){
        ArrayList<String> list=new ArrayList<String>();
        list.add(rebackStatWithReport.getRegion());
        list.add(rebackStatWithReport.getAccountPeriod());
        list.add(rebackStatWithReport.getCustomer());
        list.add(rebackStatWithReport.getVerifyMoney().toString());
        list.add(rebackStatWithReport.getRebackMoney().toString());
        list.add(rebackStatWithReport.getDifference().toString());
        list.add(rebackStatWithReport.getStatDate());
        return list;
    }

    public static List<String> getList(RebackStat rebackStat) {
        ArrayList<String> list=new ArrayList<String>();
        list.add(rebackStat.getRegion());
        list.add(rebackStat.getVerifyCode());
        list.add(rebackStat.getPayDate());
        list.add(rebackStat.getCustomer());
        list.add(rebackStat.getSiteCode());
        list.add(rebackStat.getVerifyMoney().toString());
        list.add(rebackStat.getRebackMoney().toString());
        list.add(rebackStat.getDifference().toString());
        list.add(rebackStat.getStatDate());
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
        list.add(reback.getSettlement().toString());
        list.add(reback.getRebackDate());
        list.add(reback.getRebacked());
        list.add(reback.getUploadDate());
        list.add(reback.getSettlementModel());

        return list;
    }

    public static ArrayList<String> getList(Percentage percentage){
        ArrayList<String> list=new ArrayList<String>();
        list.add(percentage.getSiteCode());
        list.add(percentage.getAmmeterCode());
        list.add(percentage.getLastDate1());
        list.add(percentage.getLastDate2());
        list.add(percentage.getLastDate3());
        list.add(percentage.getLastProportion1());
        list.add(percentage.getLastProportion2());
        list.add(percentage.getLastProportion3());
        list.add(percentage.getNewProportion1());
        list.add(percentage.getNewProportion2());
        list.add(percentage.getNewProportion3());


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

    public static String getaccountPeriodFromExcelDate(String dateStr){
        int dateInt = NumberUtils.toInt(dateStr)-2;
        Date date0 = new Date(Date.parse("1/1/1900"));
        Date newDate = addDate(date0, dateInt);
        return FastDateFormat.getInstance("yyyyMM").format(newDate);
    }

    public static String toExcelDate(String dateStr) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyy/MM/dd");
        Date endDate = formatter.parse(dateStr);
        Date startDate = formatter.parse("1900/1/1");
        int day = daysBetween(startDate, endDate)+2;
        return String.valueOf(day);

    }

    public static Date addDate(Date date,long day) {
        long time = date.getTime(); // 得到指定日期的毫秒数
        day = day*24*60*60*1000; // 要加上的天数转换成毫秒数
        time+=day; // 相加得到新的毫秒数
        return new Date(time); // 将毫秒数转换成日期
    }

    public static int daysBetween(Date date1,Date date2){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        long time1 = cal.getTimeInMillis();
        cal.setTime(date2);
        long time2 = cal.getTimeInMillis();
        long between_days=(time2-time1)/(1000*3600*24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    public static String getnowtime(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return formatter.format(new Date()) ;
    }



}
