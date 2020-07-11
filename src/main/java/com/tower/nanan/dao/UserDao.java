package com.tower.nanan.dao;

import com.tower.nanan.pojo.User;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

public interface UserDao extends Mapper<User> {

    @Select("select * from user where username =#{username} and password=#{password}")
    User findUser(String username,String password);

}
