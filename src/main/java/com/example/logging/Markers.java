package com.example.logging;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class Markers {
    public static final Marker TRACE = MarkerFactory.getMarker("TRACE");
    public static final Marker EXCEPTION = MarkerFactory.getMarker("EXCEPTION");
    public static final Marker REMOTE_CALL = MarkerFactory.getMarker("REMOTE_CALL");
    public static final Marker AUDIT = MarkerFactory.getMarker("AUDIT");
    public static final Marker DEFAULT = MarkerFactory.getMarker("DEFAULT");
    public static final Marker PROGRESS = MarkerFactory.getMarker("PROGRESS");
}
