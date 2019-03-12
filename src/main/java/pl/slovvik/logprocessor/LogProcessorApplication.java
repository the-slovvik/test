package pl.slovvik.logprocessor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.slovvik.logprocessor.model.CompletedEvent;
import pl.slovvik.logprocessor.model.Event;
import pl.slovvik.logprocessor.repository.CompletedEventRepository;
import pl.slovvik.logprocessor.repository.EventRepository;
import pl.slovvik.logprocessor.service.EventLogProcessorService;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@SpringBootApplication
public class LogProcessorApplication implements CommandLineRunner {

    private final EventLogProcessorService eventLogProcessorService;
    private final EventRepository eventRepository;
    private final CompletedEventRepository completedEventRepository;

    public static void main(String[] args) {
        SpringApplication.run(LogProcessorApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        eventLogProcessorService.processLogs();
//        Thread.sleep(2000);
//        String id = "test";
//        Event start = new Event(id, Timestamp.valueOf(LocalDateTime.now()));
//        eventRepository.save(start);
//        log.info("Before edit {}", eventRepository.findById(id));
//        Event stop = new Event();
//        stop.setId(id);
//        Thread.sleep(2000);
//        stop.setFinished(Timestamp.valueOf(LocalDateTime.now()));
//        eventRepository.save(stop);
//        log.info("After edit");
//        Iterable<Event> all = eventRepository.findAll();
//        all.forEach(System.out::println);
//        Iterable<CompletedEvent> completedEventRepositoryAll = completedEventRepository.findAll();
//        completedEventRepositoryAll.forEach(System.out::println);

    }
}

