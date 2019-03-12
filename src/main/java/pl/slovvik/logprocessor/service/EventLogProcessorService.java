package pl.slovvik.logprocessor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.slovvik.logprocessor.converter.EventLogToEventConverter;
import pl.slovvik.logprocessor.converter.EventToCompletedEventConverter;
import pl.slovvik.logprocessor.file.FileHelper;
import pl.slovvik.logprocessor.file.FileLogReader;
import pl.slovvik.logprocessor.file.FileLogWriter;
import pl.slovvik.logprocessor.model.CompletedEvent;
import pl.slovvik.logprocessor.model.Event;
import pl.slovvik.logprocessor.model.EventLog;
import pl.slovvik.logprocessor.model.State;
import pl.slovvik.logprocessor.repository.CompletedEventRepository;
import pl.slovvik.logprocessor.repository.EventRepository;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static pl.slovvik.logprocessor.file.FileLogWriter.TEMP_FINISHED_LOG_FILE;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventLogProcessorService {

    private final FileLogReader fileLogReader;
    private final FileLogWriter fileLogWriter;
    private final EventRepository eventRepository;
    private final CompletedEventRepository completedEventRepository;
    private final EventLogToEventConverter eventLogToEventConverter;
    private final EventToCompletedEventConverter eventToCompletedEventConverter;

    @Value("${log.flag.time}")
    private int flagTime;

    public void processLogs() {
        fileLogReader.readAndPerform(this::saveStartedEventLogBatch);
//        writeBackFinishedEventLogsToFile();
        fileLogReader.setLogs(new File(TEMP_FINISHED_LOG_FILE));
        fileLogReader.initLineIterator();
        fileLogReader.readAndPerform(this::updateSavedStartedEventLogs);
        FileHelper.deleteFile(TEMP_FINISHED_LOG_FILE);
    }

    private void saveStartedEventLogBatch(List<EventLog> eventList) {
        log.info("Filtering eventLogs to only {}", State.STARTED);
        List<EventLog> startedEventLogs = eventList.stream()
                .filter(eventLog -> eventLog.getState().equals(State.STARTED))
                .collect(Collectors.toList());
        covertAndSave(State.STARTED, startedEventLogs);
        eventList.removeAll(startedEventLogs);
        writeBackFinishedEventLogsToFile(eventList);
    }

    private void writeBackFinishedEventLogsToFile(List<EventLog> eventList) {
        fileLogWriter.writeFinishedEventLogs(eventList);
    }

    private void updateSavedStartedEventLogs(List<EventLog> eventList) {
        for (Event event : findEventByEventLog(eventList)) {
            Optional<EventLog> foundEvent = matchEventToEventLog(eventList, event);
            foundEvent.ifPresent(eventLog -> {
                event.setFinished(eventLog.getTimestamp());

                //Improve performance a little bit
                eventList.remove(eventLog);
                Optional<Event> filteredEvent = filterShorterEvent(this.flagTime, event);
                if (filteredEvent.isPresent()) {
                    CompletedEvent convert = eventToCompletedEventConverter.convert(event);
                    convert.setAlert(true);
                    completedEventRepository.save(convert);
                    eventRepository.delete(event);
                }
            });

        }
    }

    private Optional<EventLog> matchEventToEventLog(List<EventLog> eventList, Event event) {
        return eventList.parallelStream()
                        .filter(eventLog -> eventLog.getId().equals(event.getId()))
                        .findFirst();
    }

    private Iterable<Event> findEventByEventLog(List<EventLog> eventList) {
        return eventRepository.findAllById(eventList.stream()
                    .map(EventLog::getId)
                    .collect(Collectors.toList()));
    }

    private Optional<Event> filterShorterEvent(int ms, Event event) {
        if (event.getFinished().getTime() - event.getStarted().getTime() > ms) {
            log.info("Event {} longer than {} ms", event, ms);
            return Optional.of(event);
        }
        log.info("Event {} shorter than {} ms", event, ms);
        return Optional.empty();
    }

    private void covertAndSave(State state, List<EventLog> eventList) {
        log.info("Converting {} EventLogs to Events", state);
        List<Event> events = eventLogToEventConverter.convert(eventList);
        log.info("Saved {} batch events", state);
        this.eventRepository.saveAll(events);
    }

}
