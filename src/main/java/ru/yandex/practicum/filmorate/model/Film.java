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
    private Long id;
    @NotNull @NotBlank private String name;
    @Size(max = 200, message = "{validation.name.size.too_long}") private String description;
    @DateAfter
    private LocalDate releaseDate;
    @Min(60) private Integer duration;
}
