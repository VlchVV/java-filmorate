package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film getFilmById(Long filmId) {
        return filmStorage.findFilm(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм с Id=" + filmId + " не найден!"));
    }

    public Film create(Film film) {
        log.debug("Начато создание фильма", film);
        // сохраняем новый фильм
        filmStorage.create(film);
        log.debug("Фильм создан: " + film);
        return film;
    }

    public Film update(Film newFilm) {
        log.debug("Начато обновление фильма", newFilm);
        // проверяем необходимые условия
        if (newFilm.getId() == null) {
            log.error("Id должен быть указан");
            throw new ValidationException("Id должен быть указан");
        }
        Film updatedFilm = filmStorage.update(newFilm);
        if (updatedFilm == null) {
            log.error("Фильм с id = " + newFilm.getId() + " не найден");
            throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
        }
        log.debug("Фильм обновлен: новое значение", newFilm);
        return updatedFilm;
    }

    public void addLike(Long filmId, Long userId) {
        log.debug("Начато добавление лайка от пользователя id = " + userId + " фильму id = " + filmId + ".");
        if (userStorage.findUser(userId).isEmpty()) {
            log.error("Пользователь с id = " + userId + " не найден");
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
        Optional<Film> film = filmStorage.findFilm(filmId);
        if (film.isEmpty()) {
            log.error("Фильм с id = " + filmId + " не найден");
            throw new NotFoundException("Фильм с id = " + filmId + " не найден");
        }
        if (film.get().getLikes().contains(userId)) {
            log.error("Пользователь с id = " + userId
                    + " уже поставил лайк фильму с Id = " + filmId + ".");
            throw new DuplicatedDataException("Пользователь с id = " + userId
                    + " уже поставил лайк фильму с Id = " + filmId + ".");
        }
        filmStorage.addLike(filmId, userId);
        log.debug("Пользователь id = " + userId + " поставил лайк фильму id = " + filmId + ".");
    }

    public void deleteLike(Long filmId, Long userId) {
        log.debug("Начато удаление лайка пользователя Id = " + userId + " с фильма Id = " + filmId + ".");
        if (userStorage.findUser(userId).isEmpty()) {
            log.error("Пользователь с id = " + userId + " не найден");
            throw new NotFoundException("Пользователь с Id = " + userId + " не найден");
        }
        Optional<Film> film = filmStorage.findFilm(filmId);
        if (film.isEmpty()) {
            log.error("Фильм с id = " + filmId + " не найден");
            throw new NotFoundException("Фильм с Id = " + filmId + " не найден");
        }
        if (!film.get().getLikes().contains(userId)) {
            log.error("У фильма с id = " + filmId
                    + " нет лайка от пользователя с Id = " + userId + ".");
            throw new NotFoundException("У фильма с Id = " + filmId
                    + " нет лайка от пользователя с Id = " + userId + ".");
        }
        filmStorage.deleteLike(filmId, userId);
        log.debug("Лайк пользователя id = " + userId + " удален с фильма id = " + filmId + ".");
    }

    public Collection<Film> getPopular(Integer count) {
        return filmStorage.findAll().stream()
                .sorted(Comparator.comparingInt((Film film) -> film.getLikes().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }
}
