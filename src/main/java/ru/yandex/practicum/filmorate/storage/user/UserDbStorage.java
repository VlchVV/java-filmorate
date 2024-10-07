package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FriendStatus;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;

import java.util.Collection;
import java.util.Optional;

@Repository
public class UserDbStorage extends BaseDbStorage<User> implements UserStorage {
    private static final String FIND_ALL_QUERY = """
            SELECT
                U.ID,
                U.EMAIL,
                U.LOGIN,
                U.NAME,
                U.BIRTHDAY_DT,
                GROUP_CONCAT(DISTINCT FR.FRIEND_ID) AS USER_FRIENDS
            FROM
                "user" U
            LEFT JOIN FRIENDS FR ON
                U.ID = FR.USER_ID
            GROUP BY
                U.ID;
            """;
    private static final String FIND_BY_ID_QUERY = """
            SELECT
                U.ID,
                U.EMAIL,
                U.LOGIN,
                U.NAME,
                U.BIRTHDAY_DT,
                GROUP_CONCAT(DISTINCT FR.FRIEND_ID) AS USER_FRIENDS
            FROM
                "user" U
            LEFT JOIN FRIENDS FR ON
                U.ID = FR.USER_ID
            WHERE
            	U.ID = ?
            GROUP BY
            	U.ID;
            """;

    private static final String INSERT_QUERY = """
            INSERT INTO "user" (EMAIL, NAME, LOGIN, BIRTHDAY_DT)
            VALUES(?, ?, ?, ?);
            """;

    private static final String UPDATE_QUERY = """
            UPDATE "user" SET EMAIL = ?, NAME = ?, LOGIN = ?, BIRTHDAY_DT = ?
            WHERE ID = ?;
            """;

    private static final String ADD_FRIEND_QUERY = """
            INSERT INTO FRIENDS (USER_ID, FRIEND_ID, STATUS)
            VALUES(?, ?, ?);
            """;

    private static final String DEL_FRIEND_QUERY = """
            DELETE FROM FRIENDS
            WHERE USER_ID = ? AND FRIEND_ID = ?;
            """;

    private static final String FIND_FRIENDS_QUERY = """
            SELECT
                 U.ID,
                 U.EMAIL,
                 U.LOGIN,
                 U.NAME,
                 U.BIRTHDAY_DT,
                 GROUP_CONCAT(DISTINCT FR.FRIEND_ID) AS USER_FRIENDS
             FROM
                 FRIENDS UF
             JOIN "user" U ON
                 UF.FRIEND_ID = U.ID
             LEFT JOIN FRIENDS FR ON
                 U.ID = FR.USER_ID
             WHERE UF.USER_ID = ?
             GROUP BY
                 U.ID;
             """;

    public UserDbStorage(JdbcTemplate jdbc, UserRowMapper mapper) {
        super(jdbc, mapper, User.class);
    }

    @Override
    public Collection<User> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Optional<User> findUser(Long userId) {
        return findOne(FIND_BY_ID_QUERY, userId);
    }

    @Override
    public User create(User user) {
        long id = insert(INSERT_QUERY, user.getEmail(), user.getName(), user.getLogin(), user.getBirthday());
        user.setId(id);
        return user;
    }

    @Override
    public User update(User newUser) {
        update(UPDATE_QUERY,
                newUser.getEmail(), newUser.getName(), newUser.getLogin(), newUser.getBirthday(), newUser.getId());
        return newUser;
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        insert(ADD_FRIEND_QUERY, userId, friendId, FriendStatus.NEW.toString());
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        delete(DEL_FRIEND_QUERY, userId, friendId);
    }

    @Override
    public Collection<User> getFriends(Long userId) {
        return findMany(FIND_FRIENDS_QUERY, userId);
    }
}
