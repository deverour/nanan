package com.tower.nanan.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tower.nanan.dao.ElectricDao;
import com.tower.nanan.entity.ElectricQueryBean;
import com.tower.nanan.entity.ExcelColumns;
import com.tower.nanan.entity.PageResult;
import com.tower.nanan.entity.Result;
import com.tower.nanan.poi.ExcelRead;
import com.tower.nanan.poi.LogicCheck;
import com.tower.nanan.pojo.Electric;
import com.tower.nanan.pojo.Reback;
import com.tower.nanan.pojo.User;
import com.tower.nanan.utils.MyUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class ElectricService implements InitializingBean {

    public static Set rebackCodeSet;
    public static Set verifyCodeSet;

    @Autowired
    private ElectricDao electricDao;

    @Autowired
    private RebackService rebackService;

    @Autowired
    private VerifyService verifyService;

    @Transactional
    public Result saveElectrics(File file,User user) throws Exception {

        ExcelRead excelRead = new ExcelRead(file.getPath(),2);
        List<List<String>> electricList = excelRead.getMyDataList();
        Result result = LogicCheck.electricCheck(electricList, user, rebackCodeSet,verifyCodeSet);
        if (result.isFlag()){
            Electric electric;

            for (List<String> elect : electricList) {
                electric = ExcelColumns.getElectric(elect);
                System.out.println(">>>>>>>>>>>>>>>>>"+electric);
                electricDao.insertSelective(electric);
            }
            Electric electricR = ExcelColumns.getElectric(electricList.get(0));
            Reback reback = new Reback();
            reback.setRegion(electricR.getRegion());
            reback.setAccountPeriod(electricR.getAccountPeriod());
            reback.setCustomer(electricR.getCustomer());
            reback.setSettlementModel("代垫");
            reback.setRebackCode(electricR.getRebackCode());
            reback.setSettlement(result.getData().toString());
            reback.setUploadDate(MyUtils.getExcelDate(new Date()));
            return new Result(true,"签认明细导入成功");
        }
        return result;
    }

    public List<Electric> findByCondition(ElectricQueryBean electricQueryBean, User user){
        Example example = new Example(Electric.class);
        Example.Criteria criteria = example.createCriteria();
        if (electricQueryBean.getRegions() != null && !electricQueryBean.getRegions().isEmpty()){
            for (String region : electricQueryBean.getRegions()) {
                criteria.orEqualTo("region",region);
            }
        }

        if (electricQueryBean.getCustomers() != null && !electricQueryBean.getCustomers().isEmpty()){
            for (String customer : electricQueryBean.getCustomers()) {
                criteria.orEqualTo("customer",customer);
            }
        }
        if (electricQueryBean.getVerifyCode() != null && !electricQueryBean.getVerifyCode().isEmpty()){
            String verifyCode = electricQueryBean.getVerifyCode();
            criteria.andEqualTo("verifyCode",verifyCode);
        }

        if (electricQueryBean.getStartAccountPeriod() != null && !electricQueryBean.getStartAccountPeriod().isEmpty()){
            String startAccountPeriod = electricQueryBean.getStartAccountPeriod();
            criteria.andCondition("accountPeriod >=",startAccountPeriod);
        }

        if (electricQueryBean.getEndAccountPeriod() != null && !electricQueryBean.getEndAccountPeriod().isEmpty()){
            String endAccountPeriod = electricQueryBean.getEndAccountPeriod();
            criteria.andCondition("accountPeriod >=",endAccountPeriod);
        }
        if (electricQueryBean.getCurrentPage() != null && electricQueryBean.getPageSize() != null) {
            PageHelper.startPage(electricQueryBean.getCurrentPage(), electricQueryBean.getPageSize());
        }
        return electricDao.selectByExample(example);

    }

    public double query(ElectricQueryBean electricQueryBean, User user){
        double total =0.0;
        List<Electric> electrics = findByCondition(electricQueryBean,user);
        for (Electric electric : electrics) {
            total=total+ NumberUtils.toDouble(electric.getSettlement());
        }


        return MyUtils.to2Round(total);
    }

    public PageResult pageQuery(ElectricQueryBean electricQueryBean, User user) {

        Page<Electric> pageDate = (Page<Electric>) findByCondition(electricQueryBean,user);
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        rebackCodeSet = rebackService.getRebackCodeSet();
        verifyCodeSet = verifyService.getVerifyCodeSet();
    }
}
