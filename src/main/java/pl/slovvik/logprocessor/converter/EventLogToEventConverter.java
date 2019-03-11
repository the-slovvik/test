package pl.slovvik.logprocessor.converter;

import org.springframework.stereotype.Component;
import pl.slovvik.logprocessor.model.Event;
import pl.slovvik.logprocessor.model.EventLog;
import pl.slovvik.logprocessor.model.State;

@Component
public class EventLogToEventConverter implements Converter<EventLog, Event> {

    @Override
    public Event convert(EventLog eventLog) {
        Event event = new Event();
        event.setId(eventLog.getId());
        if (eventLog.getState().equals(State.STARTED)) {
            event.setStarted(eventLog.getTimestamp());
        } else {
            event.setFinished(eventLog.getTimestamp());
        }
        event.setType(eventLog.getType());
        event.setHost(eventLog.getHost());
        return event;
    }
}
