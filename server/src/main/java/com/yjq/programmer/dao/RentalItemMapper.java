package com.yjq.programmer.dao;

import com.yjq.programmer.domain.RentalItem;
import com.yjq.programmer.domain.RentalItemExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RentalItemMapper {
    int countByExample(RentalItemExample example);

    int deleteByExample(RentalItemExample example);

    int deleteByPrimaryKey(String id);

    int insert(RentalItem record);

    int insertSelective(RentalItem record);

    List<RentalItem> selectByExample(RentalItemExample example);

    RentalItem selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") RentalItem record, @Param("example") RentalItemExample example);

    int updateByExample(@Param("record") RentalItem record, @Param("example") RentalItemExample example);

    int updateByPrimaryKeySelective(RentalItem record);

    int updateByPrimaryKey(RentalItem record);
}
