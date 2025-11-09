package com.cs203.core;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EntityScan("com.cs203.core.entity")
@EnableJpaRepositories("com.cs203.core.repository")
@EnableScheduling
public class CoreApplication {

    public static void main(String[] args) {
        // Set default timezone to Singapore time
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Singapore"));
        SpringApplication.run(CoreApplication.class, args);
    }

}
