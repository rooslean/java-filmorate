package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTests {

    UserController userController;

    @BeforeEach
    void makeUserController() {
        userController = new UserController();
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

    @Test
    void shouldUseLoginAsNameIfNameIsEmpty() {
        User user = User.builder()
                .id(1)
                .name("")
                .email("rudusaev@yandex.ru")
                .login("rooslean")
                .birthday(LocalDate.of(1999, 3, 18))
                .build();

        assertEquals("rooslean", userController.create(user).getName());
    }

    @Test
    void shouldThrowExceptionIfEmailDoesNotContainAtSymbol() {
        User user = User.builder()
                .id(1)
                .name("Руслан")
                .email("rudusaevyandex.ru")
                .login("rooslean")
                .birthday(LocalDate.of(1999, 3, 18))
                .build();

        final UserValidationException exception = assertThrows(
                UserValidationException.class,
                () -> userController.create(user));

        assertEquals("Пользователь содержит невалидные данные", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionIfLoginIsEmpty() {
        User user = User.builder()
                .id(1)
                .name("Руслан")
                .email("rudusae@vyandex.ru")
                .login("")
                .birthday(LocalDate.of(1999, 3, 18))
                .build();

        final UserValidationException exception = assertThrows(
                UserValidationException.class,
                () -> userController.create(user));

        assertEquals("Пользователь содержит невалидные данные", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionIfLoginContainsSpace() {
        User user = User.builder()
                .id(1)
                .name("Руслан")
                .email("rudusae@vyandex.ru")
                .login("roos lean")
                .birthday(LocalDate.of(1999, 3, 18))
                .build();

        final UserValidationException exception = assertThrows(
                UserValidationException.class,
                () -> userController.create(user));

        assertEquals("Пользователь содержит невалидные данные", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionIfBirthdayTomorrow() {
        User user = User.builder()
                .id(1)
                .name("Руслан")
                .email("rudusaev@yandex.ru")
                .login("rooslean")
                .birthday(LocalDate.now().plusDays(1))
                .build();

        final UserValidationException exception = assertThrows(
                UserValidationException.class,
                () -> userController.create(user));

        assertEquals("Пользователь содержит невалидные данные", exception.getMessage());
    }
    @Test
    void shouldThrowExceptionIfBirthdayToday() {
        User user = User.builder()
                .id(1)
                .name("Руслан")
                .email("rudusae@vyandex.ru")
                .login("rooslean")
                .birthday(LocalDate.now())
                .build();

        final UserValidationException exception = assertThrows(
                UserValidationException.class,
                () -> userController.create(user));

        assertEquals("Пользователь содержит невалидные данные", exception.getMessage());
    }

    @Test
    void shouldCreateUserIfBirthdayYesterday() {
        User user = User.builder()
                .id(1)
                .name("Руслан")
                .email("rudusae@vyandex.ru")
                .login("rooslean")
                .birthday(LocalDate.now().minusDays(1))
                .build();


        assertEquals(user,  userController.create(user));
    }
}
