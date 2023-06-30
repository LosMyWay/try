package com.yjq.programmer.dao;

import com.yjq.programmer.domain.Seat;
import com.yjq.programmer.domain.SeatExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SeatMapper {
    int countByExample(SeatExample example);

    int deleteByExample(SeatExample example);

    int deleteByPrimaryKey(String id);

    int insert(Seat record);

    int insertSelective(Seat record);

    List<Seat> selectByExample(SeatExample example);

    Seat selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Seat record, @Param("example") SeatExample example);

    int updateByExample(@Param("record") Seat record, @Param("example") SeatExample example);

    int updateByPrimaryKeySelective(Seat record);

    int updateByPrimaryKey(Seat record);
}
