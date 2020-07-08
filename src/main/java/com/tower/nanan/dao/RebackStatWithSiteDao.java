package com.tower.nanan.dao;

import com.tower.nanan.pojo.RebackStatWithCustomer;
import com.tower.nanan.pojo.RebackStatWithSite;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

public interface RebackStatWithSiteDao extends Mapper<RebackStatWithSite> {

    @Update("truncate table rebackstat_site")
    public void truncate();
}
