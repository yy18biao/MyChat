package com.example.mychat.entity;

import lombok.Data;

/**
 * @ClassName: MessageResponse
 * @Description: 一个消息响应类
 * @Date 2024/2/14 23:54
 */
@Data
public class MessageResponse {
    private String type = "message";
    private int fromId;
    private String fromName;
    private int sessionId;
    private String content;
}
