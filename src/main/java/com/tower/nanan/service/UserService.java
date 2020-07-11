package com.tower.nanan.service;

import com.tower.nanan.dao.UserDao;
import com.tower.nanan.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public User findByUserAndPassword(String username, String password){
        Example example = new Example(User.class);
        System.out.println("username"+username+"password"+password);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username",username);
        criteria.andEqualTo("password",password);

        return  userDao.findUser(username,password);
    }

    public int change(User user){
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username",user.getUsername());
        criteria.andEqualTo("password",user.getPassword());
        String id = userDao.selectOneByExample(example).getId();
        User newUser = new User();
        newUser.setPassword(user.getNewpassword());
        newUser.setId(id);
        return userDao.updateByPrimaryKeySelective(newUser);
    }
}
