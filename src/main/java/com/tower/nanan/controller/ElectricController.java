package com.tower.nanan.controller;


import com.tower.nanan.entity.FilePath;
import com.tower.nanan.entity.Result;
import com.tower.nanan.poi.ExcelRead;
import com.tower.nanan.pojo.User;
import com.tower.nanan.service.ElectricService;
import com.tower.nanan.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/electric")
public class ElectricController {

    @Autowired
    private ElectricService electricService;

    @PostMapping("/upload")
    public Result upload(@RequestParam("electricFile")MultipartFile multipartFile, HttpSession httpSession){
        try {
            User user = (User) httpSession.getAttribute("user");
            String path = FilePath.UPLOAD_TEMP;
            String uuid = UUID.randomUUID().toString().replace("-", "");
            String filename = uuid+ MyUtils.getRealName(multipartFile.getOriginalFilename());
            File file = new File(path,filename);
            if(!file.exists()){
                file.mkdir();
            }
            multipartFile.transferTo(file);
            String message = electricService.saveElectrics(file);

            if (message.length()==0){
                return new Result(true,"签认明细上传成功");
            }
            return new Result(false,message);

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"读取表格失败,请检查导入表模板后重试");
        }
    }
}
