package com.yjq.programmer.controller;

import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.dto.ScheduleDTO;
import com.yjq.programmer.service.IScheduleService;
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
@RequestMapping("/schedule")
@RestController
public class ScheduleController {

    @Resource
    private IScheduleService scheduleService;

    /**
     * 分页获取时刻数据
     * @param pageDTO
     * @return
     */
    @PostMapping("/list")
    public ResponseDTO<PageDTO<ScheduleDTO>> getScheduleListByPage(@RequestBody PageDTO<ScheduleDTO> pageDTO){
        return scheduleService.getScheduleListByPage(pageDTO);
    }

    /**
     * 获取所有时刻数据
     * @return
     */
    @PostMapping("/all")
    public ResponseDTO<List<ScheduleDTO>> getScheduleList() {
        return scheduleService.getScheduleList();
    }

    /**
     * 保存时刻数据(添加、修改)
     * @param scheduleDTO
     * @return
     */
    @PostMapping("/save")
    public ResponseDTO<Boolean> saveSchedule(@RequestBody ScheduleDTO scheduleDTO){
        return scheduleService.saveSchedule(scheduleDTO);
    }
}
