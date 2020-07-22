package com.tower.nanan.dao;

import com.tower.nanan.pojo.RebackStatWithReport;
import com.tower.nanan.pojo.RebackStatWithSite;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

public interface RebackStatWithReportDao extends Mapper<RebackStatWithReport> {

    @Update("truncate table rebackstat_report")
    public void truncate();
}
