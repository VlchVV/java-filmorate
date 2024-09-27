package ru.yandex.practicum.filmorate.storage.film;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;

import java.util.Collection;
import java.util.Optional;

@Repository
public class FilmDbStorage extends BaseDbStorage<Film> implements FilmStorage {
    private static final String FIND_ALL_QUERY = """
            SELECT
            	F.ID,
            	F.NAME,
            	F.DESCRIPTION,
            	F.RELEASE_DT,
            	F.DURATION,
            	F.RATING_ID,
            	R.NAME AS RATING_NAME,
            	GROUP_CONCAT(DISTINCT CONCAT( G.ID , '/', G.NAME)) AS GENRES,
            	GROUP_CONCAT(DISTINCT UFL.USER_ID) AS USER_LIKES
            FROM
            	FILM F
            LEFT JOIN RATING R ON
            	F.RATING_ID = R.ID
            LEFT JOIN FILM_GENRE FG ON
            	F.ID = FG.FILM_ID
            LEFT JOIN GENRE G ON
            	FG.GENRE_ID = G.ID
            LEFT JOIN USER_FILM_LIKES ufl ON
            	F.ID = ufl.FILM_ID
            GROUP BY
            	F.ID;
            """;

    private static final String FIND_BY_ID_QUERY = """
            SELECT
            	F.ID,
            	F.NAME,
            	F.DESCRIPTION,
            	F.RELEASE_DT,
            	F.DURATION,
            	F.RATING_ID,
            	R.NAME AS RATING_NAME,
            	GROUP_CONCAT(DISTINCT CONCAT( G.ID , '/', G.NAME)) AS GENRES,
            	GROUP_CONCAT(DISTINCT UFL.USER_ID) AS USER_LIKES
            FROM
            	FILM F
            LEFT JOIN RATING R ON
            	F.RATING_ID = R.ID
            LEFT JOIN FILM_GENRE FG ON
            	F.ID = FG.FILM_ID
            LEFT JOIN GENRE G ON
            	FG.GENRE_ID = G.ID
            LEFT JOIN USER_FILM_LIKES ufl ON
            	F.ID = ufl.FILM_ID
            WHERE
            	F.ID = ?
            GROUP BY
            	F.ID;
            """;

    private static final String INSERT_QUERY = """
            INSERT INTO FILM(NAME, DESCRIPTION, RELEASE_DT, DURATION, RATING_ID)
            VALUES(?, ?, ?, ?, ?);
            """;

    private static final String INSERT_FILM_GENRE_QUERY = """
            INSERT INTO FILM_GENRE(FILM_ID, GENRE_ID)
            VALUES(?, ?);
            """;

    private static final String DEL_FILM_GENRE_QUERY = """
            DELETE FROM FILM_GENRE
            WHERE FILM_ID = ?;
            """;

    private static final String UPDATE_QUERY = """
            UPDATE FILM SET NAME = ?, DESCRIPTION = ?, RELEASE_DT = ?, DURATION = ?, RATING_ID = ?
            WHERE ID = ?;
            """;

    private static final String ADD_LIKE_QUERY = """
            INSERT INTO USER_FILM_LIKES (FILM_ID, USER_ID)
            VALUES(?, ?);
            """;

    private static final String DEL_LIKE_QUERY = """
            DELETE FROM USER_FILM_LIKES
            WHERE FILM_ID = ? AND USER_ID = ?;
            """;

    public FilmDbStorage(JdbcTemplate jdbc, FilmRowMapper mapper) {
        super(jdbc, mapper, Film.class);
    }

    @Override
    public Collection<Film> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Film create(Film film) {
        long id = insert(INSERT_QUERY, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId());
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                insert(INSERT_FILM_GENRE_QUERY, id, genre.getId());
            }
        }
        film.setId(id);
        return film;
    }

    @Override
    public Film update(Film newFilm) {
        update(
                UPDATE_QUERY,
                newFilm.getName(), newFilm.getDescription(), newFilm.getReleaseDate(),
                newFilm.getDuration(), newFilm.getMpa().getId(), newFilm.getId());
        delete(DEL_FILM_GENRE_QUERY, newFilm.getId());
        if (newFilm.getGenres() != null) {
            for (Genre genre : newFilm.getGenres()) {
                insert(INSERT_FILM_GENRE_QUERY, newFilm.getId(), genre.getId());
            }
        }
        return newFilm;
    }

    @Override
    public Optional<Film> findFilm(Long filmId) {
        return findOne(FIND_BY_ID_QUERY, filmId);
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        insert(ADD_LIKE_QUERY, filmId, userId);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        delete(DEL_LIKE_QUERY, filmId, userId);
    }
}