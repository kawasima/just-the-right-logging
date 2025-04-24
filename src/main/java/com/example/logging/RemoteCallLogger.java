package com.example.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.boot.json.JsonWriter;

public final class RemoteCallLogger implements TacticLogger {
    public static final Marker MARKER = MarkerFactory.getMarker("REMOTE_CALL");

    private static final JsonWriter<ILoggingEvent> WRITER = JsonWriter.<ILoggingEvent>of(members -> {
                members.add("time", ILoggingEvent::getInstant);
                members.add("type", $ -> MARKER);
                members.add("traceId", event -> event.getMDCPropertyMap().get("traceId"));
                members.add("level", ILoggingEvent::getLevel);
                members.add("logger", ILoggingEvent::getLoggerName);
                members.add("thread", ILoggingEvent::getThreadName);
                members.add("exchangeType", event -> event.getMDCPropertyMap().get("exchangeType"));
                members.add("method", event -> event.getMDCPropertyMap().get("method"));
                members.add("uri", event -> event.getMDCPropertyMap().get("uri"));
                members.add("headers", event -> event.getMDCPropertyMap().get("headers"));
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
