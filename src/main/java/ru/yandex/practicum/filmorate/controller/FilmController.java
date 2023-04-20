package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 1;

    @PostMapping
    public Film create(@RequestBody Film film) {
        film.setId(id);
        films.put(id, film);
        id++;
        return film;
    }

    @PutMapping
    public Film save(@RequestBody Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @GetMapping
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }
}
