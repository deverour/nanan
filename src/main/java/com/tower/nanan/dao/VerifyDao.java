package com.tower.nanan.dao;

import com.tower.nanan.pojo.Verify;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

import java.util.Set;

public interface VerifyDao extends Mapper<Verify>, MySqlMapper<Verify> {

    @Select("select verify_code from verify")
    public Set<String> getVerifyCodeSet();

    @Select("select bill_id from verify")
    public Set<String> getBillIdSet();
}
