package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(of = { "email" })
public class User {
    Long id;
    @NotNull @NotBlank @Email String email;
    @NotNull @Pattern(regexp = "^[^\\s]+$") String login;
    String name;
    @Past LocalDate birthday;
}
