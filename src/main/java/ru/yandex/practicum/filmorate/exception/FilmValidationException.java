package ru.yandex.practicum.filmorate.exception;

public class FilmValidationException extends RuntimeException {
    public FilmValidationException() {
        super("Фильм содержит невалидные данные");
    }
    public FilmValidationException(String msg) {
        super(msg);
    }
}
