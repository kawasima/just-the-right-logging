package com.example.web.interceptor;

import com.example.logging.Markers;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TraceLoggingAdvice {
    @Before("within(com.example.web.controller.*)")
    public void logEnterMethod(JoinPoint jp) {
        Class<?> targetClass = jp.getSignature().getDeclaringType();
        Logger logger = LoggerFactory.getLogger(targetClass);

        logger.info(Markers.TRACE, "Enter {}.{}",
                targetClass.getSimpleName(),
                jp.getSignature().getName());
    }

    @After("within(com.example.web.controller.*)")
    public void logExitMethod(JoinPoint jp) {
        Class<?> targetClass = jp.getSignature().getDeclaringType();
        Logger logger = LoggerFactory.getLogger(targetClass);
        logger.info(Markers.TRACE, "Exit {}.{}",
                targetClass.getSimpleName(),
                jp.getSignature().getName());
    }
}
