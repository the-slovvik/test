package pl.slovvik.logprocessor.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.slovvik.logprocessor.file.FileLogReader;

import java.io.File;

@Slf4j
@Configuration
public class Config {

    @Bean
    FileLogReader fileReader(
            @Value("${log.file}") String filePath,
            @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}") int batchSize) {
        log.info("File to read: {}", filePath);
        log.info("Batch size: {}", batchSize);
        return new FileLogReader(batchSize, new File(filePath));
    }
}
