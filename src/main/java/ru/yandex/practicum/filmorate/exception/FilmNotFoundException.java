package ru.yandex.practicum.filmorate.exception;

public class FilmNotFoundException extends RuntimeException {
    public FilmNotFoundException() {
        super("Фильм содержит невалидные данные");
    }

    public FilmNotFoundException(int id) {
        super(String.format("Фильм c id - %d не найден", id));
    }
}
