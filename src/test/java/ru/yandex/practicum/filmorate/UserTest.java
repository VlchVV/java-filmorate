package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void validUserValidationPasses() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@yandex.by");
        user.setLogin("test_login");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void invalidEmailValidationFails() {
        User user = new User();
        user.setId(1L);
        user.setEmail("invalid-email@");
        user.setLogin("test_login");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void blankLoginValidationFails() {
        User user = new User();
        user.setId(1L);
        user.setEmail("testBlankLogin@yandex.by");
        user.setLogin("     ");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(2000, 12, 12));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void futureBirthdayValidationFails() {
        User user = new User();
        user.setId(1L);
        user.setEmail("testPastBirthday@yandex.by");
        user.setLogin("test_login");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(2125, 12, 12));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }
}
