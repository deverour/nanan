package com.tower.nanan.dao;

import com.tower.nanan.pojo.Verify;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.Set;

public interface VerifyDao extends Mapper<Verify> {

    @Select("select verifyCode from verify")
    public Set<String> getVerifyCodeSet();
}
