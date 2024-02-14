package com.example.mychat.controller;

import com.example.mychat.common.ApplicationVariable;
import com.example.mychat.common.UserSessionTools;
import com.example.mychat.entity.Friend;
import com.example.mychat.entity.User;
import com.example.mychat.mapper.FriendMapper;
import com.example.mychat.mapper.UserMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @ClassName: UserController
 * @Description: 用户控制器
 * @Date 2024/2/5 22:42
 */
@Controller
@RequestMapping("user")
public class UserController {
   @Resource
   private UserMapper userMapper;
   @Resource
   private FriendMapper friendMapper;

    /**
     * 注册用户
     * @param user
     * @return
     * TODO:注册用户名重复并不返回前端待处理
     */
    @PostMapping("reg")
    @ResponseBody
    public Object reg(User user){
        // 非空校验
        if(user == null){
            return null;
        }

        // 执行sql操作
        if(userMapper.reg(user) < 1){
            return null;
        }

        user.setPassword("");
        return user;
    }

    /**
     * 验证登录
     * @param username
     * @param password
     * @param request
     */
    @PostMapping("login")
    @ResponseBody
    public Object login(@Param("username") String username,
                        @Param("password") String password,
                        HttpServletRequest request){
        // 非空校验
        if(!StringUtils.hasLength(username) ||
                !StringUtils.hasLength(password)){
            return null;
        }

        // 根据传入用户名获取用户所有信息
        User user = userMapper.getUserByName(username);

        // 判断查询结果是否合法
        if (user == null || user.getId() <= 0 || !password.equals(user.getPassword())) {
            return null;
        }

        // 创建会话
        HttpSession session = request.getSession(true);
        session.setAttribute(ApplicationVariable.SESSION_KEY_USER, user);

        user.setPassword("");
        return user;
    }

    /**
     * 获取当前用户的用户名
     * @param request
     */
    @PostMapping("getusername")
    @ResponseBody
    public Object getUserName(HttpServletRequest request){
        // 从请求中获取用户信息
        User user = UserSessionTools.getLoginUser(request);

        if(user == null){
            return null;
        }

        user.setPassword("");
        return user;
    }

    /**
     * 获取当前用户的所有好友
     * @param request
     */
    @PostMapping("friends")
    @ResponseBody
    public Object getFriends(HttpServletRequest request){
        int id = UserSessionTools.getLoginUserId(request);
        if(!(id >= 0)){
            return null;
        }

        List<Friend> list = friendMapper.getFriends(id);
        return list;
    }
}
