package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.debug("Начато создание пользователя", user);
        // проверяем на уникальность по ключу
        if (isUserWithEmailExists(user)) {
            log.error("Этот email уже используется");
            throw new DuplicatedDataException("Этот email уже используется");
        }
        // формируем дополнительные данные
        user.setId(getNextId());
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("Имя пользователя обновлено на " + user.getName());
        }

        // сохраняем нового пользователя в памяти приложения
        users.put(user.getId(), user);
        log.debug("Пользователь создан", user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
        log.debug("Начато обновление пользователя", newUser);
        // проверяем необходимые условия
        if (newUser.getId() == null) {
            log.error("Id должен быть указан");
            throw new ValidationException("Id должен быть указан");
        }

        // проверяем необходимые условия
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            // проверяем на уникальность по ключу
            if (isUserWithEmailExists(newUser)) {
                throw new DuplicatedDataException("Этот имейл уже используется");
            }
            // если пользователь найден и все условия соблюдены, обновляем его
            oldUser.setEmail(newUser.getEmail());
            oldUser.setBirthday(newUser.getBirthday());
            if (newUser.getName() == null || newUser.getName().isBlank()) {
                oldUser.setName(newUser.getLogin());
            } else {
                oldUser.setName(newUser.getName());
            }
            oldUser.setLogin(newUser.getLogin());
            log.debug("Пользователь обновлен: предыдущее значение", oldUser);
            log.debug("Пользователь обновлен: новое значение", newUser);
            return oldUser;
        }
        log.error("Пользователь с id = " + newUser.getId() + " не найден");
        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
    }

    // вспомогательный метод для поиска пользователя по email
    private boolean isUserWithEmailExists(User newUser) {
        return users.values().stream()
                .filter(user -> user.equals(newUser))
                .anyMatch(user -> !user.getId().equals(newUser.getId()));
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
