package com.tower.nanan.controller;

import com.tower.nanan.entity.FilePath;
import com.tower.nanan.entity.Result;
import com.tower.nanan.poi.ExcelWrite;
import com.tower.nanan.pojo.Percentage;
import com.tower.nanan.pojo.User;
import com.tower.nanan.pojo.Verify;
import com.tower.nanan.service.PercentageService;
import com.tower.nanan.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/percentage")
public class PercentageController {

    @Autowired
    private PercentageService percentageService;

    @RequestMapping("upload")
    public Result upload(@RequestParam("percentageFile") MultipartFile multipartFile, HttpSession httpSession) {

        try {
            User user = (User) httpSession.getAttribute("user");
            if (!user.getNgroup().equals("admin")){
                return new Result(false,"对不起,你没有导入分摊比例明细的权限");
            }


            String path = FilePath.UPLOAD_TEMP;
            String uuid = UUID.randomUUID().toString().replace("-", "");
            String filename = uuid + MyUtils.getRealName(multipartFile.getOriginalFilename());
            File file = new File(path, filename);
            if (!file.exists()) {
                file.mkdir();
            }
            multipartFile.transferTo(file);
            return percentageService.savePercentage(file);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"读取表格失败,请检查导入表模板后重试");
        }
    }

    @RequestMapping("/export")
    public ResponseEntity<byte[]> export(@RequestParam("siteCode") String siteCode, HttpSession httpSession){
        try {
            User user = (User) httpSession.getAttribute("user");
            if (!user.getNgroup().equals("admin")){
                return null;
            }
            List<Percentage> percentages = percentageService.findBySiteCode(siteCode);
            InputStream is = ExcelWrite.WritePercentages(percentages);
            byte[] body = new byte[is.available()];
            is.read(body);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Content-Disposition", "attchement;filename=" + URLEncoder.encode("代垫签认明细","UTF-8")+".xlsx");
            HttpStatus status = HttpStatus.OK;
            ResponseEntity<byte[]> entity = new ResponseEntity<>(body,httpHeaders,status);
            is.close();
            System.out.println("查询成功,开始下载");
            return entity;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



}
