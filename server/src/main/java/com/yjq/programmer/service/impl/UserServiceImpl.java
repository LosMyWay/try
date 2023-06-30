package com.yjq.programmer.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjq.programmer.bean.CodeMsg;
import com.yjq.programmer.dao.CreditItemMapper;
import com.yjq.programmer.dao.RentalItemMapper;
import com.yjq.programmer.dao.SeatItemMapper;
import com.yjq.programmer.dao.UserMapper;
import com.yjq.programmer.dao.my.MyUserMapper;
import com.yjq.programmer.domain.*;
import com.yjq.programmer.dto.CreditItemDTO;
import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.dto.UserDTO;
import com.yjq.programmer.enums.CreditStateEnum;
import com.yjq.programmer.enums.RentalItemStateEnum;
import com.yjq.programmer.enums.RoleEnum;
import com.yjq.programmer.service.IUserService;
import com.yjq.programmer.utils.CommonUtil;
import com.yjq.programmer.utils.CopyUtil;
import com.yjq.programmer.utils.UuidUtil;
import com.yjq.programmer.utils.ValidateEntityUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2022-09-12 9:00
 */
@Service
@Transactional
public class UserServiceImpl implements IUserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private CreditItemMapper creditItemMapper;

    @Resource
    private RentalItemMapper rentalItemMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private MyUserMapper myUserMapper;

    @Resource
    private SeatItemMapper seatItemMapper;

    /**
     * 分页获取用户数据
     * @param pageDTO
     * @return
     */
    @Override
    public ResponseDTO<PageDTO<UserDTO>> getUserListByPage(PageDTO<UserDTO> pageDTO) {
        UserExample userExample = new UserExample();
        // 判断是否进行关键字搜索
        UserDTO searchUserDTO = pageDTO.getSearchEntity();
        UserExample.Criteria criteria = userExample.createCriteria();
        User user = userMapper.selectByPrimaryKey(searchUserDTO.getId());
        if(user == null) {
            return ResponseDTO.errorByMsg(CodeMsg.USER_NOT_EXIST);
        }
        if(RoleEnum.STUDENT.getCode().equals(user.getRoleId())) {
            // 如果是学生角色，只能查看自己的个人信息
            criteria.andIdEqualTo(user.getId());
        }
        if(!CommonUtil.isEmpty(searchUserDTO.getUsername())) {
            criteria.andUsernameLike("%" + searchUserDTO.getUsername() + "%");
        }
        if(searchUserDTO.getRoleId() != null && searchUserDTO.getRoleId() != 0) {
            criteria.andRoleIdEqualTo(searchUserDTO.getRoleId());
        }
        // 不知道当前页多少，默认为第一页
        if(pageDTO.getPage() == null){
            pageDTO.setPage(1);
        }
        if(pageDTO.getSize() == null) {
            pageDTO.setSize(5);
        }
        PageHelper.startPage(pageDTO.getPage(), pageDTO.getSize());
        // 分页查出用户数据
        List<User> userList = userMapper.selectByExample(userExample);
        PageInfo<User> pageInfo = new PageInfo<>(userList);
        // 获取数据的总数
        pageDTO.setTotal(pageInfo.getTotal());
        // 讲domain类型数据  转成 DTO类型数据
        List<UserDTO> userDTOList = CopyUtil.copyList(userList, UserDTO.class);
        pageDTO.setList(userDTOList);
        return ResponseDTO.success(pageDTO);
    }

    /**
     * 保存用户数据(添加、修改)
     * @param userDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> saveUser(UserDTO userDTO) {
        // 进行统一表单验证
        CodeMsg validate = ValidateEntityUtil.validate(userDTO);
        if(!validate.getCode().equals(CodeMsg.SUCCESS.getCode())){
            return ResponseDTO.errorByMsg(validate);
        }
        User user = CopyUtil.copy(userDTO, User.class);
        if(CommonUtil.isEmpty(user.getId())){
            // id为空 说明是添加数据
            // 判断用户昵称是否存在
            if(isUsernameExist(user, "")){
                return ResponseDTO.errorByMsg(CodeMsg.USERNAME_EXIST);
            }
            // 生成8位id
            user.setId(UuidUtil.getShortUuid());
            // 添加数据到数据库
            if(userMapper.insertSelective(user) == 0){
                return ResponseDTO.errorByMsg(CodeMsg.USER_ADD_ERROR);
            }
        }else{
            // id不为空 说明是修改数据
            // 判断用户昵称是否存在
            if(isUsernameExist(user, user.getId())){
                return ResponseDTO.errorByMsg(CodeMsg.USERNAME_EXIST);
            }
            // 修改数据库中数据
            if(userMapper.updateByPrimaryKeySelective(user) == 0){
                return ResponseDTO.errorByMsg(CodeMsg.USER_EDIT_ERROR);
            }
        }
        return ResponseDTO.successByMsg(true, "保存成功！");
    }

    /**
     * 删除用户数据
     * @param userDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> removeUser(UserDTO userDTO) {
        if(CommonUtil.isEmpty(userDTO.getId())){
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        RentalItemExample rentalItemExample = new RentalItemExample();
        rentalItemExample.createCriteria().andUserIdEqualTo(userDTO.getId());
        List<RentalItem> rentalItemList = rentalItemMapper.selectByExample(rentalItemExample);
        for(RentalItem rentalItem : rentalItemList) {
            if(RentalItemStateEnum.RENTAL.getCode().equals(rentalItem.getState())) {
                return ResponseDTO.errorByMsg(CodeMsg.RENTAL_BOOK_EXIST);
            }
        }
        // 删除与该用户有关的借阅信息
        rentalItemMapper.deleteByExample(rentalItemExample);
        // 删除与该用户有关的预约信息
        SeatItemExample seatItemExample = new SeatItemExample();
        seatItemExample.createCriteria().andUserIdEqualTo(userDTO.getId());
        seatItemMapper.deleteByExample(seatItemExample);
        // 删除与该用户有关的信誉积分信息
        CreditItemExample creditItemExample = new CreditItemExample();
        creditItemExample.createCriteria().andUserIdEqualTo(userDTO.getId());
        creditItemMapper.deleteByExample(creditItemExample);
        // 删除用户信息
        if(userMapper.deleteByPrimaryKey(userDTO.getId()) == 0){
            return ResponseDTO.errorByMsg(CodeMsg.USER_DELETE_ERROR);
        }
        return ResponseDTO.successByMsg(true, "删除成功！");
    }

    /**
     * 用户注册操作
     * @param userDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> register(UserDTO userDTO) {
        if(CommonUtil.isEmpty(userDTO.getCaptcha())){
            return ResponseDTO.errorByMsg(CodeMsg.CAPTCHA_EMPTY);
        }
        // 比对验证码是否正确
        String value = stringRedisTemplate.opsForValue().get((userDTO.getCorrectCaptcha()));
        if(CommonUtil.isEmpty(value)){
            return ResponseDTO.errorByMsg(CodeMsg.CAPTCHA_EXPIRED);
        }
        if(!value.toLowerCase().equals(userDTO.getCaptcha().toLowerCase())){
            return ResponseDTO.errorByMsg(CodeMsg.CAPTCHA_ERROR);
        }
        // 进行统一表单验证
        CodeMsg validate = ValidateEntityUtil.validate(userDTO);
        if(!validate.getCode().equals(CodeMsg.SUCCESS.getCode())){
            return ResponseDTO.errorByMsg(validate);
        }
        // 判断重复密码是否正确
        if(CommonUtil.isEmpty(userDTO.getRePassword())){
            return ResponseDTO.errorByMsg(CodeMsg.REPASSWORD_EMPTY);
        }
        if(!userDTO.getPassword().equals(userDTO.getRePassword())){
            return ResponseDTO.errorByMsg(CodeMsg.REPASSWORD_ERROR);
        }
        User user = CopyUtil.copy(userDTO, User.class);
        // 判断用户昵称是否存在
        if(isUsernameExist(user, "")){
            return ResponseDTO.errorByMsg(CodeMsg.USERNAME_EXIST);
        }
        // 设置主键id
        user.setId(UuidUtil.getShortUuid());
        // 角色为普通用户
        user.setRoleId(RoleEnum.STUDENT.getCode());
        // 保存数据到数据库中
        if(userMapper.insertSelective(user) == 0){
            return ResponseDTO.errorByMsg(CodeMsg.USER_REGISTER_ERROR);
        }
        return ResponseDTO.successByMsg(true, "注册成功，快登录体验吧！");
    }


    /**
     * 用户登录操作
     * @param userDTO
     * @return
     */
    @Override
    public ResponseDTO<UserDTO> login(UserDTO userDTO) {
        // 进行是否为空判断
        if(CommonUtil.isEmpty(userDTO.getUsername())){
            return ResponseDTO.errorByMsg(CodeMsg.USERNAME_EMPTY);
        }
        if(CommonUtil.isEmpty(userDTO.getPassword())){
            return ResponseDTO.errorByMsg(CodeMsg.PASSWORD_EMPTY);
        }
        if(CommonUtil.isEmpty(userDTO.getCaptcha())){
            return ResponseDTO.errorByMsg(CodeMsg.CAPTCHA_EMPTY);
        }
        if(CommonUtil.isEmpty(userDTO.getCorrectCaptcha())){
            return ResponseDTO.errorByMsg(CodeMsg.CAPTCHA_EXPIRED);
        }
        // 比对验证码是否正确
        String value = stringRedisTemplate.opsForValue().get((userDTO.getCorrectCaptcha()));
        if(CommonUtil.isEmpty(value)){
            return ResponseDTO.errorByMsg(CodeMsg.CAPTCHA_EXPIRED);
        }
        if(!value.toLowerCase().equals(userDTO.getCaptcha().toLowerCase())){
            return ResponseDTO.errorByMsg(CodeMsg.CAPTCHA_ERROR);
        }
        // 对比昵称和密码是否正确
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUsernameEqualTo(userDTO.getUsername()).andPasswordEqualTo(userDTO.getPassword());
        List<User> userList = userMapper.selectByExample(userExample);
        if(userList == null || userList.size() != 1){
            return ResponseDTO.errorByMsg(CodeMsg.USERNAME_PASSWORD_ERROR);
        }
        // 生成登录token并存入Redis中
        UserDTO selectedUserDto = CopyUtil.copy(userList.get(0), UserDTO.class);
        String token = UuidUtil.getShortUuid();
        selectedUserDto.setToken(token);
        //把token存入redis中 有效期1小时
        stringRedisTemplate.opsForValue().set("USER_" + token, JSON.toJSONString(selectedUserDto), 3600, TimeUnit.SECONDS);
        return ResponseDTO.successByMsg(selectedUserDto, "登录成功！");
    }

    /**
     * 获取当前登录用户
     * @param userDTO
     * @return
     */
    @Override
    public ResponseDTO<UserDTO> getLoginUser(UserDTO userDTO) {
        if(CommonUtil.isEmpty(userDTO.getToken())){
            return ResponseDTO.errorByMsg(CodeMsg.USER_SESSION_EXPIRED);
        }
        String value = stringRedisTemplate.opsForValue().get("USER_" + userDTO.getToken());
        if(CommonUtil.isEmpty(value)){
            return ResponseDTO.errorByMsg(CodeMsg.USER_SESSION_EXPIRED);
        }
        UserDTO selectedUserDTO = JSON.parseObject(value, UserDTO.class);
        return ResponseDTO.success(CopyUtil.copy(userMapper.selectByPrimaryKey(selectedUserDTO.getId()), UserDTO.class));
    }

    /**
     * 查询信誉积分明细数据
     * @param pageDTO
     * @return
     */
    @Override
    public ResponseDTO<PageDTO<CreditItemDTO>> viewCreditItem(PageDTO<CreditItemDTO> pageDTO) {
        CreditItemDTO searchCreditItemDTO = pageDTO.getSearchEntity();
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("userId", searchCreditItemDTO.getUserId());
        UserDTO searchUserDTO = searchCreditItemDTO.getUserDTO();
        if(searchUserDTO != null) {
            queryMap.put("username", searchUserDTO.getUsername());
        }
        // 不知道当前页多少，默认为第一页
        if(pageDTO.getPage() == null){
            pageDTO.setPage(1);
        }
        if(pageDTO.getSize() == null) {
            pageDTO.setSize(5);
        }
        if(pageDTO.getPaging() == 1) {
            // 分页
            PageHelper.startPage(pageDTO.getPage(), pageDTO.getSize());
        }
        List<CreditItem> creditItemList = myUserMapper.findCreditItemBySearch(queryMap);
        PageInfo<CreditItem> pageInfo = new PageInfo<>(creditItemList);
        // 获取数据的总数
        pageDTO.setTotal(pageInfo.getTotal());
        // 讲domain类型数据  转成 DTO类型数据
        List<CreditItemDTO> creditItemDTOList = CopyUtil.copyList(creditItemList, CreditItemDTO.class);
        for(CreditItemDTO creditItemDTO : creditItemDTOList) {
            User user = userMapper.selectByPrimaryKey(creditItemDTO.getUserId());
            creditItemDTO.setUserDTO(CopyUtil.copy(user, UserDTO.class));
        }
        pageDTO.setList(creditItemDTOList);
        return ResponseDTO.success(pageDTO);
    }

    /**
     * 撤销信誉积分明细操作
     * @param creditItemDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> cancelCreditItem(CreditItemDTO creditItemDTO) {
        if(CommonUtil.isEmpty(creditItemDTO.getId())) {
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        CreditItem creditItem =  creditItemMapper.selectByPrimaryKey(creditItemDTO.getId());
        User user = userMapper.selectByPrimaryKey(creditItem.getUserId());
        if(user == null) {
            return ResponseDTO.errorByMsg(CodeMsg.USER_NOT_EXIST);
        }
        if(creditItem.getRate() > 0) {
            // 减少积分
            if(user.getCreditRate() == 0) {
                return ResponseDTO.errorByMsg(CodeMsg.CREDIT_IS_0);
            }
        } else {
            // 增加积分
            if(user.getCreditRate() == 100) {
                return ResponseDTO.errorByMsg(CodeMsg.CREDIT_IS_100);
            }
        }
        creditItem.setState(CreditStateEnum.CANCEL.getCode());
        if(creditItemMapper.updateByPrimaryKeySelective(creditItem) == 0) {
            return ResponseDTO.errorByMsg(CodeMsg.CREDIT_STATE_EDIT_ERROR);
        }
        creditItem.setCreateTime(new Date());
        creditItem.setState(CreditStateEnum.OVER.getCode());
        creditItem.setRate(-creditItem.getRate());
        creditItem.setNowRate(user.getCreditRate() + creditItem.getRate());
        creditItem.setDescription("编号：" + creditItem.getId() + "的明细撤销，信誉积分还原！");
        creditItem.setId(UuidUtil.getShortUuid());
        user.setCreditRate(creditItem.getNowRate());
        userMapper.updateByPrimaryKeySelective(user);
        if(creditItemMapper.insertSelective(creditItem) == 0) {
            throw new RuntimeException(CodeMsg.CREDIT_ADD_ERROR.getMsg());
        }
        return ResponseDTO.successByMsg(true, "撤销成功！");
    }

    /**
     * 用户退出登录
     * @param userDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> logout(UserDTO userDTO) {
        if(!CommonUtil.isEmpty(userDTO.getToken())){
            // token不为空  清除redis中数据
            stringRedisTemplate.delete("USER_" + userDTO.getToken());
        }
        return ResponseDTO.successByMsg(true, "退出登录成功！");
    }

    /**
     * 获取用户总数
     * @return
     */
    @Override
    public ResponseDTO<Integer> getUserTotal() {
        return ResponseDTO.success(userMapper.countByExample(new UserExample()));
    }

    /**
     * 判断用户昵称是否重复
     * @param user
     * @param id
     * @return
     */
    public Boolean isUsernameExist(User user, String id) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUsernameEqualTo(user.getUsername());
        List<User> selectedUserList = userMapper.selectByExample(userExample);
        if(selectedUserList != null && selectedUserList.size() > 0) {
            if(selectedUserList.size() > 1){
                return true; //出现重名
            }
            if(!selectedUserList.get(0).getId().equals(id)) {
                return true; //出现重名
            }
        }
        return false;//没有重名
    }
}
