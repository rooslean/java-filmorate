package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(int userId, int friendId) {
        User user = userStorage.getUserById(userId);
        if (user == null) {
            log.info(String.format("Пользователь c id - %d не найден", userId));
            throw new UserNotFoundException(userId);
        }
        User friend = userStorage.getUserById(friendId);
        if (friend == null) {
            log.info(String.format("Пользователь c id - %d не найден", friendId));
            throw new UserNotFoundException(friendId);
        }
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        userStorage.save(user);
        userStorage.save(friend);
    }

    public void deleteFriend(int userId, int friendId) {
        User user = userStorage.getUserById(userId);
        if (user == null) {
            log.info(String.format("Пользователь c id - %d не найден", userId));
            throw new UserNotFoundException(userId);
        }
        User friend = userStorage.getUserById(friendId);
        if (friend == null) {
            log.info(String.format("Пользователь c id - %d не найден", friendId));
            throw new UserNotFoundException(friendId);
        }
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        userStorage.save(user);
        userStorage.save(friend);
    }

    public Set<Integer> getFriendsList(int userId) {
        User user = userStorage.getUserById(userId);
        if (user == null) {
            log.info(String.format("Пользователь c id - %d не найден", userId));
            throw new UserNotFoundException(userId);
        }
        return user.getFriends();
    }

    public List<User> getCommonFriendsList(int userId, int friendId) {
        Set<Integer> userFriends = getFriendsList(userId);
        Set<Integer> friendFriends = getFriendsList(friendId);
        userFriends.removeIf(id -> !friendFriends.contains(id));
        return userFriends.stream().map(id -> userStorage.getUserById(id)).collect(Collectors.toList());
    }
}
