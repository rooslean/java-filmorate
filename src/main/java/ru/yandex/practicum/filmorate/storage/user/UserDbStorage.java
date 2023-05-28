package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;

@Component("UserDbStorage")
@Slf4j
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User create(User user) {
//        String sqlQuery = "INSERT INTO user(email, login, name, birthday) values (?, ?, ?, ?);";
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("user")
                .usingGeneratedKeyColumns("user_id");
        int id = simpleJdbcInsert.executeAndReturnKey(user.toMap()).intValue();
        user.setId(id);
        log.info("Пользователь с идентификатором {} и логином {} был создан", id, user.getLogin());
        return user;
    }

    @Override
    public User save(User user) {
        String sqlQuery = "UPDATE user SET " +
                "email = ?, login = ?, name = ? , birthday = ?" +
                "where user_id = ?";
        int result = jdbcTemplate.update(sqlQuery
                , user.getEmail()
                , user.getLogin()
                , user.getName()
                , user.getBirthday()
                , user.getId());
        if (result == 0) {
            throw new UserNotFoundException(user.getId());
        }
        log.info("Данные пользователя {} (id={}) успешно обновлены", user.getLogin(), user.getId());
        return user;
    }

    @Override
    public Collection<User> getAll() {
        String sqlQuery = "SELECT user_id, email, login, name, birthday FROM user";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    @Override
    public User getUserById(int id) {
        String sqlQuery = "SELECT user_id, email, login, name, birthday " +
                "FROM user WHERE user_id = ?";
        User user;
        try {
            user = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, id);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException(id);
        }
        return user;
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getInt("user_id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(resultSet.getObject("birthday", LocalDate.class))
                .build();
    }
}
