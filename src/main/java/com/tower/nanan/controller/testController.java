package com.tower.nanan.controller;

import com.tower.nanan.service.StatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class testController {
    @Autowired
    private StatService statService;

    @RequestMapping("/1")
    public void test1(){

        //statService.rebackStatForSite();
        //statService.rebackStat();
        //statService.rebackStatForCustomer();
        statService.rebackStatWithReport();
        //statService.rebackStatWithCpy();
    }
}
