package com.example.mychat.entity;

import lombok.Data;

import java.util.List;

/**
 * @ClassName: MessageSession
 * @Description:
 * @Date 2024/2/14 16:11
 */
@Data
public class MessageSession {
    private int sessionId;
    private List<Friend> friends;
    private String lastMessage;
}
