package pl.slovvik.logprocessor.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import pl.slovvik.logprocessor.model.EventLog;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
@Data
public class FileReader {

    private final int batchSize;

    private File logs;
    private LineIterator lineIterator;
    private ObjectMapper objectMapper = new ObjectMapper();

    public FileReader(int batchSize, File logs) {
        this.batchSize = batchSize;
        this.logs = logs;
        initLineIterator();
    }

    public void readAndPerform(Consumer<List<EventLog>> action) {
        while (lineIterator.hasNext()) {
            try {
                action.accept(readBatch());
            } catch (IOException e) {
                log.error("Unable to read batch, caused by {}", e);
            }
        }
    }

    private List<EventLog> readBatch() throws IOException {
        log.info("Read batch events from log logs");
        List<EventLog> batch = new ArrayList<>(this.batchSize);
        while (lineIterator.hasNext()) {
            if (batch.size() == batchSize) {
                return batch;
            }
            EventLog event = objectMapper.readValue(lineIterator.nextLine(), EventLog.class);
            batch.add(event);
        }
        return batch;
    }

    public void initLineIterator() {
        try {
            this.lineIterator = FileUtils.lineIterator(this.logs, StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            log.error("Cannot creat line iterator, caused by {}", e);
        }
    }
}
