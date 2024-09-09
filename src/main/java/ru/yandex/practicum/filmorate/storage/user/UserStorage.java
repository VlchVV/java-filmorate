package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    public Collection<User> findAll();

    public Optional<User> findUser(Long userId);

    public User create(User user);

    public User update(User newUser);

    public void addFriend(Long userId, Long friendId);

    public void deleteFriend(Long userId, Long friendId);

    public Collection<User> getFriends(Long userId);
}
