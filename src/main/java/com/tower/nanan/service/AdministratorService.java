package com.tower.nanan.service;

import com.tower.nanan.dao.AdministratorDao;
import com.tower.nanan.entity.Cache;
import com.tower.nanan.pojo.Administrator;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class AdministratorService implements InitializingBean {
    @Autowired
    private AdministratorDao administratorDao;


    @Override
    public void afterPropertiesSet() throws Exception {
        List<Administrator> administrators = administratorDao.selectAll();
        for (Administrator administrator : administrators) {
            if (administrator.getFields().equals("switch")){
                String values = administrator.getValue();
                if (values.equals("1")){
                    Cache.switchs = true;
                }else {
                    Cache.switchs = false;
                }
            }
        }
    }



    public void turnOn(){
        Cache.switchs = true;
        Example example = new Example(Administrator.class);
        Example.Criteria criteria = example.createCriteria();
        Administrator administrator = new Administrator();
        administrator.setFields("switch");
        administrator.setValue("1");
        criteria.andEqualTo("fields","switch");
        administratorDao.updateByExampleSelective(administrator,example);
        Cache.switchs = true;

    }


    public void turnOff(){
        Cache.switchs = true;
        Example example = new Example(Administrator.class);
        Example.Criteria criteria = example.createCriteria();
        Administrator administrator = new Administrator();
        administrator.setFields("switch");
        administrator.setValue("0");
        criteria.andEqualTo("fields","switch");
        administratorDao.updateByExampleSelective(administrator,example);
        Cache.switchs = false;

    }



}
