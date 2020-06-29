package com.tower.nanan.service;

import com.tower.nanan.dao.VerifyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class VerifyService {

    @Autowired
    private VerifyDao verifyDao;

    public Set<String> getVerifyCodeSet(){
        return verifyDao.getVerifyCodeSet();
    }
}
