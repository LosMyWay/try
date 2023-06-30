package com.yjq.programmer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 蔡中宇
 * @create 2023-04-10
 */

/**
 * 项目启动类
 */
@SpringBootApplication
@MapperScan("com.yjq.programmer.dao")
public class LibraryManageSystemApplication {
    public static void main( String[] args )
    {
        SpringApplication.run(LibraryManageSystemApplication.class, args);
    }
}
