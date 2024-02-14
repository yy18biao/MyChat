package com.example.mychat.config;


import com.example.mychat.controller.WebSocketController;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

/**
 * @ClassName: WebSocketConfig
 * @Description: webscoket注册拦截器规则
 * @Date 2024/2/14 21:59
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Resource
    private WebSocketController webSocketController;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketController, "/webSocket")
                // 注册这个拦截器可以将用户给 HttpSession 中添加的 Attribute 键值对往 WebSocketSession 里也添加一份.
                .addInterceptors(new HttpSessionHandshakeInterceptor());
    }
}
