package com.tower.nanan.controller;


import com.tower.nanan.entity.Cache;
import com.tower.nanan.entity.FilePath;
import com.tower.nanan.entity.Result;
import com.tower.nanan.pojo.User;
import com.tower.nanan.service.NameCodeService;
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
@RequestMapping("/namecode")
public class NameCodeController {

    @Autowired
    private NameCodeService nameCodeService;

    @RequestMapping("/upload")
    public Result upload(@RequestParam("namecodeFile") MultipartFile multipartFile, HttpSession httpSession){
        if (!Cache.switchs){
            return null;
        }
        try {
            System.out.println("---------------------------");
            System.out.println("站址站名映射上传 |   " + MyUtils.getnowtime() );
            User user = (User) httpSession.getAttribute("user");
            String path = FilePath.UPLOAD_TEMP;
            String uuid = UUID.randomUUID().toString().replace("-", "");
            String filename = uuid+ MyUtils.getRealName(multipartFile.getOriginalFilename());
            File file = new File(path,filename);
            if(!file.exists()){
                file.mkdir();
            }
            multipartFile.transferTo(file);
            return nameCodeService.saveNameCode(file);

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"读取表格失败,请检查导入表模板后重试");
        }
    }

}
