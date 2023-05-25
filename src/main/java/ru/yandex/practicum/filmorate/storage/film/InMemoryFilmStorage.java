package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 1;

    public Film create(Film film) {
        film.setId(id);
        film.setLikes(new HashSet<>());
        films.put(id, film);
        id++;
        log.info("Фильм {} (id={}) успешно создан", film.getName(), film.getId());
        return film;
    }

    public Film save(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new FilmNotFoundException(film.getId());
        }
        films.put(film.getId(), film);
        log.info("Данные фильма {} (id={}) успешно обновлены", film.getName(), film.getId());
        return film;
    }

    public Collection<Film> getAll() {
        return films.values();
    }

    @Override
    public Film getFilmById(int id) {
        if (films.get(id) == null) {
            throw new FilmNotFoundException(id);
        }
        return films.get(id);
    }
}
