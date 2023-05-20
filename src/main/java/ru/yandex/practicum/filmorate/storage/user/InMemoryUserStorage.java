package ru.yandex.practicum.filmorate.storage.user;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PutMapping;
import ru.yandex.practicum.filmorate.exception.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 1;

    public User create(User user) {
        if (!isUserValid(user)) {
            log.warn("Ошибка при добавлении пользователя, невалидные данные: {}", user);
            throw new UserValidationException(
                    String.format("Пользователь содержит невалидные данные, проверьте корректность всех полей: %s", user));
        }
        user.setId(id);
        users.put(id, user);
        id++;
        log.info("Пользователь {} (id={}) успешно создан", user.getLogin(), user.getId());
        log.debug("Данные пользователя: {}", user);
        return user;
    }

    @PutMapping
    public User save(User user) {
        if (!isUserValid(user) || !users.containsKey(user.getId())) {
            log.warn("Ошибка при обновлении фильма, невалидные данные: {}", user);
            throw new UserValidationException(
                    String.format("Пользователь содержит невалидные данные, проверьте корректность всех полей:%s", user));
        }
        users.put(user.getId(), user);
        log.info("Данные пользователя {} (id={}) успешно обновлены", user.getLogin(), user.getId());
        log.debug("Данные пользователя: {}", user);
        return user;
    }

    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    public User getUserById(int id) {
        return users.get(id);
    }

    private boolean isUserValid(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return !user.getEmail().isEmpty()
                && user.getEmail().contains("@")
                && !user.getLogin().isEmpty()
                && !user.getLogin().contains(" ")
                && user.getBirthday().isBefore(LocalDate.now());
    }
}
