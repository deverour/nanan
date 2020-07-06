package com.tower.nanan.controller;

import com.tower.nanan.entity.ElectricQueryBean;
import com.tower.nanan.entity.PageResult;
import com.tower.nanan.entity.RebackQueryBean;
import com.tower.nanan.entity.Result;
import com.tower.nanan.poi.ExcelWrite;
import com.tower.nanan.pojo.Electric;
import com.tower.nanan.pojo.Reback;
import com.tower.nanan.pojo.User;
import com.tower.nanan.pojo.Verify;
import com.tower.nanan.service.RebackService;
import com.tower.nanan.service.VerifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/reback")
public class RebackController {

    @Autowired
    private RebackService rebackService;

    @Autowired
    private VerifyService verifyService;
    

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

    @RequestMapping("/export")
    public ResponseEntity<byte[]> export(@RequestBody RebackQueryBean rebackQueryBean,HttpSession httpSession){
        try {
            User user = (User)httpSession.getAttribute("user");
            List<Reback> rebacks = rebackService.findByCondition(rebackQueryBean, user);
            InputStream is = ExcelWrite.WriteRebacks(rebacks);

            byte[] body = new byte[is.available()];
            is.read(body);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Content-Disposition", "attchement;filename=" + URLEncoder.encode("代垫签认明细","UTF-8")+".xlsx");
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
