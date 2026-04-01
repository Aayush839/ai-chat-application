
package com.aichat.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.*;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    @Autowired
    private JwtService jwtService;

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes
    ) {

        if (request instanceof ServletServerHttpRequest servletRequest) {

            HttpServletRequest req = servletRequest.getServletRequest();

            String token = req.getParameter("token"); // 👈 from URL

            if (token != null) {
                try {
                    String username = jwtService.extractUsername(token);

                    // ✅ attach user to session
                    attributes.put("username", username);

                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return false; // ❌ reject connection
    }

    @Override
    public void afterHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Exception exception
    ) {
    }
}