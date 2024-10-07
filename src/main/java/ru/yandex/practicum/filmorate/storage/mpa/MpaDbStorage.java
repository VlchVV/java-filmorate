package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;

import java.util.Collection;
import java.util.Optional;

@Repository
public class MpaDbStorage extends BaseDbStorage<Mpa> implements MpaStorage {

    private static final String FIND_ALL_QUERY = """
            SELECT
            	R.ID,
            	R.NAME
            FROM
            	RATING R;
            """;

    private static final String FIND_BY_ID_QUERY = """
            SELECT
            	R.ID,
            	R.NAME
            FROM
            	RATING R
            WHERE
            	R.ID = ?;
            """;

    public MpaDbStorage(JdbcTemplate jdbc, MpaRowMapper mapper) {
        super(jdbc, mapper, Mpa.class);
    }

    public Collection<Mpa> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Optional<Mpa> findMpa(Long mpaId) {
        return findOne(FIND_BY_ID_QUERY, mpaId);
    }
}
