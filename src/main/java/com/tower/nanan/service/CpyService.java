package com.tower.nanan.service;

import com.tower.nanan.dao.CpyDao;
import com.tower.nanan.dao.ElectricDao;
import com.tower.nanan.dao.RebackDao;
import com.tower.nanan.entity.Cache;
import com.tower.nanan.entity.ExcelColumns;
import com.tower.nanan.entity.Result;
import com.tower.nanan.poi.ExcelRead;
import com.tower.nanan.poi.LogicCheck;
import com.tower.nanan.pojo.Electric;
import com.tower.nanan.pojo.IncomeCpy;
import com.tower.nanan.pojo.Reback;
import com.tower.nanan.pojo.User;
import com.tower.nanan.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Date;
import java.util.List;

@Service
public class CpyService {

    @Autowired
    private ElectricDao electricDao;

    @Autowired
    private RebackDao rebackDao;

    @Autowired
    private CpyDao cpyDao;

    @Autowired
    private PercentageService percentageService;

    @Transactional
    public Result saveCostCpys(File file, User user) throws Exception {
        ExcelRead excelRead = new ExcelRead(file.getPath(),2);
        List<List<String>> electricList = excelRead.getMyDataList();
        Result result = LogicCheck.cpyCheck(electricList, user, Cache.rebackCodeSet, Cache.verifyCodeSet);

        if (result.isFlag()){
            Electric electric;

            for (List<String> elect : electricList) {
                electric = ExcelColumns.getElectric(elect);
                electric.setUploadDate(MyUtils.getExcelDate(new Date()));
                electric.setSettlementModel("包干");
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
            reback.setRebacked("是");
            String rebackDate = MyUtils.getExcelDate(new Date());
            reback.setRebackDate(rebackDate);
            reback.setRegion(electricR.getRegion());
            reback.setAccountPeriod(electricR.getAccountPeriod());
            reback.setCustomer(electricR.getCustomer());
            reback.setSettlementModel("包干");
            reback.setRebackCode(electricR.getRebackCode());
            reback.setSettlement(result.getData().toString());
            reback.setUploadDate(MyUtils.getExcelDate(new Date()));
            rebackDao.insertSelective(reback);
            Cache.rebackCodeSet.add(electricR.getRebackCode());
            return new Result(true,"包干明细导入成功");
        }
        return result;
    }

    public Result saveIncomeCpys(File file, User user) throws Exception {
        ExcelRead excelRead = new ExcelRead(file.getPath(),2);
        List<List<String>> cpyList = excelRead.getMyDataList();
        Result result = LogicCheck.IncomeCpyCheck(cpyList);
        IncomeCpy incomeCpy = new IncomeCpy();
        if (result.isFlag()){


            for (List<String> cpy : cpyList) {
               String region = cpy.get(ExcelColumns.INDEX_CPY_REGION);
               String siteCode = cpy.get(ExcelColumns.INDEX_CPY_SITECODE);
               String siteName = cpy.get(ExcelColumns.INDEX_CPY_SITENAME);
               String notaxMoney = cpy.get(ExcelColumns.INDEX_CPY_NOTAXMONEY);
               String accountPeriod = cpy.get(ExcelColumns.INDEX_CPY_ACCOUNTPERIOD);
               incomeCpy.setRegion(region);
               incomeCpy.setSiteCode(siteCode);
               incomeCpy.setSiteName(siteName);
               incomeCpy.setNotaxMoney(notaxMoney);
               incomeCpy.setAccountPeriod(accountPeriod);



                incomeCpy.setUploadDate(MyUtils.getExcelDate(new Date()));

                cpyDao.insertSelective(incomeCpy);

            }

            return new Result(true,"包干收入导入成功");
        }
        return result;
    }
}


