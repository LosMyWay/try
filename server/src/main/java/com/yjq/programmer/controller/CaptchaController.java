package com.yjq.programmer.controller;

import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.utils.CaptchaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author 蔡中宇
 * @create 2023-04-19
 */

/**
 * 验证码Captcha控制类
 */
@RequestMapping("/captcha")
@RestController
public class CaptchaController {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private Logger log = LoggerFactory.getLogger(CaptchaController.class);

    /**
     * 通用验证码生成器
     */
    @PostMapping("/generate_captcha")
    public ResponseDTO<String> generateCaptcha(){
        CaptchaUtil captchaUtil = new CaptchaUtil();
        String generatorVCode = captchaUtil.generatorVCode(); //验证码的值
        // 存入Redis 定时60s
        stringRedisTemplate.opsForValue().set(generatorVCode, generatorVCode, 60, TimeUnit.SECONDS);
        log.info("验证码成功生成：" + generatorVCode);
        return ResponseDTO.success(generatorVCode);
    }

}
