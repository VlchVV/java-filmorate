package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;

import java.util.Collection;
import java.util.Optional;

@Repository
public class GenreDbStorage extends BaseDbStorage<Genre> implements GenreStorage {

    private static final String FIND_ALL_QUERY = """
            SELECT
            	G.ID,
            	G.NAME
            FROM
            	GENRE G;
            """;

    private static final String FIND_BY_ID_QUERY = """
            SELECT
                G.ID,
                G.NAME
            FROM
                GENRE G
            WHERE
            	G.ID = ?;
            """;

    public GenreDbStorage(JdbcTemplate jdbc, GenreRowMapper mapper) {
        super(jdbc, mapper, Genre.class);
    }

    public Collection<Genre> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Optional<Genre> findGenre(Long genreId) {
        return findOne(FIND_BY_ID_QUERY, genreId);
    }
}
