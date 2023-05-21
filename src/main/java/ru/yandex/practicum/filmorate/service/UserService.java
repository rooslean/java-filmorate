package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;
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
        User friend = userStorage.getUserById(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        userStorage.save(user);
        userStorage.save(friend);
    }

    public void deleteFriend(int userId, int friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        userStorage.save(user);
        userStorage.save(friend);
    }

    public List<User> getFriendsList(int userId) {
        User user = userStorage.getUserById(userId);
        return user.getFriends().stream().map(id -> userStorage.getUserById(id)).collect(Collectors.toList());
    }

    public List<User> getCommonFriendsList(int userId, int otherId) {
        User user = userStorage.getUserById(userId);
        User other = userStorage.getUserById(otherId);
        return user.getFriends().stream()
                .filter(other.getFriends()::contains)
                .map(id -> userStorage.getUserById(id))
                .collect(Collectors.toList());
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User save(User user) {
        user.setFriends(userStorage.getUserById(user.getId()).getFriends());
        return userStorage.save(user);
    }

    public Collection<User> getAll() {
        return userStorage.getAll();
    }

    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }
}
