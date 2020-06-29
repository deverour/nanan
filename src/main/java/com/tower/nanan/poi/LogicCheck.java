package com.tower.nanan.poi;

import com.tower.nanan.entity.ErrorMessage;
import com.tower.nanan.entity.ExcelColumns;
import com.tower.nanan.entity.Group;
import com.tower.nanan.entity.Result;
import com.tower.nanan.pojo.User;
import com.tower.nanan.utils.MyUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LogicCheck {

    public static Result electricCheck(List<List<String>> electrics, User user, Set<String> rebackCodeSet,Set<String> verifyCodeSet){
        ErrorMessage errorMessage = new ErrorMessage(new StringBuilder(),1);
        
        HashMap<String,String> map = new HashMap();
        
        String soleRebackCode = "";
        String soleCustomer = "";
        HashSet<String> keySet = new HashSet();
        double total=0.0;
        boolean flag = true;
        int messageIndex = 1;

        System.out.println("electrics.get(0).size()>>>"+electrics.get(0).size());
        if (electrics.get(0).size() < ExcelColumns.INDEX_ELECTRIC_REBACKCODE+1){
            errorMessage.addMessage("第【"+(electrics.get(0).size()+1)+"】列不能为空\n");
            flag=false;
            return new Result(flag,errorMessage.getMessage().toString());
        }

        soleRebackCode = electrics.get(0).get(ExcelColumns.INDEX_ELECTRIC_REBACKCODE);
        soleCustomer = electrics.get(0).get(ExcelColumns.INDEX_ELECTRIC_CUSTOMER);
        int col=2;
        for (List<String> electric : electrics) {
            //key
            String key =electric.get(ExcelColumns.INDEX_ELECTRIC_SITECODE)+electric.get(ExcelColumns.INDEX_ELECTRIC_AMMETERCODE)+electric.get(ExcelColumns.INDEX_ELECTRIC_ENDDATE);
            if (keySet.contains(key)){
                flag=false;
                errorMessage.addMessage("请以电费签认表表二为数据来源制表，以免后期取数导致电量翻倍（电费签认表表一为系统分摊出账，直供电按票据数量会出两条电量一样的明细），目前三家运营商电费签认表均有新模板（类似电信）");
            }else {
                keySet.add(key);
            }

            //核销单号verifyCode
            String verifyCode = electric.get(ExcelColumns.INDEX_ELECTRIC_VERIFYCODE);
            if (!verifyCodeSet.contains(verifyCode)){
                flag=false;
                errorMessage.addMessage("【核销单号】不存在     ");
            }

            //区域region
            String region = electric.get(ExcelColumns.INDEX_ELECTRIC_REGION);
            if (!Group.regionSet.contains(region)){
                flag=false;
                errorMessage.addMessage("【区域】错误,请参导入模板表二限定字段");
            }else if (!user.getRegion().equals(region)){
                flag=false;
                errorMessage.addMessage("【区域】错误,本账号没有录入【"+region+"】区域的权限");
            }

            //站址编码siteCode
            String siteCode = electric.get(ExcelColumns.INDEX_ELECTRIC_SITECODE);
            if (!NumberUtils.isNumber(siteCode)){
                flag=false;
                errorMessage.addMessage("【站址编码】错误,请检查是否有空格或非数字");
            }else if (siteCode.contains(".")){
                flag=false;
                errorMessage.addMessage("【站址编码】错误,请检查是否有空格或非数字");
            }

            //电表编码ammeterCode
            String ammeterCode=electric.get(ExcelColumns.INDEX_ELECTRIC_AMMETERCODE);
            if (ammeterCode==null){
                flag=false;
                errorMessage.addMessage("【电表编码】不能为空");
            }

            //是否为直供电directSupply
            boolean iszhigong=false;
            String directSupply=electric.get(ExcelColumns.INDEX_ELECTRIC_DIRECTSUPPLY);
            if (directSupply==null){
                flag=false;
                errorMessage.addMessage("【是否直供电】不能为空");

            }else if (directSupply.equals("是")){
                iszhigong=true;
            }else if (!directSupply.equals("否") ){
                flag=false;
                errorMessage.addMessage("【是否为直供电】错误,只能为'是'或者'否'");
            }

            //户号
            String accountCode = electric.get(ExcelColumns.INDEX_ELECTRIC_ACCOUNTCODE);
            if (iszhigong ){

                if (accountCode==null){
                    flag=false;
                    errorMessage.addMessage("【户号】错误,直供电户号不能为空");

                }else if (!NumberUtils.isNumber(accountCode)){
                    flag=false;
                    errorMessage.addMessage("【户号】错误,请检查是否有空格或非数字");
                }
                else if (accountCode.contains(".")){
                    flag=false;
                    errorMessage.addMessage("【户号】错误,请检查是否有空格或非数字");
                }else if(accountCode.length()>10){
                    flag=false;
                    errorMessage.addMessage("【户号】错误,直供电户号不能大于10位，转供电户号为空");
                }
            }

            //始期、终期
            String startDate=electric.get(ExcelColumns.INDEX_ELECTRIC_STARTDATE);
            String endDate=electric.get(ExcelColumns.INDEX_ELECTRIC_ENDDATE);
            if (!NumberUtils.isNumber(startDate) ||!NumberUtils.isNumber(endDate)){
                flag=false;
                errorMessage.addMessage("【起止时间】错误,请检查是否为时间格式(筛选时，为可缩进状态)");
            }else if(startDate.contains(".")|| endDate.contains(".")){
                flag=false;
                errorMessage.addMessage("【起止时间】错误,请检查是否为时间格式(筛选时，为可缩进状态)");
            }else if (Integer.parseInt(startDate)<40179 ||  Integer.parseInt(startDate)>47483){
                flag=false;
                errorMessage.addMessage("【始期】错误,时间范围异常，请检查时间值是否在正确的范围");
            }else if (Integer.parseInt(endDate)<40179 ||  Integer.parseInt(endDate)>47483){
                flag=false;
                errorMessage.addMessage("【终期】错误,时间范围异常，请检查时间值是否在正确的范围");
            }else if (Integer.parseInt(startDate)>Integer.parseInt(endDate)){
                flag=false;
                errorMessage.addMessage("【始期】不应大于【终期】");
            }

            //起度、止度、电损、电量、垫资总额、
            if (!NumberUtils.isNumber(electric.get(ExcelColumns.INDEX_ELECTRIC_STARTDEGRESS))){
                flag=false;
                errorMessage.addMessage("【起度】错误,请检查是否有空格或非数字");
            }
            if (!NumberUtils.isNumber(electric.get(ExcelColumns.INDEX_ELECTRIC_ENDDEGRESS))){
                flag=false;
                errorMessage.addMessage("【止度】错误,请检查是否有空格或非数字");
            }
            if (!NumberUtils.isNumber(electric.get(ExcelColumns.INDEX_ELECTRIC_ELECTRICQUANTITY))){
                flag=false;
                errorMessage.addMessage("【电量】错误,请检查是否有空格或非数字");
            }
            if (!NumberUtils.isNumber(electric.get(ExcelColumns.INDEX_ELECTRIC_PAYMONEY))){
                flag=false;
                errorMessage.addMessage("【垫资总额】错误,请检查是否有空格或非数字");
            }

            //共享运营商
            String shareCustomer = electric.get(ExcelColumns.INDEX_ELECTRIC_SHARECUSTOMER);
            if (!Group.ShareCustomerSet.contains(shareCustomer)){
                flag=false;
                errorMessage.addMessage("【共享运营商】错误,请参导入模板表二限定字段");
            }

            //分摊比例
            String proportion=electric.get(ExcelColumns.INDEX_ELECTRIC_PROPORTION);
            if (!NumberUtils.isNumber(proportion)){
                flag=false;
                errorMessage.addMessage("【分摊比例】错误,请检查是否大于0且小于等于100%");
            }else if (Double.parseDouble(proportion)<=0 && Double.parseDouble(proportion)>1){
                flag=false;
                errorMessage.addMessage("【分摊比例】错误,请检查是否大于0且小于等于100%");
            }



            //账期
            String accountPeriod=electric.get(ExcelColumns.INDEX_ELECTRIC_ACCOUNTPERIOD);
            if (!NumberUtils.isNumber(accountPeriod)){
                errorMessage.addMessage("【账期】错误,请检查是否有空格或非数字");
            }else if (accountPeriod.contains(".")){
                errorMessage.addMessage("【账期】错误,请检查是否有空格或非数字");
            }else if(accountPeriod.length()!=6){
                errorMessage.addMessage("【账期】错误,请检查长度是否为6位");

            }else if(Integer.parseInt(accountPeriod)>202212 || Integer.parseInt(accountPeriod)<201401){
                errorMessage.addMessage("【账期】错误,请检查是否过大或者过小");

            }else if(Integer.parseInt(accountPeriod.substring(4))>12 ){
                errorMessage.addMessage("【账期月份】错误,不应大于12");
            }else if(accountPeriod.substring(4).equals("00") ){
                errorMessage.addMessage("【账期月份】错误,不应等于00");
            }


            //结算运营商
            String customer = electric.get(ExcelColumns.INDEX_ELECTRIC_CUSTOMER);
            if (!Group.CustomerSet.contains(customer)){
                flag=false;
                errorMessage.addMessage("【结算运营商】错误,请参导入模板表二限定字段");
            }else if (!shareCustomer.contains(customer)){
                flag=false;
                errorMessage.addMessage("【共享运营商】应包含【结算运营商】");
            }

            //开票编号
            String rebackCode=electric.get(ExcelColumns.INDEX_ELECTRIC_REBACKCODE);
            if (rebackCodeSet.contains(rebackCode)){
                flag=false;
                errorMessage.addMessage("【回款编号】系统已存在,请检查本次明细是否已导入");
            }
            if(!rebackCode.equals(soleRebackCode)){
                flag=false;
                errorMessage.addMessage("【回款编号】错误,同一导入表回款编号应当一致");
            }else{
                String[] rebackCodeStr=rebackCode.split("-");
                //

                if(rebackCodeStr.length!=4){
                    flag=false;
                    errorMessage.addMessage("【回款编号】格式错误");
                }else if (!rebackCodeStr[0].equals(region)){
                    flag=false;
                    errorMessage.addMessage("【回款编号】错误,第一部分应等于区域");
                }else if(!rebackCodeStr[1].equals(customer)){
                    flag=false;
                    errorMessage.addMessage("【回款编号】错误,第二部分应等于结算运营商");
                }else if (!rebackCodeStr[2].equals(accountPeriod)){
                    flag=false;
                    errorMessage.addMessage("【回款编号】错误,第三部分应等于账期");
                }else if(rebackCodeStr[3].length()!=3){
                    flag=false;
                    errorMessage.addMessage("【回款编号】错误,第四部分应为三位数字");
                }
            }


            //结算金额
            String settlement=electric.get(ExcelColumns.INDEX_ELECTRIC_SETTLEMENT);
            if (!NumberUtils.isNumber(settlement)){
                flag=false;
                errorMessage.addMessage("【结算金额】错误,请检查是否有空格或非数字");
            }else {

                total=total+NumberUtils.toDouble(settlement);
            }

        }




        return new Result(flag,errorMessage.getMessage().toString(),MyUtils.to2Round(total));
    }
}
