package com.example.web.interceptor;

import com.example.web.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

@Component
public class MdcSetupInterceptor implements HandlerInterceptor {
    private static final String USER_AGENT = "User-Agent";
    private static final String MDC_USER_AGENT = "UserAgent";
    private static final String MDC_USER_ID    = "UserId";

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        Optional.ofNullable(request.getHeader(USER_AGENT))
                .ifPresent(ua -> MDC.put(MDC_USER_AGENT, ua));

        Optional.ofNullable(request.getRemoteAddr())
                .ifPresent(ip -> MDC.put("ipAddress", ip));


        // 2) Spring Security などから認可済みのユーザ ID を取得して MDC にセット
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof Member ud) {
            MDC.put(MDC_USER_ID, ud.getUsername());  // あるいは .getId()
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) throws Exception {
        MDC.remove(MDC_USER_AGENT);
        MDC.remove("ipAddress");
        MDC.remove(MDC_USER_ID);
    }
}
