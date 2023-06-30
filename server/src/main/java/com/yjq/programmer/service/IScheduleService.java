package com.yjq.programmer.service;

import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.dto.ScheduleDTO;

import java.util.List;

/**
 * @author 蔡中宇
 * @create 2023-04-24
 */
public interface IScheduleService {

    // 分页获取时刻数据
    ResponseDTO<PageDTO<ScheduleDTO>> getScheduleListByPage(PageDTO<ScheduleDTO> pageDTO);

    // 保存时刻数据(添加、修改)
    ResponseDTO<Boolean> saveSchedule(ScheduleDTO scheduleDTO);

    // 获取所有时刻数据
    ResponseDTO<List<ScheduleDTO>> getScheduleList();
}
