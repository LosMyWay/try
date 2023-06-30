package com.yjq.programmer.enums;

/**
 * @author 蔡中宇
 * @create 2023-04-26
 */
public enum SeatItemStateEnum {


    APPOINTMENT(1,"已预约"),

    USING(2,"使用中"),

    BREAK(3,"已违约"),

    CANCEL(4,"已取消"),

    FINISH(5,"已完成"),

    ;

    Integer code;

    String desc;

    SeatItemStateEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
