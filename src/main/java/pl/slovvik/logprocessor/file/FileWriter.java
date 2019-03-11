package pl.slovvik.logprocessor.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import pl.slovvik.logprocessor.model.EventLog;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static pl.slovvik.logprocessor.file.FileHelper.NEW_LINE;
import static pl.slovvik.logprocessor.file.FileHelper.creatFile;

@Slf4j
@Component
public class FileWriter {

    public final static String TEMP_FINISHED_LOG_FILE = "tempFinishedLog.json";

    private ObjectMapper objectMapper = new ObjectMapper();
    private File finishedEventLogFile;

    public FileWriter() {
        this.finishedEventLogFile = creatFile(TEMP_FINISHED_LOG_FILE);
    }

    public void writeFinishedEventLogs(List<EventLog> eventLogs) {
        for (EventLog eventLog : eventLogs) {
            try {
                FileUtils.writeStringToFile(
                        finishedEventLogFile,
                        objectMapper.writeValueAsString(eventLog) + NEW_LINE,
                        StandardCharsets.UTF_8,
                        true
                );
            } catch (IOException e) {
                log.error("Cannot write EventLog: {} caused by {}", eventLog, e);
            }
        }
    }
}
