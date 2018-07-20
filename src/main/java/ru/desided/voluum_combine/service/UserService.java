package ru.desided.voluum_combine.service;

import org.springframework.aop.target.LazyInitTargetSource;
import ru.desided.voluum_combine.entity.TrafficSource;
import ru.desided.voluum_combine.entity.User;

import java.util.List;


public interface UserService {
    List<User> findAllByNick(String nick);
    User findByNick(String login);
    List<User> findByUserId(Integer integer);
    void delete(User user);
    User edit(User user);
    User add(User user);
    User findByVoluumLoginAndVoluumPassword(String login, String password);
}
