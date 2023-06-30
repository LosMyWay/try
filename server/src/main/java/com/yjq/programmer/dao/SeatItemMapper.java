package com.yjq.programmer.dao;

import com.yjq.programmer.domain.SeatItem;
import com.yjq.programmer.domain.SeatItemExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SeatItemMapper {
    int countByExample(SeatItemExample example);

    int deleteByExample(SeatItemExample example);

    int deleteByPrimaryKey(String id);

    int insert(SeatItem record);

    int insertSelective(SeatItem record);

    List<SeatItem> selectByExample(SeatItemExample example);

    SeatItem selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") SeatItem record, @Param("example") SeatItemExample example);

    int updateByExample(@Param("record") SeatItem record, @Param("example") SeatItemExample example);

    int updateByPrimaryKeySelective(SeatItem record);

    int updateByPrimaryKey(SeatItem record);
}
