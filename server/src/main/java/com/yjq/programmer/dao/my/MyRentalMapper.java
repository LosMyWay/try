package com.yjq.programmer.dao.my;

import com.yjq.programmer.domain.RentalItem;

import java.util.List;
import java.util.Map;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2022-09-22 14:35
 */
public interface MyRentalMapper {

    // 根据条件查询借阅数据
    List<RentalItem> findRentalItemBySearch(Map<String, Object> queryMap);
}
