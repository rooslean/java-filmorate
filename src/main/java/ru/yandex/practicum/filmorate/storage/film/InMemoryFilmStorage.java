package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 1;
    private final int maxDescriptionLength = 200;
    private final LocalDate minFilmReleaseDate = LocalDate.of(1895, 12, 28);

    public Film create(@Valid @RequestBody Film film) {
        if (!isFilmValid(film)) {
            log.warn("Ошибка при добавлении фильма, невалидные данные: {}", film);
            throw new FilmValidationException();
        }
        film.setId(id);
        film.setLikes(new HashSet<>());
        films.put(id, film);
        id++;
        log.info("Фильм {} (id={}) успешно создан", film.getName(), film.getId());
        log.debug("Данные фильма: {}", film);
        return film;
    }

    public Film save(@Valid @RequestBody Film film) {
        if (!isFilmValid(film) || !films.containsKey(film.getId())) {
            log.warn("Ошибка при обновлении фильма, невалидные данные: {}", film);
            throw new FilmValidationException();
        }
//        film.setLikes(films.get(film.getId()).getLikes());
        films.put(film.getId(), film);
        log.info("Данные фильма {} (id={}) успешно обновлены", film.getName(), film.getId());
        log.debug("Данные фильма: {}", film);
        return film;
    }

    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(int id) {
        if (films.get(id) == null) {
            log.info(String.format("Фильм c id - %d не найден", id));
            throw new FilmNotFoundException(id);
        }
        return films.get(id);
    }

    private boolean isFilmValid(Film film) {
        return !film.getName().isEmpty()
                && film.getDescription().length() < maxDescriptionLength
                && film.getReleaseDate().isAfter(minFilmReleaseDate)
                && film.getDuration() > 0;
    }
}
