package pl.slovvik.logprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import pl.slovvik.logprocessor.model.EventLog;
import pl.slovvik.logprocessor.model.State;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static pl.slovvik.logprocessor.file.FileHelper.*;

@Slf4j
public class TestDataProducer {

    public static final String FILE_NAME = "logs.json";

    private static final int BATCH_SIZE = 100;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private File file;

    public TestDataProducer() {
        deleteFile(FILE_NAME);
        this.file = creatFile(FILE_NAME);
    }

    void produce(int multiplicator, FileSize fileSize) throws IOException {
        while (file.length() < fileSize.calculateSize(multiplicator)) {
            int batchCount = 0;
            List<EventLog> events = new ArrayList<>();
            while (batchCount < BATCH_SIZE) {
                String id = UUID.randomUUID().toString();
                LocalDateTime dateStart = LocalDateTime.now();
                int nanos = ThreadLocalRandom.current().nextInt(1000 * 1000 * 10);
                LocalDateTime dateStop = dateStart.plusNanos(nanos);
                EventLog start = new EventLog(id, State.STARTED, Timestamp.valueOf(dateStart));
                EventLog stop = new EventLog(id, State.FINISHED, Timestamp.valueOf(dateStop));
                events.add(start);
                log.info("Produced {}", start);
                events.add(stop);
                log.info("Produced {}", stop);
                log.info("Shuffle event list for custom order");
                Collections.shuffle(events);
                batchCount++;
            }
            for (EventLog event : events) {
                FileUtils.writeStringToFile(
                        file,
                        objectMapper.writeValueAsString(event) + NEW_LINE,
                        StandardCharsets.UTF_8,
                        true
                );
            }
        }
    }
}
