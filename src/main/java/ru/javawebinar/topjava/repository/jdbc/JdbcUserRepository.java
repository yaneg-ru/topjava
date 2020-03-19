package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.JdbcValidator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository<mapUserRoles> implements UserRepository {

    private JdbcValidator<User> jdbcValidator = new JdbcValidator<>();

    private static final BeanPropertyRowMapper<User> ROW_MAPPER_USER = BeanPropertyRowMapper.newInstance(User.class);

    private static final RowMapper<Role> ROW_MAPPER_ROLE = new RowMapper<>() {
        @Override
        public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
            String role = rs.getString("role");
            return Role.valueOf(role);
        }
    };

    private static final ResultSetExtractor<Map<Integer, Set<Role>>> RESULT_SET_EXTRACTOR_USER_ROLES = new ResultSetExtractor<Map<Integer, Set<Role>>>() {
        Map<Integer, Set<Role>> mapUserRoles = new HashMap<>();
        @Override
        public Map<Integer, Set<Role>> extractData(ResultSet rs) throws SQLException, DataAccessException {
            while(rs.next()) {
                mapUserRoles.computeIfAbsent(rs.getInt("user_id"), k -> EnumSet.noneOf(Role.class))
                        .add(Role.valueOf(rs.getString("role")));
            }
            return mapUserRoles;
        }
    };

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {

        if (!jdbcValidator.validate(user)) {
            return null;
        }

        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update(
                "UPDATE users SET name=:name, email=:email, password=:password, " +
                        "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) == 0) {
            return null;
        }

        if (!user.isNew()) {
            jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.getId());
        }

        jdbcTemplate.batchUpdate(
                "insert into user_roles (user_id, role) values(?,?)",
                new BatchPreparedStatementSetter() {

                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, user.getId());
                        ps.setString(2, String.valueOf(user.getRoles().toArray()[i]));
                    }

                    public int getBatchSize() {
                        return user.getRoles().size();
                    }
                });

        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users u WHERE u.id=?", ROW_MAPPER_USER, id);
        User user = DataAccessUtils.singleResult(users);
        getRolesForUser(user);
        return user;
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER_USER, email);
        User user = DataAccessUtils.singleResult(users);
        getRolesForUser(user);
        return user;
    }

    @Override
    public List<User> getAll() {
        List<User> users = jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER_USER);
        Map<Integer, Set<Role>> mapUserRoles = jdbcTemplate.query("SELECT * FROM user_roles", RESULT_SET_EXTRACTOR_USER_ROLES);
        for (User user : users) {
            user.setRoles(mapUserRoles.get(user.getId()));
        }
        return users;
    }

    private void getRolesForUser(User user) {
        if (user == null) {
            return;
        }
        Set<Role> roles = new HashSet<>(jdbcTemplate.query("SELECT * FROM user_roles ur WHERE ur.user_id=?", ROW_MAPPER_ROLE, user.getId()));
        user.setRoles(roles);
    }
}
