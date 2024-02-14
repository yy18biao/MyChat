package com.example.mychat.mapper;

import com.example.mychat.entity.Friend;
import com.example.mychat.entity.MessageSession;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @ClassName: MessageSessionMapper
 * @Description:
 * @Date 2024/2/14 16:12
 */
@Mapper
public interface MessageSessionMapper {
    List<Integer> getSessionIdsByUserId(int id);

    List<Friend> getFriendsBySessionId(int sessionId, int id);

    int addMessageSession(MessageSession messageSession);

    void addMessageSessionUser(int sessionId, int id);
}
