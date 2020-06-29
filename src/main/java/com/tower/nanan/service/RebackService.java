package com.tower.nanan.service;

import com.tower.nanan.dao.RebackDao;
import com.tower.nanan.pojo.Reback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
