package com.vinsguru.elasticsearch_playground;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class ElasticsearchPlaygroundApplicationTests {

	@Test
	void contextLoads() {
	}

}
