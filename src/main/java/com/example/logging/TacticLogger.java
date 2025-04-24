package com.example.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import org.slf4j.Marker;
import org.springframework.boot.json.JsonWriter;

public sealed interface TacticLogger permits DefaultLogger,
        TraceLogger,
        ExceptionLogger,
        RemoteCallLogger,
        AuditLogger {
    static TacticLogger getByMarker(Marker marker) {
        return switch (marker.getName()) {
            case "TRACE" -> new TraceLogger();
            case "EXCEPTION" -> new ExceptionLogger();
            case "REMOTE_CALL" -> new RemoteCallLogger();
            default -> new DefaultLogger();
        };
    }

    Marker marker();

    JsonWriter<ILoggingEvent> writer();
}
