package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JdbcTest
@AutoConfigureTestDatabase
@ComponentScan("ru.yandex.practicum.filmorate")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;

    @Test
    public void findUserByIdReturnsUserWithId() {
        Optional<User> userOptional = userStorage.findUser(1L);
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void findAllUsersReturnsUsers() {
        Collection<User> users = userStorage.findAll();
        assertFalse(users.isEmpty());
    }

    @Test
    public void createUserReturnsNewUser() {
        User user = new User();
        user.setEmail("test@yandex.by");
        user.setLogin("test_login");
        user.setName("Тест");
        user.setBirthday(LocalDate.of(1999, 8, 4));
        userStorage.create(user);
        assertTrue(user.getId() > 0);
    }

    @Test
    public void findFilmByIdReturnsFilmWithId() {
        Optional<Film> filmOptional = filmStorage.findFilm(1L);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void findAllFilmsReturnsFilms() {
        Collection<Film> films = filmStorage.findAll();
        assertFalse(films.isEmpty());
    }

    @Test
    public void createFilmReturnsNewFilm() {
        Film film = new Film();
        film.setName("Kill Bill: Vol. 1,");
        film.setDescription("Quentin Tarantino");
        film.setDuration(140);
        film.setReleaseDate(LocalDate.of(2003, 1, 1));
        film.setMpa(new Mpa(1L, null));
        filmStorage.create(film);
        assertTrue(film.getId() > 0);
    }
}