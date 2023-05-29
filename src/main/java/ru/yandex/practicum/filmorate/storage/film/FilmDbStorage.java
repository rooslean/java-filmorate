package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component("FilmDbStorage")
@Slf4j
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    @Override
    public Film create(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("film")
                .usingGeneratedKeyColumns("film_id");
        int id = simpleJdbcInsert.executeAndReturnKey(film.toMap()).intValue();

        if (film.getGenres() != null) {
            addGenresToFilm(id, film.getGenres().stream().map(Genre::getId).collect(Collectors.toList()));
        }
        film.setId(id);
        return film;
    }

    @Override
    public Film save(Film film) {
        String sqlQuery = "UPDATE film SET " +
                "name = ?, description = ?, release_date = ? , duration = ?, rating_id = ?" +
                "where film_id = ?";
        int result = jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        if (result == 0) {
            throw new FilmNotFoundException(film.getId());
        }

        if (film.getGenres() != null) {
            Collection<Genre> genres = getGenresByFilmId(film.getId());
            solveGenresAdd(film.getId(), genres, film.getGenres());
        }
        film.setGenres(getGenresByFilmId(film.getId()));
        return film;
    }

    @Override
    public Collection<Film> getAll() {
        String sqlQuery = "SELECT f.film_id, " +
                "f.name film_name, " +
                "f.description, " +
                "f.release_date, " +
                "f.duration, " +
                "f.rating_id, " +
                "r.name rating_name " +
                "FROM film f " +
                "JOIN rating r ON f.rating_id=r.rating_id";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    @Override
    public Film getFilmById(int id) {
        String sqlQuery = "SELECT f.film_id, " +
                "f.name film_name, " +
                "f.description, " +
                "f.release_date, " +
                "f.duration, " +
                "f.rating_id, " +
                "r.name rating_name " +
                "FROM film f " +
                "JOIN rating r ON f.rating_id=r.rating_id " +
                "WHERE f.film_id = ?";
        Film film;
        try {
            film = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);
        } catch (EmptyResultDataAccessException e) {
            throw new FilmNotFoundException(id);
        }
        return film;
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getInt("film_id"))
                .name(resultSet.getString("film_name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getObject("release_date", LocalDate.class))
                .duration(resultSet.getInt("duration"))
                .mpa(new Rating(resultSet.getInt("rating_id"), resultSet.getString("rating_name")))
                .genres(getGenresByFilmId(resultSet.getInt("film_id")))
                .build();
    }

    private Collection<Genre> getGenresByFilmId(int filmId) {
        String sqlQuery = "SELECT genre_id, " +
                "name " +
                "FROM genre " +
                "WHERE genre_id IN (SELECT genre_id " +
                "FROM film_genre " +
                "WHERE film_id = ?)";
        return jdbcTemplate.query(sqlQuery, ((rs, rowNum) -> Genre.builder().id(rs.getInt("genre_id"))
                .name(rs.getString("name"))
                .build()), filmId);
    }

    private void solveGenresAdd(int filmId, Collection<Genre> oldGenres, Collection<Genre> newGenres) {
        Set<Integer> oldGenresIds = oldGenres.stream().map(Genre::getId).collect(Collectors.toSet());
        Set<Integer> newGenresIds = newGenres.stream().map(Genre::getId).collect(Collectors.toSet());
        List<Integer> genresToAdd = newGenresIds.stream().filter(id -> !oldGenresIds.contains(id)).collect(Collectors.toList());
        List<Integer> genresToDel = oldGenresIds.stream().filter(id -> !newGenresIds.contains(id)).collect(Collectors.toList());
        addGenresToFilm(filmId, genresToAdd);
        removeGenresFromFilm(filmId, genresToDel);
    }

    private void removeGenresFromFilm(int filmId, List<Integer> genresToDel) {
        if (genresToDel.size() > 0) {
            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("film_id", filmId)
                    .addValue("genre_ids", genresToDel);
            String sqlQuery = "DELETE FROM film_genre " +
                    "WHERE film_id = :film_id " +
                    "AND genre_id IN (:genre_ids)";
            namedJdbcTemplate.update(sqlQuery, parameters);
        }
    }

    private void addGenresToFilm(int filmId, List<Integer> genresToAdd) {
        if (genresToAdd.size() > 0) {
            String sqlQuery = "INSERT INTO film_genre(film_id, genre_id)" +
                    "VALUES(?, ?)";
            for (Integer genreId : genresToAdd) {
                jdbcTemplate.update(sqlQuery, filmId, genreId);
            }
        }

    }
}
