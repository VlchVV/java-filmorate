package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User getUserById(Long userId) {
        return userStorage.findUser(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с Id=" + userId + " не найден!"));
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User user) {
        log.debug("Начато создание пользователя", user);
        // проверяем на уникальность по ключу
        if (isUserWithEmailExists(user)) {
            log.error("Этот email уже используется");
            throw new DuplicatedDataException("Этот email уже используется");
        }

        // формируем дополнительные данные
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("Имя пользователя обновлено на " + user.getName());
        }

        userStorage.create(user);

        log.debug("Пользователь создан", user);
        return user;
    }

    public void addFriend(Long userId, Long friendId) {
        log.debug("Начато добавление пользователя id = " + friendId + " в друзья к id = " + userId + ".");
        Optional<User> user = userStorage.findUser(userId);
        if (user.isEmpty()) {
            log.error("Пользователь с id = " + userId + " не найден");
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
        if (Objects.equals(userId, friendId)) {
            log.error("Нельзя добавить пользователя с id = " + userId + " в друзья самому себе.");
            throw new ValidationException("Нельзя добавить пользователя с id = " + userId + " в друзья самому себе.");
        }
        Optional<User> friend = userStorage.findUser(friendId);
        if (friend.isEmpty()) {
            log.error("Пользователь с id = " + friendId + " не найден");
            throw new NotFoundException("Пользователь с id = " + friendId + " не найден");
        }
        if (user.get().getFriends().contains(friendId)) {
            log.error("У пользователя с id = " + userId
                    + " уже есть друг с Id = " + friendId + ".");
            return;
        }
        userStorage.addFriend(userId, friendId);
        userStorage.addFriend(friendId, userId);
        log.debug("Пользователь id = " + friendId + " добавлен в друзья к id = " + userId + ".");
    }

    public void deleteFriend(Long userId, Long friendId) {
        log.debug("Начато удаление пользователя id = " + friendId + " из друзей у id = " + userId + ".");
        Optional<User> user = userStorage.findUser(userId);
        if (user.isEmpty()) {
            log.error("Пользователь с id = " + userId + " не найден");
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
        Optional<User> friend = userStorage.findUser(friendId);
        if (friend.isEmpty()) {
            log.error("Пользователь с id = " + friendId + " не найден");
            throw new NotFoundException("Пользователь с id = " + friendId + " не найден");
        }
        if (!user.get().getFriends().contains(friendId)) {
            log.error("У пользователя с id = " + userId
                    + " нет друга с Id = " + friendId + ".");
            return;
        }
        userStorage.deleteFriend(userId, friendId);
        userStorage.deleteFriend(friendId, userId);
        log.debug("Пользователь id = " + friendId + " удален из друзей у id = " + userId + ".");
    }

    public Collection<User> getFriends(Long userId) {
        userStorage.findUser(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с Id=" + userId + " не найден!"));
        return userStorage.getFriends(userId);
    }

    public Collection<User> getCommonFriends(Long userId, Long otherId) {
        userStorage.findUser(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с Id=" + userId + " не найден!"));
        userStorage.findUser(otherId)
                .orElseThrow(() -> new NotFoundException("Пользователь с Id=" + otherId + " не найден!"));
        return userStorage.getFriends(userId).stream()
                .filter(user -> userStorage.getFriends(otherId).contains(user))
                .collect(Collectors.toList());
    }

    public User update(User newUser) {
        log.debug("Начато обновление пользователя", newUser);
        // проверяем необходимые условия
        if (newUser.getId() == null) {
            log.error("Id должен быть указан");
            throw new ValidationException("Id должен быть указан");
        }

        User updatedUser = userStorage.update(newUser);

        if (updatedUser == null) {
            log.error("Пользователь с id = " + newUser.getId() + " не найден");
            throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
        }

        // проверяем на уникальность по ключу
        if (isUserWithEmailExists(newUser)) {
            throw new DuplicatedDataException("Этот имейл уже используется");
        }

        if (newUser.getName() == null || newUser.getName().isBlank()) {
            newUser.setName(newUser.getLogin());
        } else {
            newUser.setName(newUser.getName());
        }

        return updatedUser;
    }

    // вспомогательный метод для поиска пользователя по email
    private boolean isUserWithEmailExists(User newUser) {
        return findAll().stream()
                .filter(user -> user.equals(newUser))
                .anyMatch(user -> !user.getId().equals(newUser.getId()));
    }
}
