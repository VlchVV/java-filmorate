package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.filmorate.validation.DateAfter;

import java.time.LocalDate;

@Getter
@Setter
@Data
@EqualsAndHashCode(of = {"id"})
public class Film {
    Long id;
    @NotNull @NotBlank String name;
    @Size(max = 200, message = "{validation.name.size.too_long}") String description;
    @DateAfter
    LocalDate releaseDate;
    @Min(60) Integer duration;
}
