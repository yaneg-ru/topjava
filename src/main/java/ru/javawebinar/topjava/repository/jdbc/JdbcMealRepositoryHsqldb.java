package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.Profiles;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Timer;

@Repository
@Profile(Profiles.HSQL_DB)
public class JdbcMealRepositoryHsqldb extends JdbcMealRepository<Timestamp> {

    public JdbcMealRepositoryHsqldb(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
    }

    @Override
    public Timestamp handlerData(LocalDateTime dateTime) {
        return Timestamp.valueOf(dateTime);
    }
}
