package com.example.logging;

import org.slf4j.Logger;
import org.slf4j.event.Level;
import org.slf4j.spi.LoggingEventBuilder;

import java.util.Map;

public final class AuditLogger {
    private final Logger delegate;
    private AuditLogger(Logger loggerDelegate) {
        this.delegate = loggerDelegate;
    }
    public static AuditLogger getLogger(Logger loggerDelegate) {
        return new AuditLogger(loggerDelegate);
    }

    public void log(Map<String, Object> data) {
        LoggingEventBuilder builder = delegate.atLevel(Level.INFO)
                .addMarker(Markers.AUDIT);
        for(Map.Entry<String, Object> entry: data.entrySet()) {
            builder = builder.addKeyValue(entry.getKey(), entry.getValue());
        }
        builder.log("Audit log entry");
    }
}
