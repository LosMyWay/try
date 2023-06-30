package com.yjq.programmer.interceptor;

import com.alibaba.fastjson.JSON;
import com.yjq.programmer.bean.CodeMsg;
import com.yjq.programmer.utils.CommonUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 前台用户登录拦截器
 * @author Administrator
 *
 */
@Component
public class UserLoginInterceptor implements HandlerInterceptor {


	@Resource
	private StringRedisTemplate stringRedisTemplate;

	@Override
	public boolean  preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		String type = request.getHeader("type");
		response.setCharacterEncoding("UTF-8");
		if("axios".equals(type)){
			// 说明是axios请求
			String token = request.getHeader("token");
			if(CommonUtil.isEmpty(token)){
				try {
					response.getWriter().write(JSON.toJSONString(CodeMsg.USER_SESSION_EXPIRED));
				} catch (IOException e) {
					e.printStackTrace();
				}
				return false;
			}else {
				// 和Redis中数据进行校验
				String value = stringRedisTemplate.opsForValue().get("USER_" + token);
				if(CommonUtil.isEmpty(value)){
					try {
						response.getWriter().write(JSON.toJSONString(CodeMsg.USER_SESSION_EXPIRED));
					} catch (IOException e) {
						e.printStackTrace();
					}
					return false;
				}
				return true;
			}
		}
		return true;
	}
}
