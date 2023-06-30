package com.yjq.programmer.enums;

/**
 * @author 蔡中宇
 * @create 2023-04-24
 */
public enum BookStateEnum {

    WAIT_RENTAL(1,"待借阅"),

    RENTAL(2,"已借阅"),

    OFF_SHELVES(3,"已下架"),

    ;

    Integer code;

    String desc;

    BookStateEnum(Integer code, String desc) {
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
