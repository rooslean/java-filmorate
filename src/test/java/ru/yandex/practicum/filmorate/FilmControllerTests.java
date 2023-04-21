package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class FilmControllerTests {

     FilmController filmController;

    @BeforeEach
     void makeFilmController() {
        filmController = new FilmController();
    }

    @Test
    void shouldReturnAllFilms() {
        Film filmOne = Film.builder()
                .name("Джентльмены")
                .description("Один ушлый американец ещё со студенческих лет " +
                        "приторговывал наркотиками, а теперь придумал схему нелегального обогащения.")
                .releaseDate(LocalDate.of(2020, 2, 13))
                .duration(113).build();

        Film filmTwo = Film.builder()
                .name("Джанго освобождённый")
                .description("Эксцентричный охотник за головами, также известный как «Дантист», " +
                        "промышляет отстрелом самых опасных преступников на Диком Западе.")
                .releaseDate(LocalDate.of(2013, 1, 17))
                .duration(165).build();

        ArrayList<Film> films = new ArrayList<>();
        films.add(filmOne);
        films.add(filmTwo);

        filmController.create(filmOne);
        filmController.create(filmTwo);

        assertEquals(films, filmController.getAll());
    }
    @Test
    void shouldCreateNewFilm() {
        Film film = Film.builder()
                .name("Джентльмены")
                .description("Один ушлый американец ещё со студенческих лет " +
                "приторговывал наркотиками, а теперь придумал схему нелегального обогащения.")
                .releaseDate(LocalDate.of(2020, 2, 13))
                .duration(113).build();

        Film addedFilm  = filmController.create(film);
        assertEquals(film, addedFilm);
    }

    @Test
    void shouldThrowExceptionBecauseOfTooLargeDescription() {
        Film film = Film.builder()
                .name("Джентльмены")
                .description("Один ушлый американец ещё со студенческих лет " +
                     "приторговывал наркотиками, а теперь придумал схему нелегального обогащения с использованием поместий " +
                     "обедневшей английской аристократии и очень неплохо на этом разбогател. Другой пронырливый журналист " +
                     "приходит к Рэю, правой руке американца, и предлагает тому купить киносценарий, в котором подробно " +
                     "описаны преступления его босса при участии других представителей лондонского криминального мира — " +
                     "партнёра-еврея, китайской диаспоры, чернокожих спортсменов и даже русского олигарха.")
                .releaseDate(LocalDate.of(2020, 2, 13))
                .duration(113).build();

        final FilmValidationException exception = assertThrows(
                FilmValidationException.class,
                () -> filmController.create(film));

        assertEquals("Фильм содержит невалидные данные", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenDescriptionLengthIs200() {
        Film film = Film.builder()
                .name("Джентльмены")
                .description("Один ушлый американец ещё со студенческих лет приторговывал наркотиками, " +
                        "а теперь придумал схему нелегального обогащения с использованием поместий обедневшей " +
                        "английской аристократии и очень неплохо на")
                .releaseDate(LocalDate.of(2020, 2, 13))
                .duration(113).build();

        final FilmValidationException exception = assertThrows(
                FilmValidationException.class,
                () -> filmController.create(film));

        assertEquals("Фильм содержит невалидные данные", exception.getMessage());
    }

    @Test
    void shouldCreateFilmWhenDescriptionLengthIs199() {
        Film film = Film.builder()
                .name("Джентльмены")
                .description("Один ушлый американец ещё со студенческих лет приторговывал наркотиками, " +
                        "а теперь придумал схему нелегального обогащения с использованием поместий обедневшей " +
                        "английской аристократии и очень неплохо н")
                .releaseDate(LocalDate.of(2020, 2, 13))
                .duration(113).build();

        assertEquals(film, filmController.create(film));
    }

    @Test
    void shouldThrowExceptionBecauseOfEmptyName() {
        Film film = Film.builder()
                .name("")
                .description("Один ушлый американец ещё со студенческих лет " +
                        "приторговывал наркотиками, а теперь придумал схему нелегального обогащения.")
                .releaseDate(LocalDate.of(2020, 2, 13))
                .duration(113).build();

        final FilmValidationException exception = assertThrows(
                FilmValidationException.class,
                () -> filmController.create(film));

        assertEquals("Фильм содержит невалидные данные", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionBecauseOfTooEarlyDate() {
        Film film = Film.builder()
                .name("Джентльмены")
                .description("Один ушлый американец ещё со студенческих лет " +
                        "приторговывал наркотиками, а теперь придумал схему нелегального обогащения.")
                .releaseDate(LocalDate.of(1895, 12, 27))
                .duration(113).build();

        final FilmValidationException exception = assertThrows(
                FilmValidationException.class,
                () -> filmController.create(film));

        assertEquals("Фильм содержит невалидные данные", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenDateEqualsMinDate() {
        Film film = Film.builder()
                .name("Джентльмены")
                .description("Один ушлый американец ещё со студенческих лет " +
                        "приторговывал наркотиками, а теперь придумал схему нелегального обогащения.")
                .releaseDate(LocalDate.of(1895, 12, 28))
                .duration(113).build();

        final FilmValidationException exception = assertThrows(
                FilmValidationException.class,
                () -> filmController.create(film));

        assertEquals("Фильм содержит невалидные данные", exception.getMessage());
    }

    @Test
    void shouldCreateFilmWhenDateEqualsMinDatePlusDay() {
        Film film = Film.builder()
                .name("Джентльмены")
                .description("Один ушлый американец ещё со студенческих лет " +
                        "приторговывал наркотиками, а теперь придумал схему нелегального обогащения.")
                .releaseDate(LocalDate.of(1895, 12, 29))
                .duration(113).build();


        assertEquals(film,  filmController.create(film));
    }

    @Test
    void shouldThrowExceptionBecauseOfZeroDuration() {
        Film film = Film.builder()
                .name("Джентльмены")
                .description("Один ушлый американец ещё со студенческих лет " +
                        "приторговывал наркотиками, а теперь придумал схему нелегального обогащения.")
                .releaseDate(LocalDate.of(2020, 2, 13))
                .duration(0).build();

        final FilmValidationException exception = assertThrows(
                FilmValidationException.class,
                () -> filmController.create(film));

        assertEquals("Фильм содержит невалидные данные", exception.getMessage());
    }
    @Test
    void shouldThrowExceptionBecauseOfNegativeDuration() {
        Film film = Film.builder()
                .name("Джентльмены")
                .description("Один ушлый американец ещё со студенческих лет " +
                        "приторговывал наркотиками, а теперь придумал схему нелегального обогащения.")
                .releaseDate(LocalDate.of(2020, 2, 13))
                .duration(-113).build();

        final FilmValidationException exception = assertThrows(
                FilmValidationException.class,
                () -> filmController.create(film));

        assertEquals("Фильм содержит невалидные данные", exception.getMessage());
    }

    @Test
    void shouldCreateFilmWhenDurationEqualsOne() {
        Film film = Film.builder()
                .name("Джентльмены")
                .description("Один ушлый американец ещё со студенческих лет " +
                        "приторговывал наркотиками, а теперь придумал схему нелегального обогащения.")
                .releaseDate(LocalDate.of(2020, 2, 13))
                .duration(1).build();

        assertEquals(film, filmController.create(film));
    }
}
