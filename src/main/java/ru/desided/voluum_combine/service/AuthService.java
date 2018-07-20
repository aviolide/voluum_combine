package ru.desided.voluum_combine.service;

import ru.desided.voluum_combine.entity.User;

public interface AuthService {
    void addUser(User user);
    void deleteUser(User user);
    User getUser();
}
