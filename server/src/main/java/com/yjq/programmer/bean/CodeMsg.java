package com.yjq.programmer.bean;

/**
 * @author 蔡中宇
 * @create 2023-04-19
 */

/**
 * 错误码统一处理类，所有的错误码统一定义在这里
 */
public class CodeMsg {

    private Integer code;//错误码

    private String msg;//错误信息

    /**
     * 该类负责创建自己的对象，同时确保只有单个对象被创建。这个类提供了一种访问其唯一的对象的方式，可以直接访问，不需要实例化该类的对象。
     * @param code
     * @param msg
     */
    private CodeMsg(Integer code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public CodeMsg() {

    }

    public Integer getCode() {
        return code;
    }



    public void setCode(Integer code) {
        this.code = code;
    }



    public String getMsg() {
        return msg;
    }



    public void setMsg(String msg) {
        this.msg = msg;
    }

    //通用错误码定义
    //处理成功消息码
    public static CodeMsg SUCCESS = new CodeMsg(0, "success");
    //通用数据错误码
    public static CodeMsg DATA_ERROR = new CodeMsg(-1, "非法数据！");
    public static CodeMsg VALIDATE_ENTITY_ERROR = new CodeMsg(-2, "");
    public static CodeMsg CAPTCHA_EMPTY = new CodeMsg(-3, "验证码不能为空!");
    public static CodeMsg NO_PERMISSION = new CodeMsg(-4, "您没有当前操作的权限哦！");
    public static CodeMsg CAPTCHA_ERROR = new CodeMsg(-5, "验证码错误！");
    public static CodeMsg USER_SESSION_EXPIRED = new CodeMsg(-6, "还未登录或会话失效，请重新登录！");
    public static CodeMsg UPLOAD_PHOTO_SUFFIX_ERROR = new CodeMsg(-7, "图片格式不正确！");
    public static CodeMsg PHOTO_SURPASS_MAX_SIZE = new CodeMsg(-8, "上传的图片不能超过2MB！");
    public static CodeMsg PHOTO_FORMAT_NOT_CORRECT = new CodeMsg(-9, "上传的图片格式不正确！");
    public static CodeMsg SAVE_FILE_EXCEPTION = new CodeMsg(-10, "保存文件异常！");
    public static CodeMsg FILE_EXPORT_ERROR = new CodeMsg(-11, "文件导出失败！");
    public static CodeMsg SYSTEM_ERROR = new CodeMsg(-12, "系统出现了错误，请联系管理员！");
    public static CodeMsg NO_AUTHORITY = new CodeMsg(-13, "不好意思，您没有权限操作哦！");
    public static CodeMsg CAPTCHA_EXPIRED = new CodeMsg(-14, "验证码已过期，请刷新验证码！");
    public static CodeMsg COMMON_ERROR = new CodeMsg(-15, "");
    public static CodeMsg PHOTO_EMPTY = new CodeMsg(-16, "上传的图片不能为空！");
    public static CodeMsg SYSTEM_BUSY = new CodeMsg(-17, "系统繁忙，请稍后再试！");


    //用户管理类错误码
    public static CodeMsg USER_ADD_ERROR = new CodeMsg(-1000, "用户信息添加失败，请联系管理员！");
    public static CodeMsg USER_NOT_EXIST  = new CodeMsg(-1001, "该用户不存在！");
    public static CodeMsg USER_EDIT_ERROR = new CodeMsg(-1002, "用户信息编辑失败，请联系管理员！");
    public static CodeMsg USER_DELETE_ERROR = new CodeMsg(-1003, "用户信息删除失败，请联系管理员！");
    public static CodeMsg USERNAME_EXIST = new CodeMsg(-1004, "用户昵称重复，请换一个！");
    public static CodeMsg USERNAME_EMPTY = new CodeMsg(-1005, "用户昵称不能为空！");
    public static CodeMsg PASSWORD_EMPTY = new CodeMsg(-1006, "用户密码不能为空！");
    public static CodeMsg USERNAME_PASSWORD_ERROR = new CodeMsg(-1007, "用户昵称或密码错误！");
    public static CodeMsg REPASSWORD_EMPTY = new CodeMsg(-1008, "确认密码不能为空！");
    public static CodeMsg REPASSWORD_ERROR = new CodeMsg(-1009, "确认密码不一致！");
    public static CodeMsg USER_REGISTER_ERROR = new CodeMsg(-1010, "注册用户失败，请联系管理员！");
    public static CodeMsg USER_NOT_IS_ADMIN = new CodeMsg(-1011, "只有管理员角色才能登录后台系统！");

    // 时刻管理类错误码
    public static CodeMsg SCHEDULE_ADD_ERROR = new CodeMsg(-2000, "时刻信息添加失败，请联系管理员！");
    public static CodeMsg SCHEDULE_EDIT_ERROR = new CodeMsg(-2001, "时刻信息编辑失败，请联系管理员！");

    // 座位管理类错误码
    public static CodeMsg SEAT_ADD_ERROR = new CodeMsg(-3000, "座位信息添加失败，请联系管理员！");
    public static CodeMsg SEAT_NOT_EXIST  = new CodeMsg(-3001, "该座位不存在！");
    public static CodeMsg SEAT_EDIT_ERROR = new CodeMsg(-3002, "座位信息编辑失败，请联系管理员！");
    public static CodeMsg SEAT_DELETE_ERROR = new CodeMsg(-3003, "座位信息删除失败，请联系管理员！");
    public static CodeMsg SEAT_ALREADY_EXIST = new CodeMsg(-3004, "此时间段下已存在座位信息，请勿重复添加！");
    public static CodeMsg SEAT_ALREADY_FULL = new CodeMsg(-3005, "预约失败，该时刻下座位已满！");
    public static CodeMsg SEAT_ALREADY_PICK = new CodeMsg(-3006, "来晚了一步，座位已被其他人预定！");
    public static CodeMsg SEAT_PICK_ERROR = new CodeMsg(-3007, "预约失败，请稍后再试！");
    public static CodeMsg SEAT_STATE_EDIT_ERROR = new CodeMsg(-3008, "状态修改失败，请联系管理员！");
    public static CodeMsg SEAT_ITEM_REMOVE_ERROR = new CodeMsg(-3008, "座位详情删除失败，请联系管理员！");
    public static CodeMsg SEAT_ITEM_BOOK_EXIST = new CodeMsg(-3009, "您已经预约过了，请勿重复预约！");

    // 信誉积分管理类错误码
    public static CodeMsg CREDIT_PICK_ERROR = new CodeMsg(-4000, "信誉积分小于80，无法选座！");
    public static CodeMsg CREDIT_ADD_ERROR = new CodeMsg(-4001, "信誉积分明细写入失败，请联系管理员！");
    public static CodeMsg CREDIT_STATE_EDIT_ERROR = new CodeMsg(-4002, "信誉积分状态修改失败，请联系管理员！");
    public static CodeMsg CREDIT_IS_0 = new CodeMsg(-4003, "该用户积分已为0，不可再减少，信誉积分撤销失败！");
    public static CodeMsg CREDIT_IS_100 = new CodeMsg(-4004, "该用户积分已为100，不可再增加，信誉积分撤销失败！");

    // 图书管理类错误码
    public static CodeMsg BOOK_ADD_ERROR = new CodeMsg(-5000, "图书信息添加失败，请联系管理员！");
    public static CodeMsg BOOK_NOT_EXIST  = new CodeMsg(-5001, "该图书不存在！");
    public static CodeMsg BOOK_EDIT_ERROR = new CodeMsg(-5002, "图书信息编辑失败，请联系管理员！");
    public static CodeMsg BOOK_DELETE_ERROR = new CodeMsg(-5003, "图书信息删除失败，请联系管理员！");
    public static CodeMsg CREDIT_RENTAL_ERROR = new CodeMsg(-5004, "信誉积分小于80，无法借阅！");
    public static CodeMsg BOOK_RENTAL_ERROR = new CodeMsg(-5005, "图书借阅失败，请联系管理员！");
    public static CodeMsg BOOK_RENTAL_TIME_OVER = new CodeMsg(-5006, "图书预计归还时间不能大于180天！");
    public static CodeMsg BOOK_OFF_SHELVES_ERROR = new CodeMsg(-5007, "图书在被借阅中，无法下架！");
    public static CodeMsg BOOK_WAIT_RENTAL_ERROR = new CodeMsg(-5007, "图书在被借阅中，无法改成待借阅状态！");

    // 借阅管理类错误码
    public static CodeMsg RENTAL_DELETE_ERROR = new CodeMsg(-6000, "借阅信息删除失败，请联系管理员！");
    public static CodeMsg RENTAL_RETURN_ERROR = new CodeMsg(-6001, "归还图书失败，请联系管理员！");
    public static CodeMsg RENTAL_BOOK_EXIST = new CodeMsg(-6002, "该用户存在借阅中的图书，无法删除用户！");
}
