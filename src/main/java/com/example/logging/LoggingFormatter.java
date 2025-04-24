package com.example.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import org.springframework.boot.json.JsonWriter;
import org.springframework.boot.logging.structured.StructuredLogFormatter;

import java.util.List;
import java.util.Optional;

public class LoggingFormatter implements StructuredLogFormatter<ILoggingEvent> {
    private static final JsonWriter<ILoggingEvent> DEFAULT = new DefaultLogger().writer();

    @Override
    public String format(ILoggingEvent event) {
        return Optional.ofNullable(event.getMarkerList())
                .map(List::getFirst)
                .map(TacticLogger::getByMarker)
                .map(logger -> logger.writer().writeToString(event))
                .orElseGet(() -> DEFAULT.writeToString(event));
    }
}
