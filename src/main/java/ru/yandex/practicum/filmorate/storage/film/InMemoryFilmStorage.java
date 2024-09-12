package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Optional<Film> findFilm(Long filmId) {
        return Optional.ofNullable(films.get(filmId));
    }

    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Film create(Film film) {
        // формируем дополнительные данные
        film.setId(getNextId());
        film.setLikes(new HashSet<>());
        // сохраняем новоый фильм в памяти приложения
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film newFilm) {
        if (films.containsKey(newFilm.getId())) {
            Film olfFilm = films.get(newFilm.getId());
            olfFilm.setDescription(newFilm.getDescription());
            olfFilm.setReleaseDate(newFilm.getReleaseDate());
            olfFilm.setName(newFilm.getName());
            olfFilm.setDuration(newFilm.getDuration());
            return olfFilm;
        } else {
            return null;
        }
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        films.get(filmId).getLikes().add(userId);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        films.get(filmId).getLikes().remove(userId);
    }

    // вспомогательный метод для генерации идентификатора нового фильма
    private long getNextId() {
        long currentMaxId = films.keySet().stream().mapToLong(id -> id).max().orElse(0);
        return ++currentMaxId;
    }
}
