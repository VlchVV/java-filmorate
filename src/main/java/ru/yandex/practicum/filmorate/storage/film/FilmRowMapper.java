package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

@Component
public class FilmRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getTimestamp("release_dt").toLocalDateTime().toLocalDate());
        film.setDuration(rs.getInt("duration"));
        film.setMpa(new Mpa(rs.getLong("rating_id"), rs.getString("rating_name")));

        String genresInfo = rs.getString("genres");

        Set<Genre> genres = new HashSet<>();
        if (genresInfo != null && !genresInfo.equals("/")) {
            String[] genreArr = genresInfo.split(",");
            for (int i = 0; i < genreArr.length; i++) {
                Genre genre = new Genre();
                genre.setId(Long.parseLong(genreArr[i].split("/")[0]));
                genre.setName(genreArr[i].split("/")[1]);
                genres.add(genre);
            }
        }
        film.setGenres(genres);

        String userLikes = rs.getString("user_likes");
        Set<Long> likes = new HashSet<>();
        if (userLikes != null) {
            String[] userIds = userLikes.split(",");
            for (String userId : userIds) {
                likes.add(Long.parseLong(userId));
            }
        }
        film.setLikes(likes);
        return film;
    }
}
