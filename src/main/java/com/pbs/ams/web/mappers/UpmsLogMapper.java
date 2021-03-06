package com.pbs.ams.web.mappers;

import com.pbs.ams.web.model.UpmsLog;
import com.pbs.ams.web.model.UpmsLogExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UpmsLogMapper {
    long countByExample(UpmsLogExample example);

    int deleteByExample(UpmsLogExample example);

    int deleteByPrimaryKey(Long logId);

    int insert(UpmsLog record);

    int insertSelective(UpmsLog record);

    List<UpmsLog> selectByExampleWithBLOBs(UpmsLogExample example);

    List<UpmsLog> selectByExample(UpmsLogExample example);

    UpmsLog selectByPrimaryKey(Long logId);

    int updateByExampleSelective(@Param("record") UpmsLog record, @Param("example") UpmsLogExample example);

    int updateByExampleWithBLOBs(@Param("record") UpmsLog record, @Param("example") UpmsLogExample example);

    int updateByExample(@Param("record") UpmsLog record, @Param("example") UpmsLogExample example);

    int updateByPrimaryKeySelective(UpmsLog record);

    int updateByPrimaryKeyWithBLOBs(UpmsLog record);

    int updateByPrimaryKey(UpmsLog record);
}