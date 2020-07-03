package com.tower.nanan.utils;

import com.tower.nanan.service.StatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TimerUtil {

    @Autowired
    private StatService statService;
    @Scheduled(cron = "*1**?")
    public void mytask(){
        statService.rebackstat();

    }

}