package com.cs203.core;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class CoreApplicationTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void contextLoads() {
    }

    @Test
    void testDatabaseConnection() {
        assertNotNull(jdbcTemplate, "Failed to inject JdbcTemplate. Check your database configuration.");
        try {
            Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            assertNotNull(result, "Could not execute simple query. Check database connection.");
        } catch (Exception e) {
            throw new AssertionError("Exception during database test: " + e.getMessage(), e);
        }
    }
}
