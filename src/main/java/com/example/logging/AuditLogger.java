package com.example.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.boot.json.JsonWriter;

public final class AuditLogger implements TacticLogger {
    static final Marker MARKER = MarkerFactory.getMarker("AUDIT");

    private static final JsonWriter<ILoggingEvent> WRITER = JsonWriter.<ILoggingEvent>of(members -> {
                members.add("time", ILoggingEvent::getInstant);
                members.add("traceId", event -> event.getMDCPropertyMap().get("traceId"));
                members.add("level", ILoggingEvent::getLevel);
                members.add("logger", ILoggingEvent::getLoggerName);
                members.add("thread", ILoggingEvent::getThreadName);
                members.add("message", ILoggingEvent::getFormattedMessage);
            })
            .withNewLineAtEnd();

    @Override
    public Marker marker() {
        return MARKER;
    }

    @Override
    public JsonWriter<ILoggingEvent> writer() {
        return WRITER;
    }

}
