package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.filmorate.validation.DateAfter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Data
@EqualsAndHashCode(of = {"id"})
public class Film {
    private Long id;
    @NotNull
    @NotBlank
    private String name;
    @Size(max = 200, message = "{validation.name.size.too_long}")
    private String description;
    @DateAfter
    private LocalDate releaseDate;
    @Min(60)
    private Integer duration;
    private Set<Long> likes;
}
