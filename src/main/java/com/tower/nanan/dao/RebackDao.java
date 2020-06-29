package com.tower.nanan.dao;

import com.tower.nanan.pojo.Reback;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Set;

public interface RebackDao extends Mapper<Reback> {

    @Select("select reback_code from reback")
    public Set<String> getRebackCodeSet();
}
