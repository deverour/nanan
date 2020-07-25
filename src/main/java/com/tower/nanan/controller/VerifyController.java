package com.tower.nanan.controller;

import com.tower.nanan.entity.ElectricQueryBean;
import com.tower.nanan.entity.FilePath;
import com.tower.nanan.entity.Result;
import com.tower.nanan.poi.ExcelWrite;
import com.tower.nanan.pojo.Electric;
import com.tower.nanan.pojo.User;
import com.tower.nanan.pojo.Verify;
import com.tower.nanan.service.VerifyService;
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
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/verify")
public class VerifyController {

    @Autowired
    private VerifyService verifyService;

    @RequestMapping("upload")
    public Result upload(@RequestParam("verifyFile") MultipartFile multipartFile, HttpSession httpSession) {
        try {
            System.out.println("---------------------------");
            System.out.println("核销明细上传   |   " + MyUtils.getnowtime() );
            User user = (User) httpSession.getAttribute("user");
            if (!user.getNgroup().equals("admin")){
                return new Result(false,"对不起,你没有核销明细的权限");
            }

            String path = FilePath.UPLOAD_TEMP;
            String uuid = UUID.randomUUID().toString().replace("-", "");
            String filename = uuid + MyUtils.getRealName(multipartFile.getOriginalFilename());
            File file = new File(path, filename);
            if (!file.exists()) {
                file.mkdir();
            }
            multipartFile.transferTo(file);
            return verifyService.saveVerify(file, user);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"读取表格失败,请检查导入表模板后重试");
        }
    }

    @RequestMapping("export")
    public ResponseEntity<byte[]> export( HttpSession httpSession){
        try {
            System.out.println("---------------------------");
            System.out.println("核销明细导出   |   " + MyUtils.getnowtime() );
            User user = (User)httpSession.getAttribute("user");
            List<Verify> verifies = verifyService.findAll();
            InputStream is = ExcelWrite.WriteVerifies(verifies);
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
