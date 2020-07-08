package com.tower.nanan.service;

import com.tower.nanan.dao.ElectricDao;
import com.tower.nanan.dao.RebackStatWithCustomerDao;
import com.tower.nanan.dao.RebackStatWithSiteDao;
import com.tower.nanan.dao.VerifyDao;
import com.tower.nanan.entity.StatTempWithSite;
import com.tower.nanan.pojo.Electric;
import com.tower.nanan.pojo.RebackStatWithCustomer;
import com.tower.nanan.pojo.RebackStatWithSite;
import com.tower.nanan.entity.StatTempWithCustomer;
import com.tower.nanan.pojo.Verify;
import com.tower.nanan.utils.MyUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class StatService {

    @Autowired
    private VerifyDao verifyDao;

    @Autowired
    private ElectricDao electricDao;

    @Autowired
    private RebackStatWithCustomerDao rebackStatWithCustomerDao;

    @Autowired
    private RebackStatWithSiteDao rebackStatWithSiteDao;


    @Transactional
    public void rebackStat(){
        System.out.println("开始统计");
        Map<String, StatTempWithCustomer> verifyMap = new HashMap<>();
        List<Verify> verifies = verifyDao.selectAll();
        for (Verify verify : verifies) {
            String region = verify.getRegion();
            String verifyCode = verify.getVerifyCode();
            String payDate = verify.getPayDate();
            String customer = verify.getCustomer();
            String verifyKey = verifyCode + "-" +customer;
            Double taxMoney = NumberUtils.toDouble(verify.getTaxMoney());
            StatTempWithCustomer statTempWithCustomer = new StatTempWithCustomer(region,verifyCode,payDate,customer,taxMoney);
            if (verifyMap.containsKey(verifyKey)){
                taxMoney = taxMoney + verifyMap.get(verifyKey).getTaxMoney();
                statTempWithCustomer.setTaxMoney(taxMoney);
                verifyMap.put(verifyKey, statTempWithCustomer);
            }else {
                verifyMap.put(verifyKey, statTempWithCustomer);
            }
        }

        Map<String,Double> electricMap = new HashMap<>();
        List<Electric> electrics = electricDao.selectAll();
        for (Electric electric : electrics) {
            String verifyCode = electric.getVerifyCode();
            String customer = electric.getCustomer();
            String electricKey = verifyCode + "-" +customer;
            Double notaxMoney = NumberUtils.toDouble(electric.getSettlement());
            if (electricMap.containsKey(electricKey)){
                electricMap.put(electricKey,notaxMoney+electricMap.get(electricKey));
            }else {
                electricMap.put(electricKey,notaxMoney);
            }
        }

        rebackStatWithCustomerDao.truncate();

        for (Map.Entry<String, StatTempWithCustomer> entry : verifyMap.entrySet()) {
            System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());

            //String[] split = entry.getKey().split("-");
            StatTempWithCustomer statTempWithCustomer = entry.getValue();
            RebackStatWithCustomer rebackStatWithCustomer = new RebackStatWithCustomer();
            rebackStatWithCustomer.setRegion(statTempWithCustomer.getRegion());
            rebackStatWithCustomer.setVerifyCode(statTempWithCustomer.getVerifyCode());
            rebackStatWithCustomer.setPayDate(statTempWithCustomer.getPayDate());
            rebackStatWithCustomer.setCustomer(statTempWithCustomer.getCustomer());
            rebackStatWithCustomer.setVerifyMoney(String.valueOf(MyUtils.to2Round(statTempWithCustomer.getTaxMoney())));

            if (electricMap.containsKey(entry.getKey())){
                rebackStatWithCustomer.setRebackMoney(String.valueOf(MyUtils.to2Round(electricMap.get(entry.getKey()))));
            }

            rebackStatWithCustomer.setStatDate(MyUtils.getExcelDate(new Date()));

            rebackStatWithCustomerDao.insertSelective(rebackStatWithCustomer);
        }
    }

    @Transactional
    public void rebackStatWithSite(){
        System.out.println("开始统计");
        Map<String,StatTempWithSite> verifyMap = new HashMap<>();
        List<Verify> verifies = verifyDao.selectAll();
        for (Verify verify : verifies) {
            String region = verify.getRegion();
            String verifyCode = verify.getVerifyCode();
            String payDate = verify.getPayDate();
            String siteCode = verify.getSiteCode();
            String verifyKey = verifyCode + "-" +siteCode;
            Double taxMoney = NumberUtils.toDouble(verify.getTaxMoney());
            StatTempWithSite statTempWithSite = new StatTempWithSite(region,verifyCode,payDate,siteCode,taxMoney);
            if (verifyMap.containsKey(verifyKey)){
                taxMoney = taxMoney + verifyMap.get(verifyKey).getTaxMoney();
                statTempWithSite.setTaxMoney(taxMoney);
                verifyMap.put(verifyKey,statTempWithSite);
            }else {
                verifyMap.put(verifyKey,statTempWithSite);
            }
        }

        Map<String,Double> electricMap = new HashMap<>();
        List<Electric> electrics = electricDao.selectAll();
        for (Electric electric : electrics) {
            String verifyCode = electric.getVerifyCode();
            String siteCode = electric.getSiteCode();
            String electricKey = verifyCode + "-" +siteCode;
            Double notaxMoney = NumberUtils.toDouble(electric.getSettlement());
            if (electricMap.containsKey(electricKey)){
                electricMap.put(electricKey,notaxMoney+electricMap.get(electricKey));
            }else {
                electricMap.put(electricKey,notaxMoney);
            }
        }

        rebackStatWithSiteDao.truncate();

        for (Map.Entry<String, StatTempWithSite> entry : verifyMap.entrySet()) {
            System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());

            //String[] split = entry.getKey().split("-");
            StatTempWithSite statTempWithSite = entry.getValue();
            RebackStatWithSite rebackStatWithSite = new RebackStatWithSite();
            rebackStatWithSite.setRegion(statTempWithSite.getRegion());
            rebackStatWithSite.setVerifyCode(statTempWithSite.getVerifyCode());
            rebackStatWithSite.setPayDate(statTempWithSite.getPayDate());
            rebackStatWithSite.setStatDate(statTempWithSite.getSiteCode());
            rebackStatWithSite.setVerifyMoney(String.valueOf(MyUtils.to2Round(statTempWithSite.getTaxMoney())));

            if (electricMap.containsKey(entry.getKey())){
                rebackStatWithSite.setRebackMoney(String.valueOf(MyUtils.to2Round(electricMap.get(entry.getKey()))));
            }

            rebackStatWithSite.setStatDate(MyUtils.getExcelDate(new Date()));

            rebackStatWithSiteDao.insertSelective(rebackStatWithSite);
        }
    }






    public List<RebackStatWithCustomer> findRebackStat() {
        return rebackStatWithCustomerDao.selectAll();
    }
}



