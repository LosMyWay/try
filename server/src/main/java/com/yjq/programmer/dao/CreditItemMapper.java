package com.yjq.programmer.dao;

import com.yjq.programmer.domain.CreditItem;
import com.yjq.programmer.domain.CreditItemExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CreditItemMapper {
    int countByExample(CreditItemExample example);

    int deleteByExample(CreditItemExample example);

    int deleteByPrimaryKey(String id);

    int insert(CreditItem record);

    int insertSelective(CreditItem record);

    List<CreditItem> selectByExample(CreditItemExample example);

    CreditItem selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") CreditItem record, @Param("example") CreditItemExample example);

    int updateByExample(@Param("record") CreditItem record, @Param("example") CreditItemExample example);

    int updateByPrimaryKeySelective(CreditItem record);

    int updateByPrimaryKey(CreditItem record);
}
