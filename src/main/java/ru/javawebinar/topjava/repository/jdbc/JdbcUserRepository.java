package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.JdbcValidator;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private JdbcValidator<User> jdbcValidator = new JdbcValidator<>();

    private static final BeanPropertyRowMapper<User> ROW_MAPPER_USER = BeanPropertyRowMapper.newInstance(User.class);

    private static final RowMapper ROW_MAPPER_ROLE = new RowMapper<Role>() {
        @Override
        public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
            String role = rs.getString("role");
            return Role.valueOf(role);
        }
    };

    private final RowMapper ROW_MAPPER_USER_ROLE = new RowMapper<UserAndRole>() {
        @Override
        public UserAndRole mapRow(ResultSet rs, int rowNum) throws SQLException {
            UserAndRole userAndRole = new UserAndRole();
            userAndRole.setUserId(rs.getInt("user_id"));
            userAndRole.setRole(rs.getString("role"));
            return userAndRole;
        }
    };

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    private final TransactionTemplate txReadOnly;
    private final TransactionTemplate tx;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate, DataSource dataSource) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;

        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(dataSource);
        tx = new TransactionTemplate(dataSourceTransactionManager);
        dataSourceTransactionManager.setEnforceReadOnly(true);
        txReadOnly = new TransactionTemplate(dataSourceTransactionManager);
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
        } else if (tx.execute(ts -> namedParameterJdbcTemplate.update(
                "UPDATE users SET name=:name, email=:email, password=:password, " +
                        "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) == 0)) {
            return null;
        }

        txReadOnly.execute(ts -> jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.getId()));

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
    public boolean delete(int id) {
        return txReadOnly.execute(ts -> jdbcTemplate.update("DELETE FROM users WHERE id=?", id)) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = txReadOnly.execute(ts ->
                jdbcTemplate.query("SELECT * FROM users u WHERE u.id=?", ROW_MAPPER_USER, id));
        User user = DataAccessUtils.singleResult(users);
        getRolesForUser(user);
        return user;
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = txReadOnly.execute(ts -> jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER_USER, email));
        User user = DataAccessUtils.singleResult(users);
        getRolesForUser(user);
        return user;
    }

    @Override
    public List<User> getAll() {
        List<User> users = txReadOnly.execute(ts -> jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER_USER));
        List<UserAndRole> listRolesWithRoles = txReadOnly.execute(ts -> jdbcTemplate.query("SELECT * FROM user_roles", ROW_MAPPER_USER_ROLE));
        Map<Integer, Set<Role>> mapRoles = listRolesWithRoles
                .stream()
                .collect(Collectors.toMap(
                        item -> item.getUserId(),
                        item -> new HashSet<>(Arrays.asList(Role.valueOf(item.getRole()))),
                        (old_vale, new_value) -> {
                            old_vale.addAll(new_value);
                            return old_vale;
                        })
                );
        for (User user : users) {
            user.setRoles(mapRoles.get(user.getId()));
        }
        return users;
    }

    private void getRolesForUser(User user) {
        if (user == null) {
            return;
        }
        Set<Role> roles = (Set<Role>) txReadOnly.execute(ts ->
                jdbcTemplate.query("SELECT * FROM user_roles ur WHERE ur.user_id=?", ROW_MAPPER_ROLE, user.getId()))
                .stream()
                .collect(Collectors.toSet());
        user.setRoles(roles);
    }

    private class UserAndRole {
        private int userId;
        private String role;

        public UserAndRole(int userId, String role) {
            this.userId = userId;
            this.role = role;
        }

        public UserAndRole() {

        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }
}
