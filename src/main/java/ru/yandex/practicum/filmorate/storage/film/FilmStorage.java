package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Film create(Film film);

    Film save(Film film);

    Collection<Film> getAll();

    Film getFilmById(int id);

    void addLike(int filmId, int userId);

    void deleteLike(int filmId, int userId);

    Collection<Film> getPopularFilms(int count);
}
