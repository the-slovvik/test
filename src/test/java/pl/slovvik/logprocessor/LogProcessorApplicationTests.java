package pl.slovvik.logprocessor;

import org.junit.Test;

import java.io.IOException;


public class LogProcessorApplicationTests {

    @Test
    public void contextLoads() throws IOException {
        TestDataProducer producer = new TestDataProducer();
        producer.produce(1, FileSize.MB);
    }

}
