package com.tower.nanan.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tower.nanan.dao.RebackDao;
import com.tower.nanan.entity.PageResult;
import com.tower.nanan.entity.RebackQueryBean;
import com.tower.nanan.pojo.Reback;
import com.tower.nanan.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Set;

@Service
public class RebackService {
    @Autowired
    private RebackDao rebackDao;

    public void saveReback(Reback reback){
        rebackDao.insertSelective(reback);

    }

    public Set<String> getRebackCodeSet(){
        return rebackDao.getRebackCodeSet();
    }

    public PageResult pageQuery(RebackQueryBean rebackQueryBean, User user) {
        Example example = new Example(Reback.class);
        Example.Criteria criteria = example.createCriteria();
        if (rebackQueryBean.getRegion() != null && !rebackQueryBean.getRegion().isEmpty()){
            for (String region : rebackQueryBean.getRegion()) {
                criteria.orEqualTo("region",region);
            }
        }

        if (rebackQueryBean.getCustomer() != null && !rebackQueryBean.getCustomer().isEmpty()){
            for (String customer : rebackQueryBean.getCustomer()) {
                criteria.orEqualTo("customer",customer);
            }
        }

        if (rebackQueryBean.getRebacked() != null && !rebackQueryBean.getRebacked().isEmpty()){
            String rebacked = rebackQueryBean.getRebacked();
            criteria.andEqualTo("rebacked",rebacked);
        }

        if (rebackQueryBean.getRebackCode() != null && !rebackQueryBean.getRebackCode().isEmpty()){
            String rebackCode = rebackQueryBean.getRebackCode();
            criteria.andEqualTo("reback_code",rebackCode);
        }
        if (rebackQueryBean.getCurrentPage() != null && rebackQueryBean.getPageSize() != null) {
            PageHelper.startPage(rebackQueryBean.getCurrentPage(), rebackQueryBean.getPageSize());
        }
        Page<Reback> pageData = (Page<Reback>) rebackDao.selectByExample(example);
        System.out.println("pageData.getTotal()"+pageData.getTotal());
        System.out.println("pageData.getResult()"+pageData.getResult());
        return new PageResult(pageData.getTotal(),pageData.getResult());

    }
}
