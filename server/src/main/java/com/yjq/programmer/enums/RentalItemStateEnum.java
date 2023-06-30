package com.yjq.programmer.enums;

/**
 * @author 蔡中宇
 * @create 2023-04-26
 */
public enum RentalItemStateEnum {

    RENTAL(1,"借阅中"),

    NORMAL_RETURN(2,"正常归还"),

    ILLEGAL_RETURN(3,"归还异常"),

    ;

    Integer code;

    String desc;

    RentalItemStateEnum(Integer code, String desc) {
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
