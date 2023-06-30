package com.yjq.programmer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjq.programmer.bean.CodeMsg;
import com.yjq.programmer.dao.ScheduleMapper;
import com.yjq.programmer.domain.Schedule;
import com.yjq.programmer.domain.ScheduleExample;
import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.dto.ScheduleDTO;
import com.yjq.programmer.service.IScheduleService;
import com.yjq.programmer.utils.CommonUtil;
import com.yjq.programmer.utils.CopyUtil;
import com.yjq.programmer.utils.UuidUtil;
import com.yjq.programmer.utils.ValidateEntityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2022-09-14 9:36
 */
@Service
@Transactional
public class ScheduleServiceImpl implements IScheduleService {

    @Resource
    private ScheduleMapper scheduleMapper;

    /**
     * 分页获取时刻数据
     * @param pageDTO
     * @return
     */
    @Override
    public ResponseDTO<PageDTO<ScheduleDTO>> getScheduleListByPage(PageDTO<ScheduleDTO> pageDTO) {
        ScheduleExample scheduleExample = new ScheduleExample();
        // 不知道当前页多少，默认为第一页
        if(pageDTO.getPage() == null){
            pageDTO.setPage(1);
        }
        if(pageDTO.getSize() == null) {
            pageDTO.setSize(5);
        }
        PageHelper.startPage(pageDTO.getPage(), pageDTO.getSize());
        // 分页查出时刻数据
        List<Schedule> scheduleList = scheduleMapper.selectByExample(scheduleExample);
        PageInfo<Schedule> pageInfo = new PageInfo<>(scheduleList);
        // 获取数据的总数
        pageDTO.setTotal(pageInfo.getTotal());
        // 讲domain类型数据  转成 DTO类型数据
        List<ScheduleDTO> scheduleDTOList = CopyUtil.copyList(scheduleList, ScheduleDTO.class);
        pageDTO.setList(scheduleDTOList);
        return ResponseDTO.success(pageDTO);
    }

    /**
     * 保存时刻数据(添加、修改)
     * @param scheduleDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> saveSchedule(ScheduleDTO scheduleDTO) {
        // 进行统一表单验证
        CodeMsg validate = ValidateEntityUtil.validate(scheduleDTO);
        if(!validate.getCode().equals(CodeMsg.SUCCESS.getCode())){
            return ResponseDTO.errorByMsg(validate);
        }
        Schedule schedule = CopyUtil.copy(scheduleDTO, Schedule.class);
        if(CommonUtil.isEmpty(schedule.getId())){
            // id为空 说明是添加数据
            // 生成8位id
            schedule.setId(UuidUtil.getShortUuid());
            // 添加数据到数据库
            if(scheduleMapper.insertSelective(schedule) == 0){
                return ResponseDTO.errorByMsg(CodeMsg.SCHEDULE_ADD_ERROR);
            }
        }else{
            // id不为空 说明是修改数据
            // 修改数据库中数据
            if(scheduleMapper.updateByPrimaryKeySelective(schedule) == 0){
                return ResponseDTO.errorByMsg(CodeMsg.SCHEDULE_EDIT_ERROR);
            }
        }
        return ResponseDTO.successByMsg(true, "保存成功！");
    }

    /**
     * 获取所有时刻数据
     * @return
     */
    @Override
    public ResponseDTO<List<ScheduleDTO>> getScheduleList() {
        List<Schedule> scheduleList = scheduleMapper.selectByExample(new ScheduleExample());
        return ResponseDTO.success(CopyUtil.copyList(scheduleList, ScheduleDTO.class));
    }
}
