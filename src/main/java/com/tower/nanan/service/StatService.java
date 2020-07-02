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
            Double notaxMoney = NumberUtils.toDouble(verify.getNotaxMoney());
            if (verifyMap.containsKey(verifyCode)){
                verifyMap.put(verifyCode,notaxMoney+verifyMap.get(verifyCode));
            }else {
                verifyMap.put(verifyCode,notaxMoney);
            }
        }

        Map<String,Double> electricMap = new HashMap<>();
        List<Electric> electrics = electricDao.selectAll();
        for (Electric electric : electrics) {
            String verifyCode = electric.getVerifyCode();
            Double notaxMoney = NumberUtils.toDouble(electric.getNotaxMoney());
            if (electricMap.containsKey(verifyCode)){
                electricMap.put(verifyCode,notaxMoney+electricMap.get(verifyCode));
            }else {
                electricMap.put(verifyCode,notaxMoney);
            }
        }

        rebackStatDao.truncate();

        for (Map.Entry<String, Double> entry : verifyMap.entrySet()) {
            System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
            RebackStat rebackStat = new RebackStat();
            rebackStat.setVerifyCode(entry.getKey());
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



