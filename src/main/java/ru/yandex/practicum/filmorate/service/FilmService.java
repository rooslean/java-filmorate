package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmBadReleaseDateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    FilmStorage filmStorage;
    UserStorage userStorage;
    private final LocalDate minFilmReleaseDate = LocalDate.of(1895, 12, 28);

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(int filmId, int userId) {
        Film film = filmStorage.getFilmById(filmId);
        userStorage.getUserById(userId);
        film.getLikes().add(userId);
        filmStorage.save(film);
    }

    public void deleteLike(int filmId, int userId) {
        Film film = filmStorage.getFilmById(filmId);
        userStorage.getUserById(userId);
        film.getLikes().remove(userId);
        filmStorage.save(film);
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage
                .getAll()
                .stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film create(Film film) {
        if (!isFilmValid(film)) {
            throw new FilmBadReleaseDateException(String.format("Дата релиза должна быть позже %s", minFilmReleaseDate));
        }
        return filmStorage.create(film);
    }

    public Film save(Film film) {
        if (!isFilmValid(film)) {
            throw new FilmBadReleaseDateException(String.format("Дата релиза должна быть позже %s", minFilmReleaseDate));
        }
        film.setLikes(filmStorage.getFilmById(film.getId()).getLikes());
        return filmStorage.save(film);
    }

    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film getFilmById(int id) {
        return filmStorage.getFilmById(id);
    }

    private boolean isFilmValid(Film film) {
        return film.getReleaseDate().isAfter(minFilmReleaseDate);
    }
}
