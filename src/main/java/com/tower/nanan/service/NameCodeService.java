package com.tower.nanan.service;


import com.tower.nanan.dao.NameCodeDao;
import com.tower.nanan.entity.Cache;
import com.tower.nanan.entity.ExcelColumns;
import com.tower.nanan.entity.Result;
import com.tower.nanan.poi.ExcelRead;
import com.tower.nanan.poi.LogicCheck;
import com.tower.nanan.pojo.Electric;
import com.tower.nanan.pojo.NameCode;
import com.tower.nanan.pojo.Reback;
import com.tower.nanan.utils.MyUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;
import java.util.List;

@Service
public class NameCodeService {

    @Autowired
    private NameCodeDao nameCodeDao;


    public Result  saveNameCode(File file) throws Exception {
        ExcelRead excelRead = new ExcelRead(file.getPath(),2);
        List<List<String>> nameCodeList = excelRead.getMyDataList();

        Result result = LogicCheck.nameCodeCheck(nameCodeList);
        if (result.isFlag()){
           NameCode nameCode = new NameCode();

            for (List<String> nc : nameCodeList) {

                nameCode.setSiteCode(nc.get(0));
                nameCode.setSiteName(nc.get(1));
                if (nameCodeDao.selectByPrimaryKey(nc.get(0)) == null){
                    nameCodeDao.insertSelective(nameCode);

                }else {
                    nameCodeDao.updateByPrimaryKeySelective(nameCode);
                }

            }
            return new Result(true,"站址编码名称关系表导入成功");

        }
        return result;
    }



}
