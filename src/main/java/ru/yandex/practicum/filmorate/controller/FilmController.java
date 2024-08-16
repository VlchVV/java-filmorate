package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.debug("Начато создание фильма", film);
        // формируем дополнительные данные
        film.setId(getNextId());
        // сохраняем новоог пользователя в памяти приложения
        films.put(film.getId(), film);
        log.debug("Фильм создан: " + film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {
        log.debug("Начато обновление фильма", newFilm);
        // проверяем необходимые условия
        if (newFilm.getId() == null) {
            log.error("Id должен быть указан");
            throw new ValidationException("Id должен быть указан");
        }
        if (films.containsKey(newFilm.getId())) {
            Film olfFilm = films.get(newFilm.getId());
            // если публикация найдена и все условия соблюдены, обновляем её содержимое
            olfFilm.setDescription(newFilm.getDescription());
            olfFilm.setReleaseDate(newFilm.getReleaseDate());
            olfFilm.setName(newFilm.getName());
            olfFilm.setDuration(newFilm.getDuration());
            log.debug("Фильм обновлен: предыдущее значение", olfFilm);
            log.debug("Фильм обновлен: новое значение", newFilm);
            return olfFilm;
        }
        log.error("Фильм с id = " + newFilm.getId() + " не найден");
        throw new NotFoundException("фильм с id = " + newFilm.getId() + " не найден");
    }

    // вспомогательный метод для генерации идентификатора нового пользователя
    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
