package com.yjq.programmer.controller;

import com.yjq.programmer.dto.CreditItemDTO;
import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.dto.UserDTO;
import com.yjq.programmer.service.IUserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author 蔡中宇
 * @create 2023-04-19
 */
@RestController("UserController")
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    /**
     * 分页获取用户数据
     * @param pageDTO
     * @return
     */
    @PostMapping("/list")
    public ResponseDTO<PageDTO<UserDTO>> getUserListByPage(@RequestBody PageDTO<UserDTO> pageDTO){
        return userService.getUserListByPage(pageDTO);
    }

    /**
     * 保存用户数据(添加、修改)
     * @param userDTO
     * @return
     */
    @PostMapping("/save")
    public ResponseDTO<Boolean> saveUser(@RequestBody UserDTO userDTO){
        return userService.saveUser(userDTO);
    }

    /**
     * 后台删除用户数据
     * @param userDTO
     * @return
     */
    @PostMapping("/remove")
    public ResponseDTO<Boolean> removeUser(@RequestBody UserDTO userDTO){
        return userService.removeUser(userDTO);
    }

    /**
     * 用户注册操作处理
     * @param userDTO
     * @return
     */
    @PostMapping("/register")
    public ResponseDTO<Boolean> register(@RequestBody UserDTO userDTO){
        return userService.register(userDTO);
    }

    /**
     * 用户登录操作处理
     * @param userDTO
     * @return
     */
    @PostMapping("/login")
    public ResponseDTO<UserDTO> login(@RequestBody UserDTO userDTO){
        return userService.login(userDTO);
    }

    /**
     * 获取当前登录用户
     * @param userDTO
     * @return
     */
    @PostMapping("/getLoginUser")
    public ResponseDTO<UserDTO> getLoginUser(@RequestBody UserDTO userDTO){
        return userService.getLoginUser(userDTO);
    }

    /**
     * 查询信誉积分明细数据
     * @param pageDTO
     * @return
     */
    @PostMapping("/viewCreditItem")
    public ResponseDTO<PageDTO<CreditItemDTO>> viewCreditItem(@RequestBody PageDTO<CreditItemDTO> pageDTO) {
        return userService.viewCreditItem(pageDTO);
    }

    /**
     * 撤销信誉积分明细操作
     * @param creditItemDTO
     * @return
     */
    @PostMapping("/cancelCreditItem")
    public ResponseDTO<Boolean> cancelCreditItem(@RequestBody CreditItemDTO creditItemDTO) {
        return userService.cancelCreditItem(creditItemDTO);
    }

    /**
     * 用户退出登录
     * @return
     */
    @PostMapping("/logout")
    public ResponseDTO<Boolean> logout(@RequestBody UserDTO userDTO){
        return userService.logout(userDTO);
    }

    /**
     * 获取用户总数
     * @return
     */
    @PostMapping("/total")
    public ResponseDTO<Integer> getUserTotal(){
        return userService.getUserTotal();
    }
}
