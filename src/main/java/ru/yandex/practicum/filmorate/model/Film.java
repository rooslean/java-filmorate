package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Film {
    int id;
    @NotBlank
    String name;
    @Size(max = 200)
    String description;
    @NotNull
    LocalDate releaseDate;
    @PositiveOrZero
    int duration;
    Rating mpa;
    Set<Genre> genres;
    Set<Integer> likes;

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("film_id", id);
        values.put("name", name);
        values.put("description", description);
        values.put("release_date", releaseDate);
        values.put("duration", duration);
        values.put("rating_id", mpa.getId());
        return values;
    }
}
