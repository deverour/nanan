package com.tower.nanan.service;

import com.tower.nanan.dao.ElectricDao;
import com.tower.nanan.dao.RebackStatDao;
import com.tower.nanan.dao.VerifyDao;
import com.tower.nanan.pojo.Electric;
import com.tower.nanan.pojo.RebackStat;
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
    private RebackStatDao rebackStatDao;


    @Transactional
    public void rebackstat(){
        Map<String,Double> verifyMap = new HashMap<>();
        List<Verify> verifies = verifyDao.selectAll();
        for (Verify verify : verifies) {
            String verifyCode = verify.getVerifyCode();
            String customer = verify.getCustomer();
            String verifyKey = verifyCode + "-" +customer;
            Double notaxMoney = NumberUtils.toDouble(verify.getTaxMoney());
            if (verifyMap.containsKey(verifyKey)){
                verifyMap.put(verifyKey,notaxMoney+verifyMap.get(verifyKey));
            }else {
                verifyMap.put(verifyKey,notaxMoney);
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

        rebackStatDao.truncate();

        for (Map.Entry<String, Double> entry : verifyMap.entrySet()) {
            System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
            String[] split = entry.getKey().split("-");

            RebackStat rebackStat = new RebackStat();

            rebackStat.setVerifyCode(split[0]);
            rebackStat.setCustomer(split[1]);
            rebackStat.setVerifyMoney(String.valueOf(MyUtils.to2Round(entry.getValue())));

            if (electricMap.containsKey(entry.getKey())){
                rebackStat.setRebackMoney(String.valueOf(MyUtils.to2Round(electricMap.get(entry.getKey()))));
            }

            rebackStat.setStatDate(MyUtils.getExcelDate(new Date()));

            rebackStatDao.insertSelective(rebackStat);
        }
    }


    public List<RebackStat> findRebackStat() {
        return rebackStatDao.selectAll();
    }
}



