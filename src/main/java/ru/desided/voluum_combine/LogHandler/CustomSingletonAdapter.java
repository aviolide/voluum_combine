package ru.desided.voluum_combine.LogHandler;

import org.apache.log4j.spi.LoggingEvent;

import java.util.ArrayList;

//https://gist.github.com/kengelke/4664612
public class CustomSingletonAdapter {

    private static final CustomSingletonAdapter theInstance = new CustomSingletonAdapter();

    ArrayList<LoggingEvent> eventsList;

    private CustomSingletonAdapter(){
        eventsList = new ArrayList<>();
    }

    public static CustomSingletonAdapter getTheInstance() {
        return theInstance;
    }

    public void append(LoggingEvent loggingEvent){
        if (!eventsList.contains(loggingEvent)) {
            eventsList.add(loggingEvent);
        }
    }

    public ArrayList<LoggingEvent> getEventsList() {
        return eventsList;
    }
}
