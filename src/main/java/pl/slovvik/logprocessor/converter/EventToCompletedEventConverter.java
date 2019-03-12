package pl.slovvik.logprocessor.converter;

import org.springframework.stereotype.Component;
import pl.slovvik.logprocessor.model.CompletedEvent;
import pl.slovvik.logprocessor.model.Event;

@Component
public class EventToCompletedEventConverter implements Converter<Event, CompletedEvent> {

    @Override
    public CompletedEvent convert(Event event) {
        return new CompletedEvent(
                event.getId(),
                event.getFinished().getTime() - event.getStarted().getTime(),
                event.getType(),
                event.getHost()
        );

    }
}
