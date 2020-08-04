package com.tower.nanan.controller;


import com.tower.nanan.entity.*;
import com.tower.nanan.poi.ExcelWrite;
import com.tower.nanan.pojo.Electric;
import com.tower.nanan.pojo.User;
import com.tower.nanan.service.CpyService;
import com.tower.nanan.service.ElectricService;
import com.tower.nanan.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cpy")
public class CpyController {

    @Autowired
    private CpyService cpyService;

    @RequestMapping("/costupload")
    public Result upload(@RequestParam("cpycostFile") MultipartFile multipartFile, HttpSession httpSession){
        if (!Cache.switchs){
            return null;
        }
        try {
            System.out.println("---------------------------");
            System.out.println("包干成本上传   |   " + MyUtils.getnowtime() );
            User user = (User) httpSession.getAttribute("user");
            if (!user.getNgroup().equals("admin")){
                return new Result(false,"对不起,你没有导入包干明细的权限");
            }
            String path = FilePath.UPLOAD_TEMP;
            String uuid = UUID.randomUUID().toString().replace("-", "");
            String filename = uuid+ MyUtils.getRealName(multipartFile.getOriginalFilename());
            File file = new File(path,filename);
            if(!file.exists()){
                file.mkdir();
            }
            multipartFile.transferTo(file);
            return cpyService.saveCostCpys(file,user);

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"读取表格失败,请检查导入表模板后重试");
        }
    }

    @RequestMapping("/incomeupload")
    public Result incomeupload(@RequestParam("cpyincomeFile") MultipartFile multipartFile, HttpSession httpSession){
        if (!Cache.switchs){
            return null;
        }
        try {
            System.out.println("---------------------------");
            System.out.println("包干收入上传   |   " + MyUtils.getnowtime() );
            User user = (User) httpSession.getAttribute("user");
            if (!user.getNgroup().equals("admin")){
                return new Result(false,"对不起,你没有导入包干明细的权限");
            }
            String path = FilePath.UPLOAD_TEMP;
            String uuid = UUID.randomUUID().toString().replace("-", "");
            String filename = uuid+ MyUtils.getRealName(multipartFile.getOriginalFilename());
            File file = new File(path,filename);
            if(!file.exists()){
                file.mkdir();
            }
            multipartFile.transferTo(file);
            return cpyService.saveIncomeCpys(file,user);

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"读取表格失败,请检查导入表模板后重试");
        }
    }


}
