package com.tower.nanan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.tower.nanan.dao")
@EnableScheduling//开启定时器
public class NananApplication {

    public static void main(String[] args) {
        SpringApplication.run(NananApplication.class, args);
    }

}
