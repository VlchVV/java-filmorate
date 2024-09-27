package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

@Component
public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setName(rs.getString("name"));
        user.setLogin(rs.getString("login"));
        user.setBirthday(rs.getTimestamp("birthday_dt").toLocalDateTime().toLocalDate());
        user.setEmail(rs.getString("email"));
        String friendIds = rs.getString("user_friends");
        Set<Long> friends = new HashSet<>();
        if (friendIds != null) {
            String[] ids = friendIds.split(",");
            for (String id : ids) {
                friends.add(Long.parseLong(id));
            }
        }
        user.setFriends(friends);
        return user;
    }
}
