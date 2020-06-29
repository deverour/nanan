package com.tower.nanan.controller;

import com.tower.nanan.entity.ElectricQueryBean;
import com.tower.nanan.entity.PageResult;
import com.tower.nanan.entity.RebackQueryBean;
import com.tower.nanan.entity.Result;
import com.tower.nanan.pojo.User;
import com.tower.nanan.service.RebackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@RestController
@RequestMapping("/reback")
public class RebackController {

    @Autowired
    private RebackService rebackService;
    

    @PostMapping("/findPage")
    public PageResult findPage(@RequestBody RebackQueryBean rebackQueryBean, HttpSession httpSession) {
        try {
            User user = (User)httpSession.getAttribute("user");

            PageResult pageResult = rebackService.pageQuery(rebackQueryBean, user);
            System.out.println("开始返回");
            return pageResult;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new PageResult(0l,new ArrayList());

    }
}
