package com.tower.nanan.service;


import com.tower.nanan.dao.VerifyDao;
import com.tower.nanan.entity.Cache;
import com.tower.nanan.entity.ExcelColumns;
import com.tower.nanan.entity.Group;
import com.tower.nanan.entity.Result;
import com.tower.nanan.poi.ExcelRead;
import com.tower.nanan.poi.LogicCheck;
import com.tower.nanan.pojo.User;
import com.tower.nanan.pojo.Verify;
import com.tower.nanan.utils.MyUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import java.io.File;
import java.util.List;


@Service
public class VerifyService implements InitializingBean {

    @Autowired
    private VerifyDao verifyDao;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;




    @Override
    public void afterPropertiesSet() throws Exception {
        Cache.verifyCodeSet = verifyDao.getVerifyCodeSet();
        Cache.billIdSet = verifyDao.getBillIdSet();
    }



    public Result saveVerify(File file, User user) throws Exception {
        ExcelRead excelRead = new ExcelRead(file.getPath(),2);
        List<List<String>> verifys = excelRead.getMyDataList();
        Verify verify = new Verify();
        Result result = LogicCheck.verifyCheck(verifys, user, Cache.rebackCodeSet, Cache.verifyCodeSet);
        if (!result.isFlag()){

            return  result;
        }
        SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        VerifyDao verifyMapper = sqlSession.getMapper(VerifyDao.class);
        long t =0;
        long t1 = System.currentTimeMillis();
        for (List<String> ver : verifys) {
            String region =ver.get(ExcelColumns.INDEX_VERIFY_REGION);
            String verifyCode = ver.get(ExcelColumns.INDEX_VERIFY_VERIFYCODE);
            String billId = ver.get(ExcelColumns.INDEX_VERIFY_BILLID);
            String billState = ver.get(ExcelColumns.INDEX_VERIFY_BILLSTATE);
            String payDate = ver.get(ExcelColumns.INDEX_VERIFY_PAYDATE);
            String siteCode = ver.get(ExcelColumns.INDEX_VERIFY_SITECODE);
            String customer = ver.get(ExcelColumns.INDEX_VERIFY_CUSTOMER);
            String taxMoney = ver.get(ExcelColumns.INDEX_VERIFY_TAXMONEY);

            verify.setRegion(region);
            verify.setVerifyCode(verifyCode);
            verify.setBillId(billId);
            verify.setBillState(billState);
            verify.setPayDate(payDate);
            verify.setSiteCode(siteCode);
            verify.setCustomer(customer);
            verify.setTaxMoney(MyUtils.to2Round(taxMoney));

            Example example = new Example(Verify.class);
            long l1 = System.currentTimeMillis();
            Verify oldVerify = verifyDao.selectByPrimaryKey(billId);
            t=t+ System.currentTimeMillis()-l1;
            if (oldVerify != null){
                verifyDao.updateByPrimaryKey(verify);
            }else {
                verifyMapper.insertSelective(verify);
                Cache.verifyCodeSet.add(verifyCode);
                Cache.billIdSet.add(billId);
            }
            sqlSession.commit();
            sqlSession.clearCache();
        }
        long t2 = System.currentTimeMillis();
        System.out.println("2>>>>"+t/1000);
        System.out.println("总耗时"+(t2-t1)/1000);
        return new Result(true,"核销明细导入成功");
    }


    public List<Verify> findAll() {
        return verifyDao.selectAll();
    }
}
