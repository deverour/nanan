package com.tower.nanan.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tower.nanan.dao.ElectricDao;
import com.tower.nanan.dao.RebackDao;
import com.tower.nanan.entity.*;
import com.tower.nanan.pojo.Electric;
import com.tower.nanan.pojo.Reback;
import com.tower.nanan.pojo.User;
import com.tower.nanan.utils.MyUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class RebackService implements InitializingBean {
    @Autowired
    private RebackDao rebackDao;

    @Autowired
    private ElectricDao electricDao;

    public void saveReback(Reback reback){
        rebackDao.insertSelective(reback);

    }


    public List<Reback> findByCondition(RebackQueryBean rebackQueryBean, User user) {
        System.out.println(rebackQueryBean);
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
        if (rebackQueryBean.getRebackCode() != null && !rebackQueryBean.getRebackCode().isEmpty()){
            String rebackCode = rebackQueryBean.getRebackCode();
            criteria.andEqualTo("rebackCode",rebackCode);
        }
        if (rebackQueryBean.getStartAccountPeriod() != null && !rebackQueryBean.getStartAccountPeriod().isEmpty()){
            String startAccountPeriod = rebackQueryBean.getStartAccountPeriod();
            criteria.andCondition("account_period >=",startAccountPeriod);
        }

        if (rebackQueryBean.getEndAccountPeriod() != null && !rebackQueryBean.getEndAccountPeriod().isEmpty()){
            String endAccountPeriod = rebackQueryBean.getEndAccountPeriod();
            criteria.andCondition("account_period <=",endAccountPeriod);
        }
        if (rebackQueryBean.getStartRebackDate() != null && !rebackQueryBean.getStartRebackDate().isEmpty()){
            String startRebackDate = rebackQueryBean.getStartRebackDate();
            criteria.andCondition("reback_date >=",startRebackDate);
        }

        if (rebackQueryBean.getEndRebackDate() != null && !rebackQueryBean.getEndRebackDate().isEmpty()){
            String endRebackDate = rebackQueryBean.getEndRebackDate();
            criteria.andCondition("reback_date <=",endRebackDate);
        }

        if (rebackQueryBean.getCurrentPage() != null && rebackQueryBean.getPageSize() != null) {
            PageHelper.startPage(rebackQueryBean.getCurrentPage(), rebackQueryBean.getPageSize());
        }
        return rebackDao.selectByExample(example);
    }

    public PageResult pageQuery(RebackQueryBean rebackQueryBean, User user) {
        if (rebackQueryBean.getCurrentPage() != null && rebackQueryBean.getPageSize() != null) {
            PageHelper.startPage(rebackQueryBean.getCurrentPage(), rebackQueryBean.getPageSize());
        }
        Page<Reback> pageData = (Page<Reback>) findByCondition(rebackQueryBean,user);

        return new PageResult(pageData.getTotal(),pageData.getResult());
    }

    public Result mark(Integer id) {
        Reback reback = new Reback();
        reback.setId(id);
        Reback reback_db = rebackDao.selectOne(reback);
        String rebacked = reback_db.getRebacked();

        if (rebacked != null && rebacked.equals("是")){
            reback_db.setRebacked("否");
            reback_db.setRebackDate("");
            rebackDao.updateByPrimaryKey(reback_db);
            return new Result(true,"撤销打标成功");
        }else {
            reback_db.setRebacked("是");
            String rebackDate = MyUtils.getExcelDate(new Date());
            reback_db.setRebackDate(rebackDate);
            rebackDao.updateByPrimaryKey(reback_db);
            return new Result(true,"回款打标成功");
        }


    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Cache.rebackCodeSet = rebackDao.getRebackCodeSet();
    }

    @Transactional
    public void delete(Integer id) {
        Reback reback = new Reback();
        reback = rebackDao.selectByPrimaryKey(id);
        System.out.println("reback>>>>"+reback);
        Example example = new Example(Electric.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("rebackCode",reback.getRebackCode());
        rebackDao.deleteByPrimaryKey(id);
        electricDao.deleteByExample(example);
    }
}
