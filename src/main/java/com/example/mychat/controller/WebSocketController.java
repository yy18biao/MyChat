package com.example.mychat.controller;

import com.example.mychat.common.ApplicationVariable;
import com.example.mychat.entity.*;
import com.example.mychat.mapper.MessageMapper;
import com.example.mychat.mapper.MessageSessionMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;

/**
 * @ClassName: WebSocketController
 * @Description:
 * @Date 2024/2/14 21:58
 */
@Controller
public class WebSocketController extends TextWebSocketHandler {
    @Resource
    private MessageMapper messageMapper;
    @Resource
    private MessageSessionMapper messageSessionMapper;
    @Resource
    private OnlineUserManager onlineUserManager;

    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 连接建立成功回调
     * @param session
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("连接成功!");

        User user = (User) session.getAttributes().get(ApplicationVariable.SESSION_KEY_USER);
        if(user == null){
            return;
        }

        onlineUserManager.online(user.getId(), session);
    }

    /**
     * 收到消息的时候回调
     * @param session
     * @param message
     * @throws Exception
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("收到消息!" + message.toString());

        // 先获取到当前用户的信息
        User user = (User) session.getAttributes().get(ApplicationVariable.SESSION_KEY_USER);
        if(user == null){
            return;
        }

        // 针对请求进行解析
        // 把 json 格式的字符串, 转成一个 Java 中的对象
        MessageRequest req = objectMapper.readValue(message.getPayload(), MessageRequest.class);
        if (req.getType().equals("message")) {
            // 就进行消息转发
            transferMessage(user, req);
        }
    }

    private void transferMessage(User user, MessageRequest req) throws IOException{
        // 先构造一个待转发的响应对象
        MessageResponse resp = new MessageResponse();
        resp.setFromId(user.getId());
        resp.setFromName(user.getUsername());
        resp.setSessionId(req.getSessionId());
        resp.setContent(req.getContent());

        // 把这个 java 对象转成 json 格式字符串
        String respJson = objectMapper.writeValueAsString(resp);

        // 根据请求中的 sessionId, 获取到这个 MessageSession 里的所有用户
        List<Friend> friends = messageSessionMapper.getFriendsBySessionId(req.getSessionId(), user.getId());
        // 由于会把当前发消息的用户给排除掉，但是发送的话也要给自身发送一份
        // 因此需要将当前用户添加进list里
        Friend myself = new Friend();
        myself.setFriendId(user.getId());
        myself.setFriendName(user.getUsername());
        friends.add(myself);

        // 循环遍历上述的这个列表, 给列表中的每个用户都发一份响应消息
        for (Friend friend : friends){
            WebSocketSession webSocketSession = onlineUserManager.getSession(friend.getFriendId());
            if (webSocketSession == null) {
                // 如果该用户未在线, 则不发送.
                continue;
            }

            webSocketSession.sendMessage(new TextMessage(respJson));
        }

        // 转发的消息, 还需要放到数据库里,等用户上线后可以通过历史记录拿到消息
        Message message = new Message();
        message.setFromId(user.getId());
        message.setSessionId(req.getSessionId());
        message.setContent(req.getContent());
        messageMapper.add(message);
    }

    /**
     * 异常回调
     * @param session
     * @param exception
     * @throws Exception
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.out.println("连接异常!");

        User user = (User) session.getAttributes().get(ApplicationVariable.SESSION_KEY_USER);
        if(user == null){
            return;
        }

        onlineUserManager.offline(user.getId(), session);
    }

    /**
     * 连接关闭回调
     * @param session
     * @param status
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("连接关闭!");

        User user = (User) session.getAttributes().get(ApplicationVariable.SESSION_KEY_USER);
        if(user == null){
            return;
        }

        onlineUserManager.offline(user.getId(), session);
    }
}
