package com.tower.nanan.dao;

import com.tower.nanan.pojo.RebackStatWithCustomer;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

public interface RebackStatWithCustomerDao extends Mapper<RebackStatWithCustomer> {

    @Update("truncate table rebackstat_customer")
    public void truncate();
}
