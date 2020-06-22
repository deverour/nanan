package com.tower.nanan.service;

import com.tower.nanan.poi.ExcelRead;
import com.tower.nanan.pojo.LogicCheck;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class ElectricService {

  /*  public String checkElectrics(List<List<String>> electricList){
        return null;
    }*/

    public String saveElectrics(File file) throws Exception {
        ExcelRead excelRead = new ExcelRead(file.getPath(),2);
        List<List<String>> electricList = excelRead.getMyDataList();
        String message = LogicCheck.electricCheck(electricList,null);
        if (message.length() == 0){
            return "";
        }
        return message;
    }

}
