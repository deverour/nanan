package com.tower.nanan.service;

import com.tower.nanan.dao.ElectricDao;
import com.tower.nanan.dao.VerifyDao;
import com.tower.nanan.entity.Cache;
import com.tower.nanan.entity.ExcelColumns;
import com.tower.nanan.entity.Result;
import com.tower.nanan.poi.ExcelRead;
import com.tower.nanan.pojo.Electric;
import com.tower.nanan.pojo.User;
import com.tower.nanan.pojo.Verify;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class VerifyService implements InitializingBean {

    @Autowired
    private VerifyDao verifyDao;



    @Override
    public void afterPropertiesSet() throws Exception {
        Cache.verifyCodeSet = verifyDao.getVerifyCodeSet();
        Cache.billIdSet = verifyDao.getBillIdSet();
    }

    public Set<String> getVerifyCache(){
        return verifyDao.getVerifyCodeSet();

    }


    public void saveVerify(File file, User user) throws Exception {
        ExcelRead excelRead = new ExcelRead(file.getPath(),2);
        List<List<String>> verifys = excelRead.getMyDataList();
        Verify verify = new Verify();
        int index = 1;
        for (List<String> ver : verifys) {
            String region =ver.get(ExcelColumns.INDEX_VERIFY_REGION);
            String verifyCode = ver.get(ExcelColumns.INDEX_VERIFY_VERIFYCODE);
            String customer = ver.get(ExcelColumns.INDEX_VERIFY_CUSTOMER);
            String billId = ver.get(ExcelColumns.INDEX_VERIFY_BILLID);
            String billState = ver.get(ExcelColumns.INDEX_VERIFY_BILLSTATE);
            String taxMoney = ver.get(ExcelColumns.INDEX_VERIFY_TAXMONEY);
            verify.setVerifyCode(verifyCode);
            verify.setBillId(billId);
            verify.setBillState(billState);
            verify.setCustomer(customer);
            verify.setTaxMoney(taxMoney);
            Example example = new Example(Verify.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("billId",billId);
            index ++;
            System.out.println("index"+index);
            Verify oldVerify = verifyDao.selectOneByExample(example);
            System.out.println(oldVerify);
            if (oldVerify != null){

                verifyDao.updateByExample(verify,example);
                System.out.println("updateByExample");

            }else {
                System.out.println("insertSelective");
                verifyDao.insertSelective(verify);
                Cache.verifyCodeSet.add(verifyCode);
                Cache.billIdSet.add(billId);
            }
        }
    }


    public List<Verify> findAll() {
        return verifyDao.selectAll();
    }
}
