package com.tower.nanan.service;

import com.tower.nanan.dao.VerifyDao;
import com.tower.nanan.entity.Result;
import com.tower.nanan.poi.ExcelRead;
import com.tower.nanan.pojo.User;
import com.tower.nanan.pojo.Verify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Set;

@Service
public class VerifyService {

    @Autowired
    private VerifyDao verifyDao;

    public Set<String> getVerifyCodeSet(){
        return verifyDao.getVerifyCodeSet();
    }


    public void saveVerify(File file, User user) throws Exception {
        ExcelRead excelRead = new ExcelRead(file.getPath(),2);
        List<List<String>> verifys = excelRead.getMyDataList();
        Verify verify = new Verify();
        for (List<String> ver : verifys) {
            verify.setVerifyCode(ver.get(0));
            verify.setNoTaxMoney(ver.get(1));
            verifyDao.insertSelective(verify);
        }
    }
}
