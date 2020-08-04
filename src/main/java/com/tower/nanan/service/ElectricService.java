package com.tower.nanan.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tower.nanan.dao.ElectricDao;
import com.tower.nanan.dao.RebackDao;
import com.tower.nanan.dao.VerifyDao;
import com.tower.nanan.entity.*;
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
public class ElectricService {

    @Autowired
    private VerifyDao verifyDao;

    @Autowired
    private ElectricDao electricDao;

    @Autowired
    private RebackDao rebackDao;

    @Autowired
    private PercentageService percentageService;

    @Transactional
    public Result saveElectrics(File file,User user) throws Exception {

        ExcelRead excelRead = new ExcelRead(file.getPath(),2);
        List<List<String>> electricList = excelRead.getMyDataList();
        Set verifyCodeSet = verifyDao.getVerifyCodeSet();
        Set rebackCodeSet = rebackDao.getRebackCodeSet();
        Result result = LogicCheck.electricCheck(electricList, user, rebackCodeSet, verifyCodeSet);
        if (result.isFlag()){
            Electric electric;

            for (List<String> elect : electricList) {
                electric = ExcelColumns.getElectric(elect);
                electric.setUploadDate(MyUtils.getExcelDate(new Date()));
                electric.setSettlementModel("代垫");
                electricDao.insertSelective(electric);
                percentageService.savePercentageByElectric(
                        electric.getSiteCode(),
                        electric.getAmmeterCode(),
                        electric.getCustomer(),
                        electric.getEndDate(),
                        electric.getProportion());
            }
            Electric electricR = ExcelColumns.getElectric(electricList.get(0));
            Reback reback = new Reback();
            reback.setRegion(electricR.getRegion());
            reback.setAccountPeriod(electricR.getAccountPeriod());
            reback.setCustomer(electricR.getCustomer());
            reback.setSettlementModel("代垫");
            reback.setRebackCode(electricR.getRebackCode());
            reback.setSettlement(result.getTotal());
            reback.setUploadDate(MyUtils.getExcelDate(new Date()));
            reback.setRebacked("否");
            System.out.println(reback);
            rebackDao.insertSelective(reback);
            return new Result(true,"签认明细导入成功");
        }
        return result;
    }

    public List<Electric> findByCondition(ElectricQueryBean electricQueryBean, User user){
        Example example = new Example(Electric.class);
        Example.Criteria criteria = example.createCriteria();
        if (!electricQueryBean.getId().isEmpty()){
            String id = electricQueryBean.getId();
            criteria.andEqualTo("id",id);
        }
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

        if (!electricQueryBean.getSiteCode().isEmpty()){

            String siteCode = electricQueryBean.getSiteCode();
            criteria.andEqualTo("siteCode",siteCode);
        }
        if (!electricQueryBean.getVerifyCode().isEmpty()){
            String verifyCode = electricQueryBean.getVerifyCode();
            criteria.andEqualTo("verifyCode",verifyCode);
        }

        if (electricQueryBean.getStartAccountPeriod() != null && !electricQueryBean.getStartAccountPeriod().isEmpty()){
            String startAccountPeriod = electricQueryBean.getStartAccountPeriod();
            criteria.andCondition("account_period >=",startAccountPeriod);
        }

        if (electricQueryBean.getEndAccountPeriod() != null && !electricQueryBean.getEndAccountPeriod().isEmpty()){
            String endAccountPeriod = electricQueryBean.getEndAccountPeriod();
            criteria.andCondition("account_period <=",endAccountPeriod);
        }

        return electricDao.selectByExample(example);

    }

    public PageResult pageQuery(ElectricQueryBean electricQueryBean, User user) {
        if (electricQueryBean.getCurrentPage() != null && electricQueryBean.getPageSize() != null) {
            PageHelper.startPage(electricQueryBean.getCurrentPage(), electricQueryBean.getPageSize());
        }
        Page<Electric> pageData = (Page<Electric>) findByCondition(electricQueryBean,user);
        Page<Electric> pageDataResult =new Page<>();
        for (Electric electric : pageData) {
            electric.setStartDate(MyUtils.getExcelDate(electric.getStartDate()));
            electric.setEndDate(MyUtils.getExcelDate(electric.getEndDate()));
            if (electric.getDirectSupply().equals("是")){
                electric.setDirectSupply("直供");
            }else {
                electric.setDirectSupply("转供");
            }
            pageDataResult.add(electric);
        }

        return new PageResult(pageData.getTotal(),pageDataResult.getResult());
    }


    @Transactional
    public void update(File file, User user) throws Exception {
        ExcelRead excelRead = new ExcelRead(file.getPath(),2);
        List<List<String>> lists = excelRead.getMyDataList();
        int index = 2;
        Set verifyCodeSet = verifyDao.getVerifyCodeSet();
        for (List<String> list : lists) {
            if (verifyCodeSet.contains(list.get(1))){
                Electric electric = electricDao.selectByPrimaryKey(list.get(0));
                if (electric == null){
                    throw new RuntimeException("第"+index+"行,电费编号："+list.get(0)+"不存在,请下载最新的电费明细后重试");
                }
                if (!user.getRegion().equals(electric.getRegion()) || !user.getNgroup().equals("admin")){
                    throw new RuntimeException("第"+index+"行,电费编号："+list.get(0)+"  ,区域为:【"+electric.getRegion()+"】,本账号没有补录该区域的权限");
                }
                if (!electric.getVerifyCode().equals("待补录")){
                    throw new RuntimeException("第"+index+"行,电费编号："+list.get(0)+"  ,系统已存在核销单号，无需补录，如需更改请删除后重新导入");
                }
                String verifyCode =list.get(1);
                electric.setVerifyCode(verifyCode);
                electricDao.updateByPrimaryKeySelective(electric);
            }else {
                throw new RuntimeException("第"+index+"行,核销单号："+list.get(1)+"不存在");
            }
            index++;

        }
    }
}
