package com.example.mychat.entity;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName: OnlineUserManager
 * @Description: 管理用户和websocket的会话之间联系
 * @Date 2024/2/14 23:40
 */
@Component
public class OnlineUserManager {
    private ConcurrentHashMap<Integer, WebSocketSession> sessions = new ConcurrentHashMap<>();

    // 用户上线
    public void online(int id, WebSocketSession session){
        if (sessions.get(id) != null){
            return;
        }
        sessions.put(id, session);
    }

    // 用户下线
    public void offline(int id, WebSocketSession session){
        WebSocketSession exitSession = sessions.get(id);
        if (exitSession == session){
            sessions.remove(id);
        }
    }

    // 根据用户id获取到 WebSocketSession
    public WebSocketSession getSession(int id) {
        return sessions.get(id);
    }
}
