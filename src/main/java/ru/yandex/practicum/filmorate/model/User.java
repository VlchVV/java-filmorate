package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.Set;

@Data
@EqualsAndHashCode(of = {"email"})
public class User {
    private Long id;
    @NotNull
    @NotBlank
    @Email
    private String email;
    @NotNull
    @Pattern(regexp = "^[^\\s]+$")
    private String login;
    private String name;
    @Past
    private LocalDate birthday;
    private Set<Long> friends;
}
