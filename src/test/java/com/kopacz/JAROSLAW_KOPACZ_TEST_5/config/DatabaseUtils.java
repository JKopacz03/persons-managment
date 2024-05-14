package com.kopacz.JAROSLAW_KOPACZ_TEST_5.config;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseUtils {

    private final JdbcTemplate jdbcTemplate;

    public int countRecordsInDatabase() {
        String sql = "SELECT COUNT(*) FROM person";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
}
