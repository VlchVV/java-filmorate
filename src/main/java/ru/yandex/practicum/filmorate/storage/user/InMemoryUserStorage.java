package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public Optional<User> findUser(Long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public User create(User user) {
        user.setId(getNextId());
        user.setFriends(new HashSet<>());
        // сохраняем нового пользователя в памяти приложения
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User newUser) {
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            oldUser.setEmail(newUser.getEmail());
            oldUser.setName(newUser.getName());
            oldUser.setBirthday(newUser.getBirthday());
            oldUser.setLogin(newUser.getLogin());
            return oldUser;
        } else {
            return null;
        }
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        users.get(userId).getFriends().add(friendId);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        users.get(userId).getFriends().remove(friendId);
    }

    @Override
    public Collection<User> getFriends(Long userId) {
        return users.get(userId).getFriends().stream()
                .map(users::get)
                .collect(Collectors.toList());
    }

    // вспомогательный метод для генерации идентификатора нового пользователя
    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
