package ru.desided.voluum_combine.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.desided.voluum_combine.entity.User;
import ru.desided.voluum_combine.repository.AffiliateNetworkRepository;
import ru.desided.voluum_combine.repository.UserRepository;
import ru.desided.voluum_combine.service.UserService;

import javax.jws.soap.SOAPBinding;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;


    @Override
    public List<User> findAllByNick(String nick) {
        return userRepository.findAllByNick(nick);
    }

    @Override
    public User findByNick(String login) {
        return userRepository.findByNick(login);
    }

    @Override
    public List<User> findByUserId(Integer integer) {
        return null;
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    public User edit(User user) {
        return userRepository.saveAndFlush(user);
    }

    @Override
    public User add(User user) {
        return userRepository.saveAndFlush(user);
    }

    @Override
    public User findByVoluumLoginAndVoluumPassword(String login, String password) {
        User user = userRepository.findByVoluumLoginAndVoluumPassword(login, password);
        if (user != null){
            return user;
        }
        return null;
    }
}
