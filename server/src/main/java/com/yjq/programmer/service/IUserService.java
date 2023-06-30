package com.yjq.programmer.service;

import com.yjq.programmer.dto.CreditItemDTO;
import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.dto.UserDTO;

/**
 * @author 蔡中宇
 * @create 2023-04-24
 */
public interface IUserService {

    // 分页获取用户数据
    ResponseDTO<PageDTO<UserDTO>> getUserListByPage(PageDTO<UserDTO> pageDTO);

    // 保存用户数据(添加、修改)
    ResponseDTO<Boolean> saveUser(UserDTO userDTO);

    // 删除用户数据
    ResponseDTO<Boolean> removeUser(UserDTO userDTO);

    // 用户注册操作
    ResponseDTO<Boolean> register(UserDTO userDTO);

    // 用户登录操作
    ResponseDTO<UserDTO> login(UserDTO userDTO);

    // 获取当前登录用户
    ResponseDTO<UserDTO> getLoginUser(UserDTO userDTO);

    // 查询信誉积分明细数据
    ResponseDTO<PageDTO<CreditItemDTO>> viewCreditItem(PageDTO<CreditItemDTO> pageDTO);

    // 撤销信誉积分明细操作
    ResponseDTO<Boolean> cancelCreditItem(CreditItemDTO creditItemDTO);

    // 用户退出登录
    ResponseDTO<Boolean> logout(UserDTO userDTO);

    // 获取用户总数
    ResponseDTO<Integer> getUserTotal();
}
