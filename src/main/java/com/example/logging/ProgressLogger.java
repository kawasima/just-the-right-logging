package com.example.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.slf4j.event.Level;
import org.slf4j.helpers.AbstractLogger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class ProgressLogger extends AbstractLogger {
    private final static Marker MARKER = MarkerFactory.getMarker("PROGRESS");
    private final Logger delegate;
    private long targetCount;
    private LocalDateTime startDateTime;

    private ProgressLogger(Logger delegate) {
        super();
        this.delegate = delegate;
    }

    public static ProgressLogger getLogger(Class<?> clazz) {
        return new ProgressLogger(LoggerFactory.getLogger(clazz));
    }

    public void start(long targetCount) {
        this.targetCount = targetCount;
        this.startDateTime = LocalDateTime.now();
        LocalDateTime expected = null;
        info("処理を開始します");
        info( "総処理対象は{}件で、終了予想時刻は{}です。", targetCount,
                expected == null ? "不明" : DateTimeFormatter.ofPattern("HH:mm:ss").format(expected));
    }

    public void progress(long completedCount) {
        if (completedCount <= 0) return;

        LocalDateTime current = LocalDateTime.now();
        long elaspeSec = startDateTime.until(current, ChronoUnit.SECONDS);
        double unitSec = (double) elaspeSec / (double) completedCount;
        long remainSec = (long) (unitSec * (targetCount - completedCount));

        info( "{}件({}%)処理しました。現時点での終了予想時刻は{}です。",
                completedCount,
                ((double) completedCount * 100)/ (double) targetCount,
                DateTimeFormatter.ofPattern("HH:mm:ss").format(current.plusSeconds(remainSec)));
    }

    public void end() {
        info("処理を終了しました。");
    }

    @Override
    protected String getFullyQualifiedCallerName() {
        return delegate.getName();
    }

    @Override
    protected void handleNormalizedLoggingCall(Level level, Marker marker, String messagePattern, Object[] arguments, Throwable throwable) {
        delegate.atLevel(level)
                .addMarker(marker == null ? MARKER : marker)
                .log(messagePattern, arguments, throwable);
    }

    @Override
    public boolean isTraceEnabled() {
        return delegate.isTraceEnabled();
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return delegate.isTraceEnabled(marker);
    }

    @Override
    public boolean isDebugEnabled() {
        return delegate.isDebugEnabled();
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return delegate.isDebugEnabled(marker);
    }

    @Override
    public boolean isInfoEnabled() {
        return delegate.isInfoEnabled();
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return delegate.isInfoEnabled(marker);
    }

    @Override
    public boolean isWarnEnabled() {
        return delegate.isWarnEnabled();
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return delegate.isWarnEnabled(marker);
    }

    @Override
    public boolean isErrorEnabled() {
        return delegate.isErrorEnabled();
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return delegate.isErrorEnabled(marker);
    }
}
