package ru.yandex.practicum.filmorate.exception;

public class UserValidationException  extends RuntimeException {
    public UserValidationException() {
        super("Пользователь содержит невалидные данные");
    }
    public UserValidationException(String msg) {
        super(msg);
    }
}
