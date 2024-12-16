//package com.example.SecConfig;
//
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//
//
//@Component
//public class GatewayRequestInterceptor implements HandlerInterceptor {
//
//    private static final String AUTH_TOKEN = "ioj23uheou2982ns132423dq!@#123p82nu218";
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        String token = request.getHeader("X-Gateway-Token");
//        if (AUTH_TOKEN.equals(token)) {
//            return true; // Token matches, allow the request
//        }
//        response.setStatus(HttpStatus.FORBIDDEN.value());
//        return false;
//    }
//}
