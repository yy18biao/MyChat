package com.example.mychat.entity;

import lombok.Data;

/**
 * @ClassName: MessageRequest
 * @Description: 一个消息请求类
 * @Date 2024/2/14 23:54
 */
@Data
public class MessageRequest {
    private String type = "message";
    private int sessionId;
    private String content;
}
