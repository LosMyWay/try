package com.yjq.programmer.controller;

import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.dto.SeatDTO;
import com.yjq.programmer.dto.SeatItemDTO;
import com.yjq.programmer.service.ISeatService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 蔡中宇
 * @create 2023-04-19
 */
@RequestMapping("/seat")
@RestController
public class SeatController {

    @Resource
    private ISeatService seatService;

    /**
     * 分页获取座位数据
     * @param pageDTO
     * @return
     */
    @PostMapping("/list")
    public ResponseDTO<PageDTO<SeatDTO>> getSeatListByPage(@RequestBody PageDTO<SeatDTO> pageDTO){
        return seatService.getSeatListByPage(pageDTO);
    }

    /**
     * 根据时间获取座位信息
     * @param seatDTO
     * @return
     */
    @PostMapping("/getByTime")
    public ResponseDTO<SeatDTO> getSeatByTimeAndSchedule(@RequestBody SeatDTO seatDTO) {
        return seatService.getSeatByTimeAndSchedule(seatDTO);
    }

    /**
     * 根据座位id获取座位详情信息
     * @param seatDTO
     * @return
     */
    @PostMapping("/getItemBySeatId")
    public ResponseDTO<List<SeatItemDTO>> getItemBySeatId(@RequestBody SeatDTO seatDTO) {
        return seatService.getItemBySeatId(seatDTO);
    }


    /**
     * 保存座位数据(添加、修改)
     * @param seatDTO
     * @return
     */
    @PostMapping("/save")
    public ResponseDTO<Boolean> saveSeat(@RequestBody SeatDTO seatDTO){
        return seatService.saveSeat(seatDTO);
    }

    /**
     * 后台删除座位数据
     * @param seatDTO
     * @return
     */
    @PostMapping("/remove")
    public ResponseDTO<Boolean> removeSeat(@RequestBody SeatDTO seatDTO){
        return seatService.removeSeat(seatDTO);
    }

    /**
     * 预约座位操作处理
     * @param seatItemDTO
     * @return
     */
    @PostMapping("/pick")
    public ResponseDTO<Boolean> pickSeat(@RequestBody SeatItemDTO seatItemDTO) {
        return seatService.pickSeat(seatItemDTO);
    }

    /**
     * 获取座位详情数据
     * @param pageDTO
     * @return
     */
    @PostMapping("/getSeatItemList")
    public ResponseDTO<PageDTO<SeatItemDTO>> getSeatItemList(@RequestBody PageDTO<SeatItemDTO> pageDTO) {
        return seatService.getSeatItemList(pageDTO);
    }

    /**
     * 修改座位详情状态
     * @param seatItemDTO
     * @return
     */
    @PostMapping("/updateSeatItemState")
    public ResponseDTO<Boolean> updateSeatItemState(@RequestBody SeatItemDTO seatItemDTO) {
        return seatService.updateSeatItemState(seatItemDTO);
    }

    /**
     * 删除座位详情数据
     * @param seatItemDTO
     * @return
     */
    @PostMapping("/removeSeatItem")
    public ResponseDTO<Boolean> removeSeatItem(@RequestBody SeatItemDTO seatItemDTO) {
        return seatService.removeSeatItem(seatItemDTO);
    }


    /**
     * 获取今日预约座位数
     * @return
     */
    @PostMapping("/dayTotal")
    public ResponseDTO<Integer> getSeatItemTotalByDay() {
        return seatService.getSeatItemTotalByDay();
    }
}
