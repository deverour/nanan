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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@RestController
@RequestMapping("/reback")
public class RebackController {

    @Autowired
    private RebackService rebackService;
    

    @RequestMapping("/query")
    public PageResult query(@RequestBody RebackQueryBean rebackQueryBean, HttpSession httpSession) {
        try {
            User user = (User)httpSession.getAttribute("user");

            PageResult pageResult = rebackService.pageQuery(rebackQueryBean, user);

            return pageResult;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new PageResult(0l,new ArrayList());
    }

    @RequestMapping("mark")
    public Result mark(@RequestParam("id") Integer id,HttpSession httpSession){
        try {
            return rebackService.mark(id);

        }catch (Exception e){
            e.printStackTrace();
        }
        return new Result(false,"打标失败");

    }

    @RequestMapping("delete")
    public Result delete(@RequestParam("id") Integer id,HttpSession httpSession){
        try {
             rebackService.delete(id);
             return new Result(true,"删除成功");

        }catch (Exception e){
            e.printStackTrace();
        }
        return new Result(false,"删除失败，请稍后重试");
    }



}
