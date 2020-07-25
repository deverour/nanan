package com.tower.nanan.controller;

import com.tower.nanan.service.StatService;
import com.tower.nanan.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/test")
public class testController {
    @Autowired
    private StatService statService;

    @RequestMapping("/1")
    public void test1(){
        System.out.println("---------------------------");
        System.out.println("开始定时统计    |   " + MyUtils.getnowtime() );
        System.out.println("---------------------------");
        System.out.println("收支按明细统计  |   " + MyUtils.getnowtime() );
        long l1 = System.currentTimeMillis();
        statService.rebackStat();
        long l2 = System.currentTimeMillis();
        System.out.println("完成            |    耗时"+(l2-l1)/1000+"秒");
        System.out.println("---------------------------");
        System.out.println("收支按站址统计  |   " + MyUtils.getnowtime() );
        l1 = System.currentTimeMillis();
        statService.rebackStatForSite();
        l2 = System.currentTimeMillis();
        System.out.println("完成            |    耗时"+(l2-l1)/1000+"秒");
        System.out.println("---------------------------");
        System.out.println("收支按客户统计  |   " + MyUtils.getnowtime() );
        l1 = System.currentTimeMillis();
        statService.rebackStatForCustomer();
        l2 = System.currentTimeMillis();
        System.out.println("完成            |    耗时"+(l2-l1)/1000+"秒");
        System.out.println("---------------------------");
        System.out.println("收支按报表统计  |   " + MyUtils.getnowtime() );
        l1 = System.currentTimeMillis();
        statService.rebackStatWithReport();
        l2 = System.currentTimeMillis();
        System.out.println("完成            |    耗时"+(l2-l1)/1000+"秒");
    }
}
