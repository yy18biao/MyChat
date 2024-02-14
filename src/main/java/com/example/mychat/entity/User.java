package com.example.mychat.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName: User
 * @Description: 用户实体类
 * @Date 2024/2/5 21:38
 */
@Data
public class User {
    private int id;
    private String username;
    private String password;
    private String phone;
    private String email;
    private String photo;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") // 时间格式化
    private LocalDateTime createtime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") // 时间格式化
    private LocalDateTime updatetime;
}
