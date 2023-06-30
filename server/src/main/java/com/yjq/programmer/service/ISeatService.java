package com.yjq.programmer.service;

import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.dto.SeatDTO;
import com.yjq.programmer.dto.SeatItemDTO;

import java.util.List;

/**
 * @author 蔡中宇
 * @create 2023-04-24
 */
public interface ISeatService {

    // 分页获取座位数据
    ResponseDTO<PageDTO<SeatDTO>> getSeatListByPage(PageDTO<SeatDTO> pageDTO);

    // 保存座位数据(添加、修改)
    ResponseDTO<Boolean> saveSeat(SeatDTO seatDTO);

    // 删除座位数据
    ResponseDTO<Boolean> removeSeat(SeatDTO seatDTO);

    // 根据时间获取座位信息
    ResponseDTO<SeatDTO> getSeatByTimeAndSchedule(SeatDTO seatDTO);

    // 根据座位id获取座位详情信息
    ResponseDTO<List<SeatItemDTO>> getItemBySeatId(SeatDTO seatDTO);

    // 预约座位操作处理
    ResponseDTO<Boolean> pickSeat(SeatItemDTO seatItemDTO);

    // 获取座位详情数据
    ResponseDTO<PageDTO<SeatItemDTO>> getSeatItemList(PageDTO<SeatItemDTO> pageDTO);

    // 修改座位详情状态
    ResponseDTO<Boolean> updateSeatItemState(SeatItemDTO seatItemDTO);

    // 删除座位详情数据
    ResponseDTO<Boolean> removeSeatItem(SeatItemDTO seatItemDTO);

    // 获取今日预约座位数
    ResponseDTO<Integer> getSeatItemTotalByDay();
}
