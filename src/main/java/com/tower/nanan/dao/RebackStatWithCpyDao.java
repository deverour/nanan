package com.tower.nanan.dao;

import com.tower.nanan.pojo.RebackStatWithCpy;
import com.tower.nanan.pojo.RebackStatWithSite;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

public interface RebackStatWithCpyDao extends Mapper<RebackStatWithCpy> {

    @Update("truncate table rebackstat_cpy")
    public void truncate();
}
