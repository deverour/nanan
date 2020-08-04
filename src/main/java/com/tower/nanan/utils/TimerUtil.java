package com.tower.nanan.utils;

import com.tower.nanan.entity.Cache;
import com.tower.nanan.service.StatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class TimerUtil {

    @Autowired
    private StatService statService;
    @Scheduled(cron = "0 0 1 * * ?")
    public void mytask(){
        if (Cache.switchs){
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            System.out.println("开始定时统计    |   " +formatter.format(new Date()) );
            System.out.println("---------------------------");
            System.out.println("收支按明细统计  |   " +formatter.format(new Date()) );
            statService.rebackStat();
            System.out.println("---------------------------");
            System.out.println("收支按站址统计  |   " +formatter.format(new Date()) );
            statService.rebackStatForSite();
            System.out.println("---------------------------");
            System.out.println("收支按客户统计  |   " +formatter.format(new Date()) );
            statService.rebackStatForCustomer();
            System.out.println("---------------------------");
            System.out.println("收支按报表统计  |   " +formatter.format(new Date()) );
            statService.rebackStatWithReport();
            System.out.println("---------------------------");
        }


    }

}
