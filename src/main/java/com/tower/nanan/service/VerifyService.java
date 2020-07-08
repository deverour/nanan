package com.tower.nanan.service;


import com.tower.nanan.dao.VerifyDao;
import com.tower.nanan.entity.Cache;
import com.tower.nanan.entity.ExcelColumns;
import com.tower.nanan.entity.Group;
import com.tower.nanan.entity.Result;
import com.tower.nanan.poi.ExcelRead;
import com.tower.nanan.pojo.User;
import com.tower.nanan.pojo.Verify;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import java.io.File;
import java.util.List;


@Service
public class VerifyService implements InitializingBean {

    @Autowired
    private VerifyDao verifyDao;



    @Override
    public void afterPropertiesSet() throws Exception {
        Cache.verifyCodeSet = verifyDao.getVerifyCodeSet();
        Cache.billIdSet = verifyDao.getBillIdSet();
    }



    public Result saveVerify(File file, User user) throws Exception {
        ExcelRead excelRead = new ExcelRead(file.getPath(),2);
        List<List<String>> verifys = excelRead.getMyDataList();
        Verify verify = new Verify();

        for (List<String> ver : verifys) {
            String region =ver.get(ExcelColumns.INDEX_VERIFY_REGION);
            if (!Group.regionSet.contains(region)){
                return new Result(false,"【区域】错误,请参导入模板表二限定字段");
            }
            String verifyCode = ver.get(ExcelColumns.INDEX_VERIFY_VERIFYCODE);
            String customer = ver.get(ExcelColumns.INDEX_VERIFY_CUSTOMER);
            String billId = ver.get(ExcelColumns.INDEX_VERIFY_BILLID);
            String billState = ver.get(ExcelColumns.INDEX_VERIFY_BILLSTATE);
            String taxMoney = ver.get(ExcelColumns.INDEX_VERIFY_TAXMONEY);
            verify.setRegion(region);
            verify.setVerifyCode(verifyCode);
            verify.setBillId(billId);
            verify.setBillState(billState);
            verify.setCustomer(customer);
            verify.setTaxMoney(taxMoney);
            Example example = new Example(Verify.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("billId",billId);
            Verify oldVerify = verifyDao.selectOneByExample(example);
            System.out.println(oldVerify);
            if (oldVerify != null){
                verifyDao.updateByExample(verify,example);
            }else {
                verifyDao.insertSelective(verify);
                Cache.verifyCodeSet.add(verifyCode);
                Cache.billIdSet.add(billId);
            }
        }
        return new Result(true,"核销明细导入成功");
    }


    public List<Verify> findAll() {
        return verifyDao.selectAll();
    }
}
