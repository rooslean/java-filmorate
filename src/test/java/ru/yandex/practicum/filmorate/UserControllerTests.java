package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserControllerTests {

    UserController userController;

    @BeforeEach
    void makeUserController() {
        UserStorage userStorage = new InMemoryUserStorage();
        userController = new UserController(new UserService(userStorage));
    }

    @Test
    void shouldCreateNewUser() {
        User user = User.builder()
                .id(1)
                .name("Руслан")
                .email("rudusaev@yandex.ru")
                .login("rooslean")
                .birthday(LocalDate.of(1999, 3, 18))
                .build();

        assertEquals(user, userController.create(user));
    }
}
