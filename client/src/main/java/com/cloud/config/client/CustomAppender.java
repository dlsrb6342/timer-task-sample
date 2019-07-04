package com.cloud.config.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;

// Simple CustomAppender
public class CustomAppender extends ConsoleAppender<ILoggingEvent> {

    private final List<ILoggingEvent> loggingEvents = new ArrayList<>();
    private static final int BULK_SIZE = 100;

    @Override
    public void start() {
        Timer timer = new Timer(true);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                System.out.println("Pending logging events : " + loggingEvents.size());
                if (loggingEvents.size() > BULK_SIZE) {
                    // send ILoggingEvent to remote log collect server
                    loggingEvents.clear();
                }
            }
        };

        timer.schedule(timerTask, 1000, 1000);
        super.start();
    }

    @Override
    protected void append(ILoggingEvent eventObject) {
        loggingEvents.add(eventObject);
        super.append(eventObject);
    }
}