package com.yjq.programmer.dto;

import com.yjq.programmer.annotation.ValidateEntity;

/**
 * @author 蔡中宇
 * @create 2023-04-24
 */
public class UserDTO {

    private String id;

    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=8,minLength=1,errorRequiredMsg="用户昵称不能为空！",errorMaxLengthMsg="用户昵称长度不能大于8！",errorMinLengthMsg="用户昵称不能为空！")
    private String username;

    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=16,minLength=6,errorRequiredMsg="用户密码不能为空！",errorMaxLengthMsg="用户密码长度不能大于16！",errorMinLengthMsg="用户密码长度不能小于6！")
    private String password;

    private Integer roleId;

    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=11,minLength=11,errorRequiredMsg="手机号不能为空！",errorMaxLengthMsg="请输入11位手机号！",errorMinLengthMsg="请输入11位手机号！")
    private String phone;

    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=8,minLength=1,errorRequiredMsg="真实姓名不能为空！",errorMaxLengthMsg="真实姓名长度不能大于8！",errorMinLengthMsg="真实姓名不能为空！")
    private String realName;

    private Integer creditRate;

    private String headPic;

    private Integer sex;

    private String captcha;

    private String correctCaptcha;

    private String token; //用户携带的token

    private String rePassword;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Integer getCreditRate() {
        return creditRate;
    }

    public void setCreditRate(Integer creditRate) {
        this.creditRate = creditRate;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getCorrectCaptcha() {
        return correctCaptcha;
    }

    public void setCorrectCaptcha(String correctCaptcha) {
        this.correctCaptcha = correctCaptcha;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRePassword() {
        return rePassword;
    }

    public void setRePassword(String rePassword) {
        this.rePassword = rePassword;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", username=").append(username);
        sb.append(", password=").append(password);
        sb.append(", roleId=").append(roleId);
        sb.append(", phone=").append(phone);
        sb.append(", realName=").append(realName);
        sb.append(", creditRate=").append(creditRate);
        sb.append(", headPic=").append(headPic);
        sb.append(", sex=").append(sex);
        sb.append(", captcha=").append(captcha);
        sb.append(", correctCaptcha=").append(correctCaptcha);
        sb.append(", token=").append(token);
        sb.append(", rePassword=").append(rePassword);
        sb.append("]");
        return sb.toString();
    }
}
