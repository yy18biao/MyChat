package com.example.mychat.entity;

import lombok.Data;

/**
 * @ClassName: Message
 * @Description:
 * @Date 2024/2/14 17:14
 */
@Data
public class Message {
    private int messageId;
    private int fromId;
    private String fromName;
    private int sessionId;
    private String content;
}
