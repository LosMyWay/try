package com.yjq.programmer.dto;

import java.util.Date;

/**
 * @author 蔡中宇
 * @create 2023-04-24
 */
public class CreditItemDTO {

    private String id;

    private String userId;

    private UserDTO userDTO;

    private Integer rate;

    private Date createTime;

    private String description;

    private Integer nowRate;

    private Integer state;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getNowRate() {
        return nowRate;
    }

    public void setNowRate(Integer nowRate) {
        this.nowRate = nowRate;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
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
        sb.append(", userId=").append(userId);
        sb.append(", rate=").append(rate);
        sb.append(", createTime=").append(createTime);
        sb.append(", description=").append(description);
        sb.append(", nowRate=").append(nowRate);
        sb.append(", state=").append(state);
        sb.append(", userDTO=").append(userDTO);
        sb.append("]");
        return sb.toString();
    }
}
