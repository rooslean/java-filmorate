package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDate;
@Data
@Builder
public class Film {
    int id;
    @NotBlank
    String name;
    @Size(max=200)
    String description;
    @NotNull
    LocalDate releaseDate;
    @PositiveOrZero

    int duration;
}
