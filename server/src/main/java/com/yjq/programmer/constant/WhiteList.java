package com.yjq.programmer.constant;

import java.util.Arrays;
import java.util.List;

/**
 * @author 蔡中宇
 * @create 2023-04-19
 */
public class WhiteList {

    //登录拦截器无需拦截的url      Arrays.asList：字符串数组转化为List
    public static List<String> LoginExcludePathPatterns = Arrays.asList(
            "/user/login",
            "/user/register",
            "/captcha/generate_captcha"
    );
}
