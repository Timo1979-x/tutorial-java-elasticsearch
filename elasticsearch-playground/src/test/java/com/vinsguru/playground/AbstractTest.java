package com.vinsguru.playground;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ResourceLoader;

import java.util.function.Consumer;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AbstractTest {
    public static final Logger log = LoggerFactory.getLogger(AbstractTest.class);

    @Autowired
    ObjectMapper mapper;

    @Autowired
    ResourceLoader resourceLoader;

    protected <T> T readResource(String path, TypeReference<T> typeReference) {
        try {
            var file = resourceLoader.getResource("classpath:" + path).getFile();
            return mapper.readValue(file, typeReference);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected <T> Consumer<T> print() {
        return t -> log.info("{}", t);
    }
}
