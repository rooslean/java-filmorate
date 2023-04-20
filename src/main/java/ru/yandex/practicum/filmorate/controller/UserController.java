package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 1;

    @PostMapping
    public User create(@RequestBody User user) {
        user.setId(id);
        users.put(id, user);
        id++;

        return user;
    }

    @PutMapping
    public User save(@RequestBody User user) {
        users.put(user.getId(), user);

        return user;
    }

    @GetMapping
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }
}
