package com.example.web.interceptor;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.example.logging.Markers;
import com.example.web.controller.AdviceTestController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

import static org.junit.jupiter.api.Assertions.*;

class TraceLoggingAdviceTest {
    private AdviceTestController proxy;
    private ListAppender<ILoggingEvent> listAppender;

    @BeforeEach
    void setUp() {
        TraceLoggingAdvice advice = new TraceLoggingAdvice();
        AspectJProxyFactory factory = new AspectJProxyFactory(new AdviceTestController());
        factory.addAspect(advice);
        proxy = factory.getProxy();

        Logger logger = (Logger) LoggerFactory.getLogger(AdviceTestController.class);
        listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);
    }

    @AfterEach
    void tearDown() {
        listAppender.stop();
    }

    @Test
    void testTraceLogging() {
        // Act: メソッド呼び出し
        proxy.doAction();

        // Assert: Enter ログ
        boolean enterLogged = listAppender.list.stream()
                .anyMatch(event ->
                        event.getMarkerList() != null &&
                                event.getMarkerList().contains(Markers.TRACE) &&
                                event.getFormattedMessage().contains("Enter AdviceTestController.doAction")
                );
        assertTrue(enterLogged, "Enter ログが出力されているはず");

        // Assert: Exit ログ
        boolean exitLogged = listAppender.list.stream()
                .anyMatch(event ->
                        event.getMarkerList() != null &&
                                event.getMarkerList().contains(Markers.TRACE) &&
                                event.getFormattedMessage().contains("Exit AdviceTestController.doAction")
                );
        assertTrue(exitLogged, "Exit ログが出力されているはず");
    }
 }