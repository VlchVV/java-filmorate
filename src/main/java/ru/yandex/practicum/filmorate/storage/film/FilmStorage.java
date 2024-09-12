package ru.yandex.practicum.filmorate.storage.film;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    public Collection<Film> findAll();

    public Film create(@Valid @RequestBody Film film);

    public Film update(@Valid @RequestBody Film newFilm);

    public Optional<Film> findFilm(Long filmId);

    public void addLike(Long filmId, Long userId);

    public void deleteLike(Long filmId, Long userId);

}
