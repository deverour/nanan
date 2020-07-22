package com.tower.nanan.service;

import com.tower.nanan.dao.*;
import com.tower.nanan.entity.RebackStatQueryBean;
import com.tower.nanan.entity.StatTemp;
import com.tower.nanan.entity.StatTempWithSite;
import com.tower.nanan.pojo.*;
import com.tower.nanan.entity.StatTempWithCustomer;
import com.tower.nanan.utils.MyUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.text.ParseException;
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

    @Autowired
    private RebackStatDao rebackStatDao;

    @Transactional
    public void rebackStatForCustomer(){
        System.out.println("开始统计");
        Map<String, StatTempWithCustomer> verifyMap = new HashMap<>();
        Example example = new Example(Verify.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("billState","已分摊");
        List<Verify> verifies = verifyDao.selectByExample(example);
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

            StatTempWithCustomer statTempWithCustomer = entry.getValue();
            RebackStatWithCustomer rebackStatWithCustomer = new RebackStatWithCustomer();
            rebackStatWithCustomer.setRegion(statTempWithCustomer.getRegion());
            rebackStatWithCustomer.setVerifyCode(statTempWithCustomer.getVerifyCode());
            rebackStatWithCustomer.setPayDate(statTempWithCustomer.getPayDate());
            rebackStatWithCustomer.setCustomer(statTempWithCustomer.getCustomer());
            rebackStatWithCustomer.setVerifyMoney(String.valueOf(MyUtils.to2Round(statTempWithCustomer.getTaxMoney())));

            if (electricMap.containsKey(entry.getKey())){
                Double difference =Math.abs(electricMap.get(entry.getKey()) - statTempWithCustomer.getTaxMoney()) ;
                rebackStatWithCustomer.setRebackMoney(String.valueOf(MyUtils.to2Round(electricMap.get(entry.getKey()))));
                rebackStatWithCustomer.setDifference(difference);
            }else {
                rebackStatWithCustomer.setRebackMoney("0");
                rebackStatWithCustomer.setDifference(Math.abs((statTempWithCustomer.getTaxMoney())));
            }

            rebackStatWithCustomer.setStatDate(MyUtils.getExcelDate(new Date()));

            rebackStatWithCustomerDao.insertSelective(rebackStatWithCustomer);
        }
        System.out.println("支付-回款按客户统计完成");
    }

    @Transactional
    public void rebackStatForSite(){
        System.out.println("开始统计");
        Map<String,StatTempWithSite> verifyMap = new HashMap<>();
        Example example = new Example(Verify.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("billState","已分摊");
        List<Verify> verifies = verifyDao.selectByExample(example);
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


            //String[] split = entry.getKey().split("-");
            StatTempWithSite statTempWithSite = entry.getValue();
            RebackStatWithSite rebackStatWithSite = new RebackStatWithSite();

            rebackStatWithSite.setRegion(statTempWithSite.getRegion());
            rebackStatWithSite.setVerifyCode(statTempWithSite.getVerifyCode());
            rebackStatWithSite.setPayDate(statTempWithSite.getPayDate());
            rebackStatWithSite.setSiteCode(statTempWithSite.getSiteCode());
            rebackStatWithSite.setVerifyMoney(String.valueOf(MyUtils.to2Round(statTempWithSite.getTaxMoney())));
            rebackStatWithSite.setStatDate(statTempWithSite.getSiteCode());


            if (electricMap.containsKey(entry.getKey())){
                Double difference = electricMap.get(entry.getKey()) - statTempWithSite.getTaxMoney();
                rebackStatWithSite.setRebackMoney(String.valueOf(MyUtils.to2Round(electricMap.get(entry.getKey()))));
                rebackStatWithSite.setDifference(Math.abs(difference));
            }else {
                rebackStatWithSite.setRebackMoney("0");
                rebackStatWithSite.setDifference(statTempWithSite.getTaxMoney());
            }

            rebackStatWithSite.setStatDate(MyUtils.getExcelDate(new Date()));
            rebackStatWithSiteDao.insertSelective(rebackStatWithSite);
        }
        System.out.println("支付-回款按站址统计完成");
    }

    @Transactional
    public void rebackStat(){
        System.out.println("开始统计");
        Map<String,StatTemp> verifyMap = new HashMap<>();
        Example example = new Example(Verify.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("billState","已分摊");
        List<Verify> verifies = verifyDao.selectByExample(example);
        for (Verify verify : verifies) {
            String region = verify.getRegion();
            String verifyCode = verify.getVerifyCode();
            String payDate = verify.getPayDate();
            String siteCode = verify.getSiteCode();
            String customer = verify.getCustomer();
            String verifyKey = verifyCode + "-" + siteCode + "-" + customer;
            Double taxMoney = NumberUtils.toDouble(verify.getTaxMoney());
            StatTemp statTemp = new StatTemp(region,verifyCode,payDate,customer,siteCode,taxMoney);
            if (verifyMap.containsKey(verifyKey)){
                taxMoney = taxMoney + verifyMap.get(verifyKey).getTaxMoney();
                statTemp.setTaxMoney(taxMoney);
                verifyMap.put(verifyKey,statTemp);
            }else {
                verifyMap.put(verifyKey,statTemp);
            }
        }

        Map<String,Double> electricMap = new HashMap<>();
        List<Electric> electrics = electricDao.selectAll();
        for (Electric electric : electrics) {
            String verifyCode = electric.getVerifyCode();
            String siteCode = electric.getSiteCode();
            String customer =electric.getCustomer();
            String electricKey = verifyCode + "-" + siteCode + "-" + customer;
            Double notaxMoney = NumberUtils.toDouble(electric.getSettlement());
            if (electricMap.containsKey(electricKey)){
                electricMap.put(electricKey,notaxMoney+electricMap.get(electricKey));
            }else {
                electricMap.put(electricKey,notaxMoney);
            }
        }

        rebackStatDao.truncate();

        for (Map.Entry<String, StatTemp> entry : verifyMap.entrySet()) {


            //String[] split = entry.getKey().split("-");
            StatTemp statTemp = entry.getValue();
            RebackStat rebackStat = new RebackStat();

            rebackStat.setRegion(statTemp.getRegion());
            rebackStat.setVerifyCode(statTemp.getVerifyCode());
            rebackStat.setPayDate(statTemp.getPayDate());
            rebackStat.setSiteCode(statTemp.getSiteCode());
            rebackStat.setCustomer(statTemp.getCustomer());
            rebackStat.setVerifyMoney(String.valueOf(MyUtils.to2Round(statTemp.getTaxMoney())));
            rebackStat.setStatDate(statTemp.getSiteCode());


            if (electricMap.containsKey(entry.getKey())){
                Double difference = electricMap.get(entry.getKey()) - statTemp.getTaxMoney();
                rebackStat.setRebackMoney(String.valueOf(MyUtils.to2Round(electricMap.get(entry.getKey()))));
                rebackStat.setDifference(Math.abs(difference));
            }else {
                rebackStat.setRebackMoney("0");
                rebackStat.setDifference(statTemp.getTaxMoney());
            }

            rebackStat.setStatDate(MyUtils.getExcelDate(new Date()));
            rebackStatDao.insertSelective(rebackStat);
        }
        System.out.println("支付-回款统计完成");
    }

    public List<RebackStatWithCustomer> findRebackStatWithCustomerByCondition(RebackStatQueryBean rebackStatQueryBean) throws ParseException {
        Example example = new Example(Electric.class);
        Example.Criteria criteria = example.createCriteria();
        if (rebackStatQueryBean.getRegions() != null && !rebackStatQueryBean.getRegions().isEmpty()){
            for (String region : rebackStatQueryBean.getRegions()) {
                criteria.orEqualTo("region",region);
            }
        }

        if (rebackStatQueryBean.getCustomers() != null && !rebackStatQueryBean.getCustomers().isEmpty()){
            for (String customer : rebackStatQueryBean.getCustomers()) {
                criteria.orEqualTo("customer",customer);
            }
        }

        if (!rebackStatQueryBean.getVerifyCode().isEmpty()){
            String verifyCode = rebackStatQueryBean.getVerifyCode();
            criteria.andEqualTo("verifyCode",verifyCode);
        }

        if (rebackStatQueryBean.getStartPayDate() != null && !rebackStatQueryBean.getStartPayDate().isEmpty()){
            String startPayDate = MyUtils.toExcelDate(rebackStatQueryBean.getStartPayDate());
            criteria.andCondition("pay_date >=",startPayDate);
        }

        if (rebackStatQueryBean.getEndPayDate() != null && !rebackStatQueryBean.getEndPayDate().isEmpty()){
            String endPayDate = MyUtils.toExcelDate(rebackStatQueryBean.getEndPayDate());
            criteria.andCondition("pay_date <=",endPayDate);
        }

        if (rebackStatQueryBean.getDifference() != null && !rebackStatQueryBean.getDifference().isEmpty()){
            String difference = rebackStatQueryBean.getDifference();
            criteria.andCondition("difference <=",difference);
        }

        return rebackStatWithCustomerDao.selectByExample(example);
    }

    public List<RebackStatWithSite> findRebackStatWithSiteByCondition(RebackStatQueryBean rebackStatQueryBean) throws ParseException {
        Example example = new Example(Electric.class);
        Example.Criteria criteria = example.createCriteria();
        if (rebackStatQueryBean.getRegions() != null && !rebackStatQueryBean.getRegions().isEmpty()){
            for (String region : rebackStatQueryBean.getRegions()) {
                criteria.orEqualTo("region",region);
            }
        }


        if (!rebackStatQueryBean.getSiteCode().isEmpty()){

            String siteCode = rebackStatQueryBean.getSiteCode();
            criteria.andEqualTo("siteCode",siteCode);
        }
        if (!rebackStatQueryBean.getVerifyCode().isEmpty()){
            String verifyCode = rebackStatQueryBean.getVerifyCode();
            criteria.andEqualTo("verifyCode",verifyCode);
        }

        if (rebackStatQueryBean.getStartPayDate() != null && !rebackStatQueryBean.getStartPayDate().isEmpty()){
            String startPayDate = MyUtils.toExcelDate(rebackStatQueryBean.getStartPayDate());
            criteria.andCondition("pay_date >=",startPayDate);
        }

        if (rebackStatQueryBean.getEndPayDate() != null && !rebackStatQueryBean.getEndPayDate().isEmpty()){
            String endPayDate = MyUtils.toExcelDate(rebackStatQueryBean.getEndPayDate());
            criteria.andCondition("pay_date <=",endPayDate);
        }

        if (rebackStatQueryBean.getDifference() != null && !rebackStatQueryBean.getDifference().isEmpty()){
            String difference = rebackStatQueryBean.getDifference();
            criteria.andCondition("difference <=",difference);
        }

        return rebackStatWithSiteDao.selectByExample(example);

    }
    public List<RebackStat> findRebackStatByCondition(RebackStatQueryBean rebackStatQueryBean) throws ParseException {
        Example example = new Example(Electric.class);
        Example.Criteria criteria = example.createCriteria();
        if (rebackStatQueryBean.getRegions() != null && !rebackStatQueryBean.getRegions().isEmpty()){
            for (String region : rebackStatQueryBean.getRegions()) {
                criteria.orEqualTo("region",region);
            }
        }
        if (rebackStatQueryBean.getCustomers() != null && !rebackStatQueryBean.getCustomers().isEmpty()){
            for (String customer : rebackStatQueryBean.getCustomers()) {
                criteria.orEqualTo("customer",customer);
            }
        }

        if (!rebackStatQueryBean.getSiteCode().isEmpty()){

            String siteCode = rebackStatQueryBean.getSiteCode();
            criteria.andEqualTo("siteCode",siteCode);
        }
        if (!rebackStatQueryBean.getVerifyCode().isEmpty()){
            String verifyCode = rebackStatQueryBean.getVerifyCode();
            criteria.andEqualTo("verifyCode",verifyCode);
        }

        if (rebackStatQueryBean.getStartPayDate() != null && !rebackStatQueryBean.getStartPayDate().isEmpty()){
            String startPayDate = MyUtils.toExcelDate(rebackStatQueryBean.getStartPayDate());
            criteria.andCondition("pay_date >=",startPayDate);
        }

        if (rebackStatQueryBean.getEndPayDate() != null && !rebackStatQueryBean.getEndPayDate().isEmpty()){
            String endPayDate = MyUtils.toExcelDate(rebackStatQueryBean.getEndPayDate());
            criteria.andCondition("pay_date <=",endPayDate);
        }

        if (rebackStatQueryBean.getDifference() != null && !rebackStatQueryBean.getDifference().isEmpty()){
            String difference = rebackStatQueryBean.getDifference();
            criteria.andCondition("difference <=",difference);
        }

        return rebackStatDao.selectByExample(example);

    }
}



