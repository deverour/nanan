package com.tower.nanan.pojo;

import com.tower.nanan.entity.ExcelColumns;
import com.tower.nanan.entity.Group;
import com.tower.nanan.entity.Result;
import com.tower.nanan.utils.MyUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class LogicCheck {

    public static Result electricCheck(List<List<String>> electrics,User user,HashSet<String> verifyCodeSet){
        HashMap<String,String> map = new HashMap();
        String message = "";
        String onlyRebackCode = "";
        HashSet<String> keySet = new HashSet();
        double total=0.0;

        if (electrics.get(0).size() < ExcelColumns.INDEX_ELECTRIC_REBACKCODE+1){
            message = message + "第【"+(electrics.get(0).size()+1)+"】列不能为空\n";
            return new Result(false,message);
        }

        onlyRebackCode = electrics.get(0).get(ExcelColumns.INDEX_ELECTRIC_REBACKCODE);
        int col=2;
        for (List<String> electric : electrics) {
            //key
            String key =electric.get(ExcelColumns.INDEX_ELECTRIC_SITECODE)+electric.get(ExcelColumns.INDEX_ELECTRIC_AMMETERCODE)+electric.get(ExcelColumns.INDEX_ELECTRIC_ENDDATE);
            if (keySet.contains(key)){
                message = message + "请以电费签认表表二为数据来源制表，以免后期取数导致电量翻倍（电费签认表表一为系统分摊出账，直供电按票据数量会出两条电量一样的明细），目前三家运营商电费签认表均有新模板（类似电信）\n";
            }else {
                keySet.add(key);
            }

            //核销单号verifyCode
            String verifyCode = electric.get(ExcelColumns.INDEX_ELECTRIC_VERIFYCODE);
            if (!verifyCodeSet.contains(verifyCode)){
                message=message+ "【核销单号】不存在\n";
            }

            //区域region
            String region = electric.get(ExcelColumns.INDEX_ELECTRIC_REGION);
            if (!Group.regionSet.contains(region)){
                message=message+"【区域错误】,请参导入模板表二限定字段\n";
            }else if (user.getRegion() != region){
                message=message+"【区域错误】,本账号没有录入【"+region+"】区域的权限\n";
            }

            //站址编码siteCode
            String siteCode = electric.get(ExcelColumns.INDEX_ELECTRIC_SITECODE);
            if (!NumberUtils.isNumber(siteCode)){
                message=message+"【站址编码】错误,请检查是否有空格或非数字\n";
            }else if (siteCode.contains(".")){
                message=message+"【站址编码】错误,请检查是否有空格或非数字\n";
            }

            //电表编码ammeterCode
            String ammeterCode=electric.get(ExcelColumns.INDEX_ELECTRIC_AMMETERCODE);
            if (ammeterCode==null){
                message=message+ "【电表编码】不能为空\n";
            }

            //是否为直供电directSupply
            boolean iszhigong=false;
            String directSupply=electric.get(ExcelColumns.INDEX_ELECTRIC_DIRECTSUPPLY);
            if (directSupply==null){

                message=message+ "【是否直供电】不能为空\n";

            }else if (directSupply.equals("是")){
                iszhigong=true;
            }else if (!directSupply.equals("否") ){
                message=message+"【是否为直供电】错误,只能为'是'或者'否'\n";
            }

            //户号
            String accountCode = electric.get(ExcelColumns.INDEX_ELECTRIC_ACCOUNTCODE);
            if (iszhigong ){

                if (accountCode==null){
                    message=message+"【户号错误】,直供电户号不能为空\n";

                }else if (!NumberUtils.isNumber(accountCode)){
                    message=message+"【户号错误】,请检查是否有空格或非数字\n";
                }
                else if (accountCode.contains(".")){
                    message=message+"【户号错误】,请检查是否有空格或非数字\n";
                }else if(accountCode.length()>10){
                    message=message+"【户号错误】,直供电户号不能大于10位，转供电户号为空\n";
                }
            }

            //始期、终期
            String startDate=electric.get(ExcelColumns.INDEX_ELECTRIC_STARTDATE);
            String endDate=electric.get(ExcelColumns.INDEX_ELECTRIC_ENDDATE);
            if (!NumberUtils.isNumber(startDate) ||!NumberUtils.isNumber(endDate)){

                message=message+"【起止时间】错误,请检查是否为时间格式(筛选时，为可缩进状态)\n";
            }else if(startDate.contains(".")|| endDate.contains(".")){
                message=message+"【起止时间】错误,请检查是否为时间格式(筛选时，为可缩进状态)\n";
            }else if (Integer.parseInt(startDate)<40179 ||  Integer.parseInt(startDate)>47483){
                message=message+"【始期】错误】,时间范围异常，请检查时间值是否在正确的范围\n";
            }else if (Integer.parseInt(endDate)<40179 ||  Integer.parseInt(endDate)>47483){
                message=message+"【终期】错误】,时间范围异常，请检查时间值是否在正确的范围\n";
            }else if (Integer.parseInt(startDate)>Integer.parseInt(endDate)){
                message=message+"【始期】不应大于【终期】\n";
            }

            //起度、止度、电损、电量、垫资总额、
            if (!NumberUtils.isNumber(electric.get(ExcelColumns.INDEX_ELECTRIC_STARTDEGRESS))){
                message=message+"【起度】错误,请检查是否有空格或非数字\n";
            }
            if (!NumberUtils.isNumber(electric.get(ExcelColumns.INDEX_ELECTRIC_ENDDEGRESS))){
                message=message+"【止度】错误,请检查是否有空格或非数字\n";
            }
            if (!NumberUtils.isNumber(electric.get(ExcelColumns.INDEX_ELECTRIC_ELECTRICQUANTITY))){
                message=message+"【电量】错误,请检查是否有空格或非数字\n";
            }
            if (!NumberUtils.isNumber(electric.get(ExcelColumns.INDEX_ELECTRIC_PAYMONEY))){
                message=message+"【垫资总额】错误,请检查是否有空格或非数字\n";
            }

            //共享运营商
            if (!Group.ShareCustomerSet.contains(electric.get(ExcelColumns.INDEX_ELECTRIC_SHARECUSTOMER))){
                message=message+"【共享运营商】错误,请参导入模板表二限定字段\n";
            }

            //分摊比例
            String proportion=electric.get(ExcelColumns.INDEX_ELECTRIC_PROPORTION);
            if (!NumberUtils.isNumber(proportion)){
                message=message+"【分摊比例】错误,请检查是否大于0且小于等于100%\n";
            }else if (Double.parseDouble(proportion)<=0 && Double.parseDouble(proportion)>1){
                message=message+"【分摊比例】错误,请检查是否大于0且小于等于100%\n";
            }

            //结算金额
            String settlement=electric.get(ExcelColumns.INDEX_ELECTRIC_SETTLEMENT);
            if (!NumberUtils.isNumber(settlement)){
                message=message+"【结算金额】错误,请检查是否有空格或非数字\n";
            }else {

                total=total+NumberUtils.toDouble(MyUtils.to2Round(settlement));
            }


        }











        return null;
    }
}
