package com.yjq.programmer.domain;

import java.util.Date;

public class SeatItem {
    private String id;

    private Integer row;

    private Integer col;

    private Date createTime;

    private String seatId;

    private String userId;

    private Integer state;

    private Date openTime;

    private String rangeTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getCol() {
        return col;
    }

    public void setCol(Integer col) {
        this.col = col;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getSeatId() {
        return seatId;
    }

    public void setSeatId(String seatId) {
        this.seatId = seatId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Date openTime) {
        this.openTime = openTime;
    }

    public String getRangeTime() {
        return rangeTime;
    }

    public void setRangeTime(String rangeTime) {
        this.rangeTime = rangeTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", row=").append(row);
        sb.append(", col=").append(col);
        sb.append(", createTime=").append(createTime);
        sb.append(", seatId=").append(seatId);
        sb.append(", userId=").append(userId);
        sb.append(", state=").append(state);
        sb.append(", openTime=").append(openTime);
        sb.append(", rangeTime=").append(rangeTime);
        sb.append("]");
        return sb.toString();
    }
}