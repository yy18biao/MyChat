package com.example.mychat.mapper;

import com.example.mychat.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @ClassName: UserMapper
 * @Description: 声明用户相关的sql方法
 * @Date 2024/2/5 22:12
 */
@Mapper
public interface UserMapper {
    /**
     * 注册用户
     * @param user
     * @return
     */
    int reg(User user);

    /**
     * 根据用户名获取用户
     * @param username
     * @return
     */
    User getUserByName(@Param("username") String username);
}
