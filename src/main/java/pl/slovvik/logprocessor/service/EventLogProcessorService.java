package pl.slovvik.logprocessor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.slovvik.logprocessor.converter.EventLogToEventConverter;
import pl.slovvik.logprocessor.file.FileHelper;
import pl.slovvik.logprocessor.file.FileReader;
import pl.slovvik.logprocessor.file.FileWriter;
import pl.slovvik.logprocessor.model.Event;
import pl.slovvik.logprocessor.model.EventLog;
import pl.slovvik.logprocessor.model.State;
import pl.slovvik.logprocessor.repository.EventRepository;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static pl.slovvik.logprocessor.file.FileWriter.TEMP_FINISHED_LOG_FILE;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventLogProcessorService {

    private final FileReader fileReader;
    private final FileWriter fileWriter;
    private final EventRepository eventRepository;
    private final EventLogToEventConverter eventLogToEventConverter;

    public void processLogs() {
        fileReader.readAndPerform(this::saveStartedEventLogBatch);
//        writeBackFinishedEventLogsToFile();
        fileReader.setLogs(new File(TEMP_FINISHED_LOG_FILE));
        fileReader.initLineIterator();
        fileReader.readAndPerform(this::updateSavedStartedEventLog);
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
        fileWriter.writeFinishedEventLogs(eventList);
    }

    private void updateSavedStartedEventLog(List<EventLog> eventList) {
        Iterable<Event> eventsWithStartedStatus = eventRepository.findAllById(eventList.stream()
                .map(EventLog::getId)
                .collect(Collectors.toList()));
        eventsWithStartedStatus.forEach(event -> {
            Optional<EventLog> foundEvent = eventList.parallelStream()
                    .filter(eventLog -> eventLog.getId().equals(event.getId()))
                    .findFirst();
            foundEvent.ifPresent(eventLog -> {
                event.setFinished(eventLog.getTimestamp());
                eventList.remove(eventLog);

            });
        });
        eventRepository.saveAll(eventsWithStartedStatus);
    }

    private void covertAndSave(State state, List<EventLog> eventList) {
        log.info("Converting {} EventLogs to Events", state);
        List<Event> events = eventLogToEventConverter.convert(eventList);
        log.info("Saved {} batch events", state);
        this.eventRepository.saveAll(events);
    }

}
