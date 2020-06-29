package com.tower.nanan.controller;

import com.tower.nanan.entity.FilePath;
import com.tower.nanan.entity.Result;
import com.tower.nanan.pojo.User;
import com.tower.nanan.service.PercentageService;
import com.tower.nanan.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.UUID;

@RestController
@RequestMapping("/percentage")
public class PercentageController {

    @Autowired
    private PercentageService percentageService;

    public Result upload(@RequestParam("verifyFile") MultipartFile multipartFile, HttpSession httpSession) {

        try {
            User user = (User) httpSession.getAttribute("user");
            if (!user.getGroup().equals("admin")){
                return new Result(false,"对不起，你的账号没有该权限");
            }

            String path = FilePath.UPLOAD_TEMP;
            String uuid = UUID.randomUUID().toString().replace("-", "");
            String filename = uuid + MyUtils.getRealName(multipartFile.getOriginalFilename());
            File file = new File(path, filename);
            if (!file.exists()) {
                file.mkdir();
            }
            multipartFile.transferTo(file);
            percentageService.savePercentage(file);
            return new Result(true,"物业系统分摊比例导入成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"读取表格失败,请检查导入表模板后重试");
        }
    }

}
