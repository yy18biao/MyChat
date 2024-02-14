package com.example.mychat.controller;

import com.example.mychat.entity.Message;
import com.example.mychat.mapper.MessageMapper;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @ClassName: MessageController
 * @Description:
 * @Date 2024/2/14 17:18
 */
@Controller
@RequestMapping("message")
public class MessageController {
    @Resource
    private MessageMapper mapper;

    @RequestMapping("getallmessage")
    @ResponseBody
    public Object getAllMessage(int sessionId){
       List<Message> messages = mapper.getAllMessageBySessionId(sessionId);

        Collections.reverse(messages);

        return messages;
    }
}
