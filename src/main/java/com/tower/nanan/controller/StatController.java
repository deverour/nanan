package com.tower.nanan.controller;


import com.tower.nanan.entity.ElectricQueryBean;
import com.tower.nanan.entity.RebackStatQueryBean;
import com.tower.nanan.poi.ExcelWrite;
import com.tower.nanan.pojo.RebackStat;
import com.tower.nanan.pojo.RebackStatWithCustomer;
import com.tower.nanan.pojo.RebackStatWithSite;
import com.tower.nanan.pojo.User;
import com.tower.nanan.service.StatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("rebackstat")
public class StatController {

    @Autowired
    private StatService statService;

    @RequestMapping("/exportforcustomer")
    public ResponseEntity<byte[]> exportForRebackStatWithCustomer(@RequestBody RebackStatQueryBean rebackStatQueryBean, HttpSession httpSession){
        try {
            User user = (User) httpSession.getAttribute("user");
            if (!user.getNgroup().equals("admin")){
                return null;
            }
            List<RebackStatWithCustomer> rebackStatWithCustomers = statService.findRebackStatWithCustomerByCondition(rebackStatQueryBean);
            InputStream is = ExcelWrite.WriteRebackStatWithCustomer(rebackStatWithCustomers);
            byte[] body = new byte[is.available()];
            is.read(body);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Content-Disposition", "attchement;filename=" + URLEncoder.encode("回款统计表","UTF-8")+".xlsx");
            HttpStatus status = HttpStatus.OK;
            ResponseEntity<byte[]> entity = new ResponseEntity<>(body,httpHeaders,status);
            System.out.println("查询成功,开始下载");
            return entity;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping("/exportforsite")
    public ResponseEntity<byte[]> exportForRebackStatWithSite(@RequestBody RebackStatQueryBean rebackStatQueryBean, HttpSession httpSession){
        try {
            User user = (User) httpSession.getAttribute("user");
            if (!user.getNgroup().equals("admin")){
                return null;
            }
            List<RebackStatWithSite> rebackStatWithSites = statService.findRebackStatWithSiteByCondition(rebackStatQueryBean);
            InputStream is = ExcelWrite.WriteRebackStatWithSite(rebackStatWithSites);
            byte[] body = new byte[is.available()];
            is.read(body);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Content-Disposition", "attchement;filename=" + URLEncoder.encode("回款统计表","UTF-8")+".xlsx");
            HttpStatus status = HttpStatus.OK;
            ResponseEntity<byte[]> entity = new ResponseEntity<>(body,httpHeaders,status);
            System.out.println("查询成功,开始下载");
            return entity;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @RequestMapping("/export")
    public ResponseEntity<byte[]> exportForRebackStat(@RequestBody RebackStatQueryBean rebackStatQueryBean, HttpSession httpSession){
        try {
            User user = (User) httpSession.getAttribute("user");
            if (!user.getNgroup().equals("admin")){
                return null;
            }
            List<RebackStat> rebackStats = statService.findRebackStatByCondition(rebackStatQueryBean);
            InputStream is = ExcelWrite.WriteRebackStats(rebackStats);
            byte[] body = new byte[is.available()];
            is.read(body);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Content-Disposition", "attchement;filename=" + URLEncoder.encode("回款统计表","UTF-8")+".xlsx");
            HttpStatus status = HttpStatus.OK;
            ResponseEntity<byte[]> entity = new ResponseEntity<>(body,httpHeaders,status);
            System.out.println("查询成功,开始下载");
            return entity;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
