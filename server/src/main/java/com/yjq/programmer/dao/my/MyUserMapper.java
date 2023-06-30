package com.yjq.programmer.dao.my;

import com.yjq.programmer.domain.CreditItem;

import java.util.List;
import java.util.Map;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2022-09-21 14:05
 */
public interface MyUserMapper {

    // 根据条件查询信誉积分详情数据
    List<CreditItem> findCreditItemBySearch(Map<String, Object> queryMap);
}
