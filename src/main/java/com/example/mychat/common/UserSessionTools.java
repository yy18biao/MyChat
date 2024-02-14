package com.example.mychat.common;

import com.example.mychat.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * @ClassName: UserSessionTools
 * @Description: 用户会话操作类
 * @Date 2024/2/13 22:10
 */
public class UserSessionTools {
    public static User getLoginUser(HttpServletRequest request){
        Object user = null;
        HttpSession session = request.getSession();
        if (session != null && (user = session.getAttribute(ApplicationVariable.SESSION_KEY_USER)) != null) {
            return ((User) user);
        }
        return null;
    }
    /**
     * 从会话中获取用户id
     * @param request
     */
    public static int getLoginUserId(HttpServletRequest request){
        Object user = null;
        HttpSession session = request.getSession();
        if (session != null && (user = session.getAttribute(ApplicationVariable.SESSION_KEY_USER)) != null) {
            return ((User) user).getId();
        }
        return -1;
    }
}
