package ru.desided.voluum_combine.LogHandler;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

public class CustomAppender extends AppenderSkeleton {

    private CustomSingletonAdapter adapter = CustomSingletonAdapter.getTheInstance();

    @Override
    protected void append(LoggingEvent loggingEvent) {
        adapter.append(loggingEvent);
    }

    @Override
    public void close() {

    }

    @Override
    public boolean requiresLayout() {
        return false;
    }

}