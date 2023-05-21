package ru.yandex.practicum.filmorate.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("Фильм содержит невалидные данные");
    }

    public UserNotFoundException(int id) {
        super(String.format("Пользователь c id - %d не найден", id));
    }
}