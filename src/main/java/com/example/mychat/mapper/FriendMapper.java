package com.example.mychat.mapper;

import com.example.mychat.entity.Friend;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @ClassName: FriendMapper
 * @Description:
 * @Date 2024/2/14 16:05
 */
@Mapper
public interface FriendMapper {
    List<Friend> getFriends(int id);
}
