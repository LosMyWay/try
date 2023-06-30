package com.yjq.programmer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjq.programmer.bean.CodeMsg;
import com.yjq.programmer.constant.CreditDescription;
import com.yjq.programmer.dao.*;
import com.yjq.programmer.domain.*;
import com.yjq.programmer.dto.*;
import com.yjq.programmer.enums.CreditStateEnum;
import com.yjq.programmer.enums.RoleEnum;
import com.yjq.programmer.enums.SeatItemStateEnum;
import com.yjq.programmer.service.ISeatService;
import com.yjq.programmer.utils.CommonUtil;
import com.yjq.programmer.utils.CopyUtil;
import com.yjq.programmer.utils.UuidUtil;
import com.yjq.programmer.utils.ValidateEntityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2022-09-14 11:11
 */
@Service
@Transactional
public class SeatServiceImpl implements ISeatService {

    @Resource
    private SeatMapper seatMapper;

    @Resource
    private ScheduleMapper scheduleMapper;

    @Resource
    private SeatItemMapper seatItemMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private CreditItemMapper creditItemMapper;

    /**
     * 分页获取座位数据
     * @param pageDTO
     * @return
     */
    @Override
    public ResponseDTO<PageDTO<SeatDTO>> getSeatListByPage(PageDTO<SeatDTO> pageDTO) {
        SeatExample seatExample = new SeatExample();
        // 判断是否进行关键字搜索
        SeatDTO searchSeatDTO = pageDTO.getSearchEntity();
        SeatExample.Criteria criteria = seatExample.createCriteria();
        if(!CommonUtil.isEmpty(searchSeatDTO.getScheduleId())) {
            criteria.andScheduleIdEqualTo(searchSeatDTO.getScheduleId());
        }
        if(searchSeatDTO.getOpenTime() != null) {
            criteria.andOpenTimeEqualTo(searchSeatDTO.getOpenTime());
        }
        // 不知道当前页多少，默认为第一页
        if(pageDTO.getPage() == null){
            pageDTO.setPage(1);
        }
        if(pageDTO.getSize() == null) {
            pageDTO.setSize(5);
        }
        PageHelper.startPage(pageDTO.getPage(), pageDTO.getSize());
        // 分页查出座位数据
        List<Seat> seatList = seatMapper.selectByExample(seatExample);
        PageInfo<Seat> pageInfo = new PageInfo<>(seatList);
        // 获取数据的总数
        pageDTO.setTotal(pageInfo.getTotal());
        // 讲domain类型数据  转成 DTO类型数据
        List<SeatDTO> seatDTOList = CopyUtil.copyList(seatList, SeatDTO.class);
        // 封装所属时刻数据
        for(SeatDTO seatDTO : seatDTOList) {
            Schedule schedule = scheduleMapper.selectByPrimaryKey(seatDTO.getScheduleId());
            seatDTO.setScheduleDTO(CopyUtil.copy(schedule, ScheduleDTO.class));
        }
        pageDTO.setList(seatDTOList);
        return ResponseDTO.success(pageDTO);
    }

    /**
     * 保存座位数据(添加、修改)
     * @param seatDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> saveSeat(SeatDTO seatDTO) {
        // 进行统一表单验证
        CodeMsg validate = ValidateEntityUtil.validate(seatDTO);
        if(!validate.getCode().equals(CodeMsg.SUCCESS.getCode())){
            return ResponseDTO.errorByMsg(validate);
        }
        Seat seat = CopyUtil.copy(seatDTO, Seat.class);
        if(CommonUtil.isEmpty(seat.getId())){
            // id为空 说明是添加数据
            // 判断开发时间和所属时刻是否已经有数据了
            if(isSeatExist(seat, "")){
                return ResponseDTO.errorByMsg(CodeMsg.SEAT_ALREADY_EXIST);
            }
            // 生成8位id
            seat.setId(UuidUtil.getShortUuid());
            // 设置总座位数
            seat.setTotalNum(seat.getRow() * seat.getCol());
            // 添加数据到数据库
            if(seatMapper.insertSelective(seat) == 0){
                return ResponseDTO.errorByMsg(CodeMsg.SEAT_ADD_ERROR);
            }
        }else{
            // id不为空 说明是修改数据
            // 判断开发时间和所属时刻是否已经有数据了
            if(isSeatExist(seat, seat.getId())){
                return ResponseDTO.errorByMsg(CodeMsg.SEAT_ALREADY_EXIST);
            }
            // 删除不在行数列数范围内选座详情数据
            SeatItemExample seatItemExample = new SeatItemExample();
            seatItemExample.createCriteria()
                    .andSeatIdEqualTo(seat.getId())
                    .andColGreaterThan(seat.getCol())
                    .andRowGreaterThan(seat.getRow());
            seatItemMapper.deleteByExample(seatItemExample);
            // 设置总座位数
            seat.setTotalNum(seat.getRow() * seat.getCol());
            // 设置已选座位数
            seatItemExample = new SeatItemExample();
            seatItemExample.createCriteria()
                    .andSeatIdEqualTo(seat.getId())
                    .andColLessThanOrEqualTo(seat.getCol())
                    .andRowLessThanOrEqualTo(seat.getRow());
            seat.setPickNum(seatItemMapper.countByExample(seatItemExample));
            // 修改数据库中数据
            if(seatMapper.updateByPrimaryKeySelective(seat) == 0){
                return ResponseDTO.errorByMsg(CodeMsg.SEAT_EDIT_ERROR);
            }
        }
        return ResponseDTO.successByMsg(true, "保存成功！");
    }

    /**
     * 删除座位数据
     * @param seatDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> removeSeat(SeatDTO seatDTO) {
        if(CommonUtil.isEmpty(seatDTO.getId())) {
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        // 删除座位数据
        if(seatMapper.deleteByPrimaryKey(seatDTO.getId()) == 0) {
            return ResponseDTO.errorByMsg(CodeMsg.SEAT_DELETE_ERROR);
        }
        return ResponseDTO.successByMsg(true, "删除成功！");
    }

    /**
     * 根据时间获取座位信息
     * @param seatDTO
     * @return
     */
    @Override
    public ResponseDTO<SeatDTO> getSeatByTimeAndSchedule(SeatDTO seatDTO) {
        SeatExample seatExample = new SeatExample();
        seatExample.createCriteria().andOpenTimeEqualTo(seatDTO.getOpenTime()).andScheduleIdEqualTo(seatDTO.getScheduleId());
        List<Seat> seatList = seatMapper.selectByExample(seatExample);
        if(seatList != null && seatList.size() > 0) {
            return ResponseDTO.success(CopyUtil.copy(seatList.get(0), SeatDTO.class));
        } else {
            return ResponseDTO.success(null);
        }
    }

    /**
     * 根据座位id获取座位详情信息
     * @param seatDTO
     * @return
     */
    @Override
    public ResponseDTO<List<SeatItemDTO>> getItemBySeatId(SeatDTO seatDTO) {
        if(CommonUtil.isEmpty(seatDTO.getId())) {
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        SeatItemExample seatItemExample = new SeatItemExample();
        seatItemExample.createCriteria().andSeatIdEqualTo(seatDTO.getId()).andStateNotEqualTo(SeatItemStateEnum.CANCEL.getCode());
        List<SeatItem> seatItemList = seatItemMapper.selectByExample(seatItemExample);
        return ResponseDTO.success(CopyUtil.copyList(seatItemList, SeatItemDTO.class));
    }

    /**
     * 预约座位操作处理
     * @param seatItemDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> pickSeat(SeatItemDTO seatItemDTO) {
        // 进行统一表单验证
        CodeMsg validate = ValidateEntityUtil.validate(seatItemDTO);
        if(!validate.getCode().equals(CodeMsg.SUCCESS.getCode())){
            return ResponseDTO.errorByMsg(validate);
        }
        Seat seat = seatMapper.selectByPrimaryKey(seatItemDTO.getSeatId());
        if(seat.getPickNum() >= seat.getTotalNum()) {
            return ResponseDTO.errorByMsg(CodeMsg.SEAT_ALREADY_FULL);
        }
        // 判断信誉积分是否小于80
        User user = userMapper.selectByPrimaryKey(seatItemDTO.getUserId());
        if(user == null) {
            return ResponseDTO.errorByMsg(CodeMsg.USER_NOT_EXIST);
        }
        if (user.getCreditRate() < 80) {
            return ResponseDTO.errorByMsg(CodeMsg.CREDIT_PICK_ERROR);
        }

        List<Integer> stateList = new ArrayList<>();
        stateList.add(SeatItemStateEnum.USING.getCode());
        stateList.add(SeatItemStateEnum.APPOINTMENT.getCode());
        SeatItemExample seatItemExample = new SeatItemExample();
        seatItemExample.createCriteria().andUserIdEqualTo(user.getId()).andSeatIdEqualTo(seat.getId())
                .andStateIn(stateList);
        if(seatItemMapper.selectByExample(seatItemExample).size() > 0) {
            return ResponseDTO.errorByMsg(CodeMsg.SEAT_ITEM_BOOK_EXIST);
        }


        // 更新座位的选座数  乐观锁  版本号控制避免重复选座
        SeatExample seatExample = new SeatExample();
        seatExample.createCriteria()
                .andIdEqualTo(seat.getId())
                .andVersionEqualTo(seat.getVersion());
        seat.setPickNum(seat.getPickNum() + 1);
        seat.setVersion(seat.getVersion() + 1);
        if(seatMapper.updateByExampleSelective(seat, seatExample) == 0) {
            return ResponseDTO.errorByMsg(CodeMsg.SEAT_ALREADY_PICK);
        }
        Schedule schedule = scheduleMapper.selectByPrimaryKey(seat.getScheduleId());
        // 写入选座详情数据
        SeatItem seatItem = CopyUtil.copy(seatItemDTO, SeatItem.class);
        seatItem.setId(UuidUtil.getShortUuid());
        seatItem.setCreateTime(new Date());
        seatItem.setState(SeatItemStateEnum.APPOINTMENT.getCode());
        seatItem.setOpenTime(seat.getOpenTime());
        seatItem.setRangeTime(schedule.getName() + "(" + schedule.getRangeTime() + ")");
        if(seatItemMapper.insertSelective(seatItem) == 0) {
            throw new RuntimeException(CodeMsg.SEAT_PICK_ERROR.getMsg());
        }
        return ResponseDTO.successByMsg(true, "预约成功！");
    }

    /**
     * 获取座位详情数据
     * @param pageDTO
     * @return
     */
    @Override
    public ResponseDTO<PageDTO<SeatItemDTO>> getSeatItemList(PageDTO<SeatItemDTO> pageDTO) {
        SeatItemDTO searchSeatItemDTO = pageDTO.getSearchEntity();
        SeatItemExample seatItemExample = new SeatItemExample();
        SeatItemExample.Criteria criteria = seatItemExample.createCriteria();
        User user = userMapper.selectByPrimaryKey(searchSeatItemDTO.getUserId());
        if(user == null) {
            return ResponseDTO.errorByMsg(CodeMsg.USER_NOT_EXIST);
        }
        if(RoleEnum.STUDENT.getCode().equals(user.getRoleId())) {
            // 如果是学生角色，只能查看自己的预约信息
            criteria.andUserIdEqualTo(user.getId());
        }
        if(searchSeatItemDTO.getOpenTime() != null) {
            criteria.andOpenTimeEqualTo(searchSeatItemDTO.getOpenTime());
        }
        // 不知道当前页多少，默认为第一页
        if(pageDTO.getPage() == null){
            pageDTO.setPage(1);
        }
        if(pageDTO.getSize() == null) {
            pageDTO.setSize(5);
        }
        seatItemExample.setOrderByClause("create_time desc");
        PageHelper.startPage(pageDTO.getPage(), pageDTO.getSize());
        // 分页查出座位详情数据
        List<SeatItem> seatItemList = seatItemMapper.selectByExample(seatItemExample);
        PageInfo<SeatItem> pageInfo = new PageInfo<>(seatItemList);
        // 获取数据的总数
        pageDTO.setTotal(pageInfo.getTotal());
        // 讲domain类型数据  转成 DTO类型数据
        List<SeatItemDTO> seatItemDTOList = CopyUtil.copyList(seatItemList, SeatItemDTO.class);
        for(SeatItemDTO seatItemDTO : seatItemDTOList) {
            Seat seat = seatMapper.selectByPrimaryKey(seatItemDTO.getSeatId());
            SeatDTO seatDTO = CopyUtil.copy(seat, SeatDTO.class);
            seatItemDTO.setSeatDTO(seatDTO);
            Schedule schedule = scheduleMapper.selectByPrimaryKey(seat.getScheduleId());
            seatDTO.setScheduleDTO(CopyUtil.copy(schedule, ScheduleDTO.class));
        }
        pageDTO.setList(seatItemDTOList);

        return ResponseDTO.success(pageDTO);
    }

    /**
     * 修改座位详情状态
     * @param seatItemDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> updateSeatItemState(SeatItemDTO seatItemDTO) {
        if(CommonUtil.isEmpty(seatItemDTO.getId()) || seatItemDTO.getState() == null) {
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        SeatItem seatItem = seatItemMapper.selectByPrimaryKey(seatItemDTO.getId());
        Seat seat = seatMapper.selectByPrimaryKey(seatItem.getSeatId());
        if(SeatItemStateEnum.CANCEL.getCode().equals(seatItemDTO.getState()) && !seatItemDTO.getState().equals(seatItem.getState())) {
            // 如果是改成已取消状态  已选座位数回退
            SeatExample seatExample = new SeatExample();
            seatExample.createCriteria()
                    .andIdEqualTo(seat.getId())
                    .andVersionEqualTo(seat.getVersion());
            seat.setPickNum(seat.getPickNum() - 1);
            seat.setVersion(seat.getVersion() + 1);
            if(seatMapper.updateByExampleSelective(seat, seatExample) == 0) {
                return ResponseDTO.errorByMsg(CodeMsg.SYSTEM_BUSY);
            }
        } else if(SeatItemStateEnum.BREAK.getCode().equals(seatItemDTO.getState()) && !seatItemDTO.getState().equals(seatItem.getState())) {
            // 如果是已违约状态 扣除信誉积分5分 写入信誉明细
            User user = userMapper.selectByPrimaryKey(seatItem.getUserId());
            if(user == null) {
                return ResponseDTO.errorByMsg(CodeMsg.USER_NOT_EXIST);
            }
            if(user.getCreditRate() > 0) {
                user.setCreditRate(user.getCreditRate() - 5);
                userMapper.updateByPrimaryKeySelective(user);
                CreditItem creditItem = new CreditItem();
                creditItem.setId(UuidUtil.getShortUuid());
                creditItem.setCreateTime(new Date());
                creditItem.setRate(-5);
                creditItem.setState(CreditStateEnum.OVER.getCode());
                creditItem.setNowRate(user.getCreditRate());
                creditItem.setUserId(user.getId());
                creditItem.setDescription(CreditDescription.CREDIT_RATE_SUB_BY_SEAT);
                if(creditItemMapper.insertSelective(creditItem) == 0) {
                    throw new RuntimeException(CodeMsg.CREDIT_ADD_ERROR.getMsg());
                }
            }
        } else if (SeatItemStateEnum.USING.getCode().equals(seatItemDTO.getState()) && !seatItemDTO.getState().equals(seatItem.getState())) {
            // 如果是使用中状态 增加信誉积分5分 写入信誉明细
            User user = userMapper.selectByPrimaryKey(seatItem.getUserId());
            if(user == null) {
                return ResponseDTO.errorByMsg(CodeMsg.USER_NOT_EXIST);
            }
            if(user.getCreditRate() < 100) {
                user.setCreditRate(user.getCreditRate() + 5);
                userMapper.updateByPrimaryKeySelective(user);
                CreditItem creditItem = new CreditItem();
                creditItem.setId(UuidUtil.getShortUuid());
                creditItem.setCreateTime(new Date());
                creditItem.setRate(5);
                creditItem.setState(CreditStateEnum.OVER.getCode());
                creditItem.setNowRate(user.getCreditRate());
                creditItem.setUserId(user.getId());
                creditItem.setDescription(CreditDescription.CREDIT_RATE_ADD_BY_SEAT);
                if(creditItemMapper.insertSelective(creditItem) == 0) {
                    throw new RuntimeException(CodeMsg.CREDIT_ADD_ERROR.getMsg());
                }
            }
        }
        seatItem.setState(seatItemDTO.getState());
        if(seatItemMapper.updateByPrimaryKeySelective(seatItem) == 0) {
            throw new RuntimeException(CodeMsg.SEAT_STATE_EDIT_ERROR.getMsg());
        }

        return ResponseDTO.success(true);
    }

    /**
     * 删除座位详情数据
     * @param seatItemDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> removeSeatItem(SeatItemDTO seatItemDTO) {
        if(CommonUtil.isEmpty(seatItemDTO.getId())) {
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        SeatItem seatItem = seatItemMapper.selectByPrimaryKey(seatItemDTO.getId());
        Seat seat = seatMapper.selectByPrimaryKey(seatItem.getSeatId());
        // 已选座位数回退
        SeatExample seatExample = new SeatExample();
        seatExample.createCriteria()
                .andIdEqualTo(seat.getId())
                .andVersionEqualTo(seat.getVersion());
        seat.setPickNum(seat.getPickNum() - 1);
        seat.setVersion(seat.getVersion() + 1);
        if(seatMapper.updateByExampleSelective(seat, seatExample) == 0) {
            return ResponseDTO.errorByMsg(CodeMsg.SYSTEM_BUSY);
        }
        // 删除数据
        if(seatItemMapper.deleteByPrimaryKey(seatItem.getId()) == 0) {
            return ResponseDTO.errorByMsg(CodeMsg.SEAT_ITEM_REMOVE_ERROR);
        }
        return ResponseDTO.successByMsg(true, "删除成功！");
    }

    /**
     * 获取今日预约座位数
     * @return
     */
    @Override
    public ResponseDTO<Integer> getSeatItemTotalByDay() {
        SeatItemExample seatItemExample = new SeatItemExample();
        Date startDate = CommonUtil.getFormatterStr(CommonUtil.getFormatterDate(new Date(), "yyyy-MM-dd") + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        Date endDate = CommonUtil.getFormatterStr(CommonUtil.getFormatterDate(new Date(), "yyyy-MM-dd") + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
        seatItemExample.createCriteria().andCreateTimeBetween(startDate, endDate).andStateNotEqualTo(SeatItemStateEnum.CANCEL.getCode());
        int total = seatItemMapper.countByExample(seatItemExample);
        return ResponseDTO.success(total);
    }

    /**
     * 判断开发时间和所属时刻是否已经有数据了
     * @param seat
     * @param id
     * @return
     */
    public Boolean isSeatExist(Seat seat, String id) {
        SeatExample seatExample = new SeatExample();
        seatExample.createCriteria().andOpenTimeEqualTo(seat.getOpenTime()).andScheduleIdEqualTo(seat.getScheduleId());
        List<Seat> selectedSeatList = seatMapper.selectByExample(seatExample);
        if(selectedSeatList != null && selectedSeatList.size() > 0) {
            if(selectedSeatList.size() > 1){
                return true; //出现重名
            }
            if(!selectedSeatList.get(0).getId().equals(id)) {
                return true; //出现重名
            }
        }
        return false;//没有重名
    }
}
