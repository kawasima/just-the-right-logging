package com.example.web.interceptor;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TraceLoggingAdvice {
    private static final Marker MARKER = MarkerFactory.getMarker("TRACE");

    @Before("within(com.example.web.controller.*)")
    public void logEnterMethod(JoinPoint jp) {
        Class<?> targetClass = jp.getSignature().getDeclaringType();
        Logger logger = LoggerFactory.getLogger(targetClass);

        logger.info(MARKER, "Enter {}.{}",
                targetClass.getSimpleName(),
                jp.getSignature().getName());
    }

    @After("within(com.example.web.controller.*)")
    public void logExitMethod(JoinPoint jp) {
        Class<?> targetClass = jp.getSignature().getDeclaringType();
        Logger logger = LoggerFactory.getLogger(targetClass);
        logger.info(MARKER, "Exit {}.{}",
                targetClass.getSimpleName(),
                jp.getSignature().getName());
    }
}
