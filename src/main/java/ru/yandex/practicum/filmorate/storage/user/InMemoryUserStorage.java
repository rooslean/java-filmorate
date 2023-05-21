package ru.yandex.practicum.filmorate.storage.user;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 1;

    public User create(User user) {
        if (!isUserValid(user)) {
            throw new UserValidationException(
                    String.format("Пользователь содержит невалидные данные, проверьте корректность всех полей: %s", user));
        }
        user.setId(id);
        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }
        users.put(id, user);
        id++;
        log.info("Пользователь {} (id={}) успешно создан", user.getLogin(), user.getId());
        return user;
    }

    public User save(User user) {
        if (!users.containsKey(user.getId())) {
            throw new UserNotFoundException(user.getId());
        }
        if (!isUserValid(user)) {
            throw new UserValidationException(
                    String.format("Пользователь содержит невалидные данные, проверьте корректность всех полей:%s", user));
        }
        users.put(user.getId(), user);
        log.info("Данные пользователя {} (id={}) успешно обновлены", user.getLogin(), user.getId());
        return user;
    }

    public Collection<User> getAll() {
        return users.values();
    }

    public User getUserById(int id) {
        if (users.get(id) == null) {
            throw new UserNotFoundException(id);
        }
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
