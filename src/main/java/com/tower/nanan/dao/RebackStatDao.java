package com.tower.nanan.dao;

import com.tower.nanan.pojo.RebackStat;
import com.tower.nanan.pojo.RebackStatWithSite;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

public interface RebackStatDao extends Mapper<RebackStat> {

    @Update("truncate table rebackstat")
    public void truncate();
}
