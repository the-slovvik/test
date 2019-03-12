package pl.slovvik.logprocessor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static pl.slovvik.logprocessor.TestDataProducer.FILE_NAME;


@Slf4j
public class LogProcessorApplicationTests {

//    @Ignore
    @Test
    public void produceTestData() throws IOException {
        TestDataProducer producer = new TestDataProducer();
        producer.produce(10, FileSize.MB);
    }

    @Test
    public void testSplitFile() throws Exception {
        LineIterator lineIterator = FileUtils.lineIterator(new File(FILE_NAME), StandardCharsets.UTF_8.name());
        log.info("{}", lineIterator.nextLine().getBytes(StandardCharsets.UTF_8).length);
        log.info("{}", lineIterator.nextLine().getBytes(StandardCharsets.UTF_8).length);
        log.info("{}", lineIterator.nextLine().getBytes(StandardCharsets.UTF_8).length);
        log.info("{}", lineIterator.nextLine().getBytes(StandardCharsets.UTF_8).length);
        log.info("{}", lineIterator.nextLine().getBytes(StandardCharsets.UTF_8).length);
        log.info("{}", lineIterator.nextLine().getBytes(StandardCharsets.UTF_8).length);
        log.info("{}", lineIterator.nextLine().getBytes(StandardCharsets.UTF_8).length);
        log.info("{}", lineIterator.nextLine().getBytes(StandardCharsets.UTF_8).length);
        log.info("{}", lineIterator.nextLine().getBytes(StandardCharsets.UTF_8).length);
        log.info("{}", lineIterator.nextLine().getBytes(StandardCharsets.UTF_8).length);
        log.info("{}", lineIterator.nextLine().getBytes(StandardCharsets.UTF_8).length);

    }

}
