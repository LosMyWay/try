package com.yjq.programmer.enums;

/**
 * @author 蔡中宇
 * @create 2023-04-26
 */
public enum CreditStateEnum {

    OVER(1,"执行结束"),

    CANCEL(2,"已撤销"),

    ;

    Integer code;

    String desc;

    CreditStateEnum(Integer code, String desc) {
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
