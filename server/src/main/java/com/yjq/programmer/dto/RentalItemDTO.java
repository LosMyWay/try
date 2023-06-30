package com.yjq.programmer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yjq.programmer.annotation.ValidateEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 蔡中宇
 * @create 2023-04-24
 */
public class RentalItemDTO {

    private String id;

    @ValidateEntity(required=true,errorRequiredMsg="图书名称不能为空！")
    private String bookId;

    @ValidateEntity(required=true,errorRequiredMsg="借阅用户不能为空！")
    private String userId;

    private UserDTO userDTO;

    private Date createTime;

    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date returnTime;

    @ValidateEntity(required=true,errorRequiredMsg="借阅状态不能为空！")
    private Integer state;

    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    @ValidateEntity(required=true,errorRequiredMsg="预计归还时间不能为空！")
    private Date predictTime;

    @ValidateEntity(requiredMaxValue=true,requiredMinValue=true,maxValue=999999.99,minValue=0,errorMaxValueMsg="赔偿金额不能大于999999.99元！",errorMinValueMsg="赔偿金额不能小于0.00元！")
    private BigDecimal money;

    @ValidateEntity(requiredMaxLength=true,maxLength=64,errorMaxLengthMsg="备注长度不能大于64！")
    private String note;

    private String bookName;

    private String bookPhoto;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(Date returnTime) {
        this.returnTime = returnTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getPredictTime() {
        return predictTime;
    }

    public void setPredictTime(Date predictTime) {
        this.predictTime = predictTime;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookPhoto() {
        return bookPhoto;
    }

    public void setBookPhoto(String bookPhoto) {
        this.bookPhoto = bookPhoto;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", bookId=").append(bookId);
        sb.append(", userId=").append(userId);
        sb.append(", createTime=").append(createTime);
        sb.append(", returnTime=").append(returnTime);
        sb.append(", state=").append(state);
        sb.append(", predictTime=").append(predictTime);
        sb.append(", money=").append(money);
        sb.append(", note=").append(note);
        sb.append(", bookName=").append(bookName);
        sb.append(", bookPhoto=").append(bookPhoto);
        sb.append(", userDTO=").append(userDTO);
        sb.append("]");
        return sb.toString();
    }
}
