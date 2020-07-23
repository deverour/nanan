package com.tower.nanan.service;

import com.tower.nanan.dao.*;
import com.tower.nanan.entity.*;
import com.tower.nanan.pojo.*;
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
    private CpyDao cpyDao;

    @Autowired
    private ElectricDao electricDao;

    @Autowired
    private RebackStatWithCustomerDao rebackStatWithCustomerDao;

    @Autowired
    private RebackStatWithSiteDao rebackStatWithSiteDao;

    @Autowired
    private RebackStatDao rebackStatDao;

    @Autowired
    private RebackStatWithReportDao rebackStatWithReportDao;

    @Autowired
    private RebackStatWithCpyDao rebackStatWithCpyDao;

    @Autowired
    private NameCodeDao nameCodeDao;

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
            Double notaxMoney = electric.getSettlement();
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
            rebackStatWithCustomer.setVerifyMoney(MyUtils.to2Round(statTempWithCustomer.getTaxMoney()));

            if (electricMap.containsKey(entry.getKey())){
                Double difference =Math.abs(electricMap.get(entry.getKey()) - statTempWithCustomer.getTaxMoney()) ;
                rebackStatWithCustomer.setRebackMoney(MyUtils.to2Round(electricMap.get(entry.getKey())));
                rebackStatWithCustomer.setDifference(difference);
            }else {
                rebackStatWithCustomer.setRebackMoney(0.0);
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
            Double taxMoney = electric.getSettlement();
            if (electricMap.containsKey(electricKey)){
                electricMap.put(electricKey,taxMoney+electricMap.get(electricKey));
            }else {
                electricMap.put(electricKey,taxMoney);
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
            rebackStatWithSite.setVerifyMoney(MyUtils.to2Round(statTempWithSite.getTaxMoney()));
            NameCode nameCode = nameCodeDao.selectByPrimaryKey(statTempWithSite.getSiteCode());
            if (nameCode != null){
                rebackStatWithSite.setSiteName(nameCode.getSiteName());
            }


            if (electricMap.containsKey(entry.getKey())){
                Double difference = electricMap.get(entry.getKey()) - statTempWithSite.getTaxMoney();
                rebackStatWithSite.setRebackMoney(MyUtils.to2Round(electricMap.get(entry.getKey())));
                rebackStatWithSite.setDifference(Math.abs(difference));
            }else {
                rebackStatWithSite.setRebackMoney(0.0);
                rebackStatWithSite.setDifference(statTempWithSite.getTaxMoney());
            }

            rebackStatWithSite.setStatDate(MyUtils.getExcelDate(new Date()));
            rebackStatWithSiteDao.insertSelective(rebackStatWithSite);
        }
        System.out.println("支付-回款按站址统计完成");
    }

    @Transactional
    public void rebackStatWithReport(){
        System.out.println("开始统计");
        Map<String,Double> verifyMap = new HashMap<>();
        Example example = new Example(Verify.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("billState","已分摊");
        List<Verify> verifies = verifyDao.selectByExample(example);
        for (Verify verify : verifies) {
            String region = verify.getRegion();
            String accountPeriod = MyUtils.getaccountPeriodFromExcelDate(verify.getPayDate());
            String customer = verify.getCustomer();
            String verifyKey = region + "-" + accountPeriod + "-" + customer;
            Double taxMoney = NumberUtils.toDouble(verify.getTaxMoney());
            if (verifyMap.containsKey(verifyKey)){
                taxMoney = taxMoney + verifyMap.get(verifyKey);
                verifyMap.put(verifyKey,taxMoney);
            }else {
                verifyMap.put(verifyKey,taxMoney);
            }
        }

        Map<String,Double> electricMap = new HashMap<>();
        List<Electric> electrics = electricDao.selectAll();
        for (Electric electric : electrics) {
            String region = electric.getRegion();
            String accountPeriod = electric.getAccountPeriod();
            String customer =electric.getCustomer();
            String electricKey = region + "-" + accountPeriod + "-" + customer;
            Double taxMoney = electric.getSettlement();
            if (electricMap.containsKey(electricKey)){
                electricMap.put(electricKey,taxMoney+electricMap.get(electricKey));
            }else {
                electricMap.put(electricKey,taxMoney);
            }
        }

        rebackStatWithReportDao.truncate();

        for (Map.Entry<String, Double> entry : verifyMap.entrySet()) {


            String[] split = entry.getKey().split("-");
            Double taxMoney = entry.getValue();
            RebackStatWithReport rebackStatWithReport = new RebackStatWithReport();

            rebackStatWithReport.setRegion(split[0]);
            rebackStatWithReport.setAccountPeriod(split[1]);
            rebackStatWithReport.setCustomer(split[2]);
            rebackStatWithReport.setVerifyMoney(MyUtils.to2Round(taxMoney));



            if (electricMap.containsKey(entry.getKey())){
                Double difference = electricMap.get(entry.getKey()) -taxMoney;
                rebackStatWithReport.setRebackMoney(MyUtils.to2Round(electricMap.get(entry.getKey())));
                rebackStatWithReport.setDifference(difference);
            }else {
                rebackStatWithReport.setRebackMoney(0.0);
                rebackStatWithReport.setDifference(taxMoney);
            }

            rebackStatWithReport.setStatDate(MyUtils.getExcelDate(new Date()));
            rebackStatWithReportDao.insertSelective(rebackStatWithReport);
        }
        System.out.println("支付-回款报表统计完成");
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
            Double notaxMoney = electric.getSettlement();
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
            rebackStat.setVerifyMoney(MyUtils.to2Round(statTemp.getTaxMoney()));



            if (electricMap.containsKey(entry.getKey())){
                Double difference = electricMap.get(entry.getKey()) - statTemp.getTaxMoney();
                rebackStat.setRebackMoney(MyUtils.to2Round(electricMap.get(entry.getKey())));
                rebackStat.setDifference(Math.abs(difference));
            }else {
                rebackStat.setRebackMoney(0.0);
                rebackStat.setDifference(statTemp.getTaxMoney());
            }

            NameCode nameCode = nameCodeDao.selectByPrimaryKey(rebackStat.getSiteCode());
            if (nameCode != null){
                rebackStat.setSiteName(nameCode.getSiteName());
            }
            rebackStat.setStatDate(MyUtils.getExcelDate(new Date()));
            rebackStatDao.insertSelective(rebackStat);
        }
        System.out.println("支付-回款按明细统计完成");
    }

    @Transactional
    public void rebackStatWithCpy(){
        System.out.println("开始统计");
        Map<String,StatTempWithCpy> verifyMap = new HashMap<>();
        Example example = new Example(Electric.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("settlementModel","包干");
        List<Electric> electrics = electricDao.selectByExample(example);
        for (Electric electric : electrics) {
            String region = electric.getRegion();
            String accountPeriod = electric.getAccountPeriod();
            String siteCode = electric.getSiteCode();
            Double taxMoney = electric.getSettlement();
            StatTempWithCpy statTempWithCpy = new StatTempWithCpy(region,accountPeriod,taxMoney);
            if (verifyMap.containsKey(siteCode)){
                taxMoney = taxMoney + verifyMap.get(siteCode).getNotaxMoney();
                statTempWithCpy.setNotaxMoney(taxMoney);
                String accountPeriodOld = verifyMap.get(siteCode).getAccountPeriod();
                if (NumberUtils.toDouble(accountPeriod)<NumberUtils.toDouble(accountPeriodOld)){
                    statTempWithCpy.setAccountPeriod(accountPeriodOld);
                }
                verifyMap.put(siteCode,statTempWithCpy);
            }else {
                verifyMap.put(siteCode,statTempWithCpy);
            }
        }

        Map<String,Double> electricMap = new HashMap<>();
        List<IncomeCpy> incomeCpies = cpyDao.selectAll();
        for (IncomeCpy incomeCpy : incomeCpies) {

            String siteCode = incomeCpy.getSiteCode();

            Double notaxMoney = incomeCpy.getNotaxMoney();
            if (electricMap.containsKey(siteCode)){
                electricMap.put(siteCode,notaxMoney+electricMap.get(siteCode));
            }else {
                electricMap.put(siteCode,notaxMoney);
            }
        }

        rebackStatWithReportDao.truncate();

        for (Map.Entry<String, StatTempWithCpy> entry : verifyMap.entrySet()) {



            StatTempWithCpy statTempWithCpy = entry.getValue();
            RebackStatWithCpy rebackStatWithCpy = new RebackStatWithCpy();

            rebackStatWithCpy.setSiteCode(entry.getKey());
            rebackStatWithCpy.setRegion(statTempWithCpy.getRegion());
            rebackStatWithCpy.setPayDate(statTempWithCpy.getAccountPeriod());
            rebackStatWithCpy.setVerifyMoney(MyUtils.to2Round(statTempWithCpy.getNotaxMoney()));


            Double notaxMoney = statTempWithCpy.getNotaxMoney();
            if (electricMap.containsKey(entry.getKey())){

                Double difference = electricMap.get(entry.getKey()) -notaxMoney;
                rebackStatWithCpy.setRebackMoney(MyUtils.to2Round(electricMap.get(entry.getKey())));
                rebackStatWithCpy.setDifference(difference);
            }else {
                rebackStatWithCpy.setRebackMoney(0.0);
                rebackStatWithCpy.setDifference(notaxMoney);
            }
            NameCode nameCode = nameCodeDao.selectByPrimaryKey(rebackStatWithCpy.getSiteCode());
            if (nameCode != null){
                rebackStatWithCpy.setSiteName(nameCode.getSiteName());
            }
            rebackStatWithCpy.setStatDate(MyUtils.getExcelDate(new Date()));
            rebackStatWithCpyDao.insertSelective(rebackStatWithCpy);
        }
        System.out.println("包干收支统计完成");
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

    public List<RebackStatWithReport> findRebackStatWithReport() throws ParseException {


        return rebackStatWithReportDao.selectAll();

    }
}



