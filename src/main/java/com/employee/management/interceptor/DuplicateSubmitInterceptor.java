package com.employee.management.interceptor;

import com.employee.management.annotation.PreventDuplicateSubmit;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DuplicateSubmitInterceptor implements HandlerInterceptor {

    private final ConcurrentHashMap<String, Long> requestMap = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        PreventDuplicateSubmit annotation = method.getAnnotation(PreventDuplicateSubmit.class);
        if (annotation == null) {
            return true;
        }

        String key = generateKey(request);
        Long lastTime = requestMap.get(key);
        long currentTime = System.currentTimeMillis();

        if (lastTime != null && (currentTime - lastTime) < annotation.interval()) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":400,\"message\":\"" + annotation.message() + "\"}");
            return false;
        }

        requestMap.put(key, currentTime);
        
        requestMap.entrySet().removeIf(entry -> currentTime - entry.getValue() > 10000);

        return true;
    }

    private String generateKey(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        String authToken = request.getHeader("Authorization");
        
        if (authToken == null || authToken.isEmpty()) {
            authToken = request.getSession().getId();
        }
        
        return method + ":" + uri + ":" + authToken;
    }
}

