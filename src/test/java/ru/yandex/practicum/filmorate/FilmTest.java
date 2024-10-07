package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FilmTest {
    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void validFilmValidationPasses() {
        Film film = new Film();
        film.setId(1L);
        film.setName("Test Film");
        film.setDescription("Test film description.");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(150);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void blankNameValidationFails() {
        Film film = new Film();
        film.setId(1L);
        film.setName("      ");
        film.setDescription("Test film description.");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(150);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void longDescriptionValidationFails() {
        Film film = new Film();
        film.setId(1L);
        film.setName("Test Film");
        film.setDescription("This is a test film description that is way too long" +
                " and exceeds the maximum allowed length of 200 characters. " +
                " Even more text: film about testing tests with testing tools " +
                "This should trigger a validation error."); // 211 symbols
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(150);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void invalidReleaseDateValidationFails() {
        Film film = new Film();
        film.setId(1L);
        film.setName("Test Film");
        film.setDescription("Test film description.");
        film.setReleaseDate(LocalDate.of(1800, 1, 1));
        film.setDuration(150);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void invalidDurationValidationFails() {
        Film film = new Film();
        film.setId(1L);
        film.setName("Test Film");
        film.setDescription("Test film description.");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(30);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }
}

