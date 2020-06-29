package com.tower.nanan.service;

import com.tower.nanan.dao.PercentageDao;
import com.tower.nanan.entity.ExcelColumns;
import com.tower.nanan.entity.Result;
import com.tower.nanan.poi.ExcelRead;
import com.tower.nanan.poi.LogicCheck;
import com.tower.nanan.pojo.*;
import com.tower.nanan.utils.MyUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.io.File;
import java.util.Date;
import java.util.List;

@Service
public class PercentageService {

    @Autowired
    private PercentageDao percentageDao;

    public void savePercentageByElectric(String siteCode, String ammeterCode, String customer, String lastDate, String proportion) {
        Example example = new Example(Percentage.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("site_code",siteCode);
        criteria.andEqualTo("ammeter_code",ammeterCode);
        Percentage percentage = percentageDao.selectOneByExample(example);

        Percentage newpercentage = new Percentage();
        newpercentage.setSiteCode(siteCode);
        newpercentage.setAmmeterCode(ammeterCode);

        if (percentage.getSiteCode() != null){
            if (customer.equals("移动")) {
                if (NumberUtils.toDouble(lastDate) > NumberUtils.toDouble(percentage.getLastDate1())){
                    newpercentage.setLastDate1(lastDate);
                    newpercentage.setLastProportion1(proportion);
                }

            }
            if (customer.equals("联通")) {
                if (NumberUtils.toDouble(lastDate) > NumberUtils.toDouble(percentage.getLastDate2())){
                    newpercentage.setLastDate2(lastDate);
                    newpercentage.setLastProportion2(proportion);
                }
            }
            if (customer.equals("电信")) {
                if (NumberUtils.toDouble(lastDate) > NumberUtils.toDouble(percentage.getLastDate3())){
                    newpercentage.setLastDate3(lastDate);
                    newpercentage.setLastProportion3(proportion);
                }
            }
            percentageDao.updateByExampleSelective(newpercentage,example);
        }else {
            if (customer.equals("移动")) {
                newpercentage.setLastDate1(lastDate);
                newpercentage.setLastProportion1(proportion);
            }
            if (customer.equals("联通")) {
                newpercentage.setLastDate2(lastDate);
                newpercentage.setLastProportion2(proportion);
            }
            if (customer.equals("电信")) {
                newpercentage.setLastDate3(lastDate);
                newpercentage.setLastProportion3(proportion);
            }
            percentageDao.insertSelective(newpercentage);
        }
    }


    public Result savePercentage(File file) throws Exception {
        ExcelRead excelRead = new ExcelRead(file.getPath(),2);
        List<List<String>> percentages = excelRead.getMyDataList();
        Result result = LogicCheck.percentageCheck(percentages);

        if (result.isFlag()){
            for (List<String> per : percentages) {

                String siteCode = per.get(0);
                String ammeterCode = per.get(1);
                String newPerportion1 = per.get(2);
                String newPerportion2 = per.get(3);
                String newPerportion3 = per.get(4);
                Example example = new Example(Percentage.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("site_code",siteCode);
                criteria.andEqualTo("ammeter_code",ammeterCode);
                Percentage oldpercentage = percentageDao.selectOneByExample(example);
                System.out.println("oldpercentage"+oldpercentage);
                Percentage percentage = new Percentage();
                percentage.setSiteCode(siteCode);
                percentage.setAmmeterCode(ammeterCode);
                percentage.setNewProportion1(newPerportion1);
                percentage.setNewProportion2(newPerportion2);
                percentage.setNewProportion3(newPerportion3);

                if (oldpercentage.getSiteCode()==null){
                    percentageDao.insertSelective(percentage);
                }else {
                    percentageDao.updateByExampleSelective(percentage,example);
                }
            }

            return new Result(true,"物业系统比例导入成功");
        }
        return result;





    }
}