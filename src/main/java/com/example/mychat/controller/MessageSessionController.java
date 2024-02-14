package com.example.mychat.controller;

import com.example.mychat.common.UserSessionTools;
import com.example.mychat.entity.Friend;
import com.example.mychat.entity.MessageSession;
import com.example.mychat.mapper.MessageMapper;
import com.example.mychat.mapper.MessageSessionMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: MessageSessionController
 * @Description:
 * @Date 2024/2/14 16:16
 */
@Controller
@RequestMapping("session")
public class MessageSessionController {
    @Resource
    private MessageSessionMapper sessionMapper;
    @Resource
    private MessageMapper messageMapper;

    @RequestMapping("getsessions")
    @ResponseBody
    public Object getSessions(HttpServletRequest request) {
        // 获取当前用户的id
        int id = UserSessionTools.getLoginUserId(request);

        List<Integer> sessionIdList = sessionMapper.getSessionIdsByUserId(id);

        List<MessageSession> list = new ArrayList<>();
        for (int sessionId : sessionIdList) {
            MessageSession messageSession = new MessageSession();
            messageSession.setSessionId(sessionId);

            List<Friend> friendList = sessionMapper.getFriendsBySessionId(sessionId, id);
            messageSession.setFriends(friendList);

            String lastMessage = messageMapper.getLastMessageBySessionId(sessionId);
            if(lastMessage == null){
                messageSession.setLastMessage("");
            }else {
                messageSession.setLastMessage(lastMessage);
            }

            list.add(messageSession);
        }

        return list;
    }

    @RequestMapping("createsession")
    @ResponseBody
    public Object CreateSession(HttpServletRequest request,
                                @Param("friendId") int friendId){
        // 获取当前用户的id
        int id = UserSessionTools.getLoginUserId(request);

        // 创建新的会话
        MessageSession messageSession = new MessageSession();
        int sessionId = sessionMapper.addMessageSession(messageSession);

        // 添加用户和会话的关联信息
        sessionMapper.addMessageSessionUser(messageSession.getSessionId(), friendId);
        sessionMapper.addMessageSessionUser(messageSession.getSessionId(), id);

        return messageSession.getSessionId();
    }
}
