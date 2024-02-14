package com.example.mychat.mapper;

import com.example.mychat.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @ClassName: MessageMapper
 * @Description:
 * @Date 2024/2/14 17:15
 */
@Mapper
public interface MessageMapper {
    /**
     * 获取会话中的最后一条消息
     * @param sessionId
     * @return
     */
    String getLastMessageBySessionId(int sessionId);

    List<Message> getAllMessageBySessionId(int sessionId);

    void add(Message message);
}
