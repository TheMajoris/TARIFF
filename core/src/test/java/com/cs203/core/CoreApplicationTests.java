package com.cs203.core;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class) // # TODO: remove once we connec db
class CoreApplicationTests {

	@Test
	void contextLoads() {
	}

}
