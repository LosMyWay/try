package com.yjq.programmer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yjq.programmer.annotation.ValidateEntity;

import java.util.Date;

/**
 * @author 蔡中宇
 * @create 2023-04-24
 */
public class SeatDTO {

    private String id;

    @ValidateEntity(required=true,requiredMinValue=true,requiredMaxValue=true,minValue=1, maxValue=8,errorRequiredMsg="座位行数不能为空！",errorMaxValueMsg="座位行数不能大于8行！",errorMinValueMsg="座位行数不能小于1行！")
    private Integer row;

    @ValidateEntity(required=true,requiredMinValue=true,requiredMaxValue=true,minValue=1, maxValue=8,errorRequiredMsg="座位列数不能为空！",errorMaxValueMsg="座位列数不能大于8列！",errorMinValueMsg="座位列数不能小于1列！")
    private Integer col;

    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date openTime;

    private String scheduleId;

    private ScheduleDTO scheduleDTO;

    private Integer totalNum;

    private Integer pickNum;

    private Integer version;

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

    public Date getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Date openTime) {
        this.openTime = openTime;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public ScheduleDTO getScheduleDTO() {
        return scheduleDTO;
    }

    public void setScheduleDTO(ScheduleDTO scheduleDTO) {
        this.scheduleDTO = scheduleDTO;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public Integer getPickNum() {
        return pickNum;
    }

    public void setPickNum(Integer pickNum) {
        this.pickNum = pickNum;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
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
        sb.append(", openTime=").append(openTime);
        sb.append(", scheduleId=").append(scheduleId);
        sb.append(", scheduleDTO=").append(scheduleDTO);
        sb.append(", totalNum=").append(totalNum);
        sb.append(", pickNum=").append(pickNum);
        sb.append(", version=").append(version);
        sb.append("]");
        return sb.toString();
    }
}
