package com.pbs.ams.web.mappers;

import com.pbs.ams.web.model.AmsProductAccount;
import com.pbs.ams.web.model.AmsProductAccountExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AmsProductAccountMapper {
    long countByExample(AmsProductAccountExample example);

    int deleteByExample(AmsProductAccountExample example);

    int deleteByPrimaryKey(Long productAccountId);

    int insert(AmsProductAccount record);

    int insertSelective(AmsProductAccount record);

    List<AmsProductAccount> selectByExample(AmsProductAccountExample example);

    AmsProductAccount selectByPrimaryKey(Long productAccountId);

    int updateByExampleSelective(@Param("record") AmsProductAccount record, @Param("example") AmsProductAccountExample example);

    int updateByExample(@Param("record") AmsProductAccount record, @Param("example") AmsProductAccountExample example);

    int updateByPrimaryKeySelective(AmsProductAccount record);

    int updateByPrimaryKey(AmsProductAccount record);
}