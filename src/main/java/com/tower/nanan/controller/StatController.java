package com.tower.nanan.controller;


import com.tower.nanan.entity.ElectricQueryBean;
import com.tower.nanan.poi.ExcelWrite;
import com.tower.nanan.pojo.RebackStatWithCustomer;
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
import java.util.List;

@RestController
@RequestMapping("rebackstat")
public class StatController {

    @Autowired
    private StatService statService;

    @RequestMapping("/export")
    public ResponseEntity<byte[]> export(@RequestBody ElectricQueryBean electricQueryBean, HttpSession httpSession){
        try {
            User user = (User)httpSession.getAttribute("user");
            List<RebackStatWithCustomer> rebackStatWithCustomers = statService.findRebackStat();
            InputStream is = ExcelWrite.WriteRebackStats(rebackStatWithCustomers);
            byte[] body = new byte[is.available()];
            is.read(body);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Content-Disposition", "attchement;filename=" + URLEncoder.encode("回款统计表","UTF-8")+".xlsx");
            HttpStatus status = HttpStatus.OK;
            ResponseEntity<byte[]> entity = new ResponseEntity<>(body,httpHeaders,status);
            System.out.println("查询成功,开始下载");
            return entity;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
