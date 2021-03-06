package com.tower.nanan.controller;


import com.tower.nanan.entity.*;
import com.tower.nanan.poi.ExcelWrite;
import com.tower.nanan.pojo.Electric;
import com.tower.nanan.pojo.User;
import com.tower.nanan.service.ElectricService;
import com.tower.nanan.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/electric")
public class ElectricController {

    @Autowired
    private ElectricService electricService;

    @RequestMapping("/upload")
    public Result upload(@RequestParam("electricFile") MultipartFile multipartFile, HttpSession httpSession){
        if (!Cache.switchs){
            return null;
        }
        try {
            System.out.println("---------------------------");
            System.out.println("电费明细上传   |   " + MyUtils.getnowtime() );
            User user = (User) httpSession.getAttribute("user");
            String path = FilePath.UPLOAD_TEMP;
            String uuid = UUID.randomUUID().toString().replace("-", "");
            String filename = uuid+ MyUtils.getRealName(multipartFile.getOriginalFilename());
            File file = new File(path,filename);
            if(!file.exists()){
                file.mkdir();
            }
            multipartFile.transferTo(file);
            return electricService.saveElectrics(file,user);

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"读取表格失败,请检查导入表模板后重试");
        }
    }

    @RequestMapping("update")
    public Result update(@RequestParam("electricFile") MultipartFile multipartFile,HttpSession httpSession){
        if (!Cache.switchs){
            return null;
        }
        try {
            System.out.println("---------------------------");
            System.out.println("电费明细补录   |   " + MyUtils.getnowtime() );
            User user = (User) httpSession.getAttribute("user");

            String path = FilePath.UPLOAD_TEMP;
            String uuid = UUID.randomUUID().toString().replace("-", "");
            String filename = uuid+ MyUtils.getRealName(multipartFile.getOriginalFilename());
            File file = new File(path,filename);
            if(!file.exists()){
                file.mkdir();
            }
            multipartFile.transferTo(file);
            electricService.update(file,user);
            return new Result(true,"核销单号补录成功");

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,e.getMessage());
        }
    }
    @RequestMapping("/export")
    public ResponseEntity<byte[]> export(@RequestBody ElectricQueryBean electricQueryBean,HttpSession httpSession){
        if (!Cache.switchs){
            return null;
        }
        try {
            System.out.println("---------------------------");
            System.out.println("电费明细导出   |   " + MyUtils.getnowtime() );
            User user = (User)httpSession.getAttribute("user");
            List<Electric> electrics = electricService.findByCondition(electricQueryBean, user);
            InputStream is = ExcelWrite.WriteElectics(electrics);
            byte[] body = new byte[is.available()];
            is.read(body);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Content-Disposition", "attchement;filename=" + URLEncoder.encode("代垫签认明细","UTF-8")+".xlsx");
            HttpStatus status = HttpStatus.OK;
            ResponseEntity<byte[]> entity = new ResponseEntity<>(body,httpHeaders,status);

            return entity;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping("/query")
    public PageResult query(@RequestBody ElectricQueryBean electricQueryBean,HttpSession httpSession) {
        if (!Cache.switchs){
            return null;
        }
        try {
            System.out.println("---------------------------");
            System.out.println("电费明细查询   |   " + MyUtils.getnowtime() );
            System.out.println("第"+electricQueryBean.getCurrentPage()+"页");
            User user = (User)httpSession.getAttribute("user");
            return electricService.pageQuery(electricQueryBean,user);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new PageResult(0l,new ArrayList());

    }
}
