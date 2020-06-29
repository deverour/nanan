package com.tower.nanan.controller;

import com.tower.nanan.entity.FilePath;
import com.tower.nanan.entity.Result;
import com.tower.nanan.pojo.User;
import com.tower.nanan.service.VerifyService;
import com.tower.nanan.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/verify")
public class VerifyController {

    @Autowired
    private VerifyService verifyService;

    public Result upload(@RequestParam("verifyFile") MultipartFile multipartFile, HttpSession httpSession) {
        System.out.println("1111111111111");
        try {
            //User user = (User) httpSession.getAttribute("user");
            User user = new User();
            user.setRegion("南岸");
            String path = FilePath.UPLOAD_TEMP;
            String uuid = UUID.randomUUID().toString().replace("-", "");
            String filename = uuid + MyUtils.getRealName(multipartFile.getOriginalFilename());
            File file = new File(path, filename);
            if (!file.exists()) {
                file.mkdir();
            }
            multipartFile.transferTo(file);
            verifyService.saveVerify(file, user);
            return new Result(true,"支付/核销明细导入成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"读取表格失败,请检查导入表模板后重试");
        }
    }

}
