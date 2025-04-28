package com.example.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import org.slf4j.Marker;
import org.springframework.boot.json.JsonWriter;
import org.springframework.boot.logging.structured.StructuredLogFormatter;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * 構造化ログで出力する時のフォーマッター.
 * <br/>
 * Markerに応じてフォーマッターを切り替える.
 *
 * @author kawasima
 */
public class LoggingFormatter implements StructuredLogFormatter<ILoggingEvent> {
    private static final Consumer<JsonWriter.Members<ILoggingEvent>> DEFAULT_CONSUMER = members -> {
        members.add("time", ILoggingEvent::getInstant);
        members.add("traceId", event -> event.getMDCPropertyMap().get("traceId"));
        members.add("level", ILoggingEvent::getLevel);
        members.add("logger", ILoggingEvent::getLoggerName);
        members.add("thread", ILoggingEvent::getThreadName);
        members.add("message", ILoggingEvent::getFormattedMessage);
    };

    private static final JsonWriter<ILoggingEvent> DEFAULT = JsonWriter.of(DEFAULT_CONSUMER)
            .withNewLineAtEnd();
    private static final JsonWriter<ILoggingEvent> EXCEPTION = JsonWriter.of(DEFAULT_CONSUMER.andThen(members ->
                    members.add("exception", event -> event.getThrowableProxy().getMessage())))
            .withNewLineAtEnd();

    private static final Map<Marker, JsonWriter<ILoggingEvent>> WRITERS = Map.of(
            Markers.DEFAULT, JsonWriter.of(DEFAULT_CONSUMER).withNewLineAtEnd(),
            Markers.EXCEPTION, JsonWriter.of(DEFAULT_CONSUMER.andThen(members -> {
                members.add("exception", event -> event.getThrowableProxy().getMessage());
            })).withNewLineAtEnd(),
            Markers.REMOTE_CALL, JsonWriter.of(DEFAULT_CONSUMER.andThen(members -> {
                members.add("exchangeType", event -> event.getMDCPropertyMap().get("exchangeType"));
                members.add("method", event -> event.getMDCPropertyMap().get("method"));
                members.add("uri", event -> event.getMDCPropertyMap().get("uri"));
                members.add("headers", event -> event.getMDCPropertyMap().get("headers"));
            })).withNewLineAtEnd(),
            Markers.AUDIT, JsonWriter.of(DEFAULT_CONSUMER.andThen(members -> {
                members.add("auditId", event -> event.getMDCPropertyMap().get("auditId"));
                members.add("userId", event -> event.getMDCPropertyMap().get("userId"));
            })).withNewLineAtEnd(),
            Markers.TRACE, JsonWriter.of(DEFAULT_CONSUMER.andThen(members -> {
                members.add("traceId", event -> event.getMDCPropertyMap().get("traceId"));
            })).withNewLineAtEnd()
    );

    @Override
    public String format(ILoggingEvent event) {
        return Optional.ofNullable(event.getMarkerList())
                .map(List::getFirst)
                .map(WRITERS::get)
                .map(writer -> writer.writeToString(event))
                .orElseGet(() -> DEFAULT.writeToString(event));
    }
}
