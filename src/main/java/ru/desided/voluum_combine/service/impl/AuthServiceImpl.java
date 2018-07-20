package ru.desided.voluum_combine.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.desided.voluum_combine.entity.User;
import ru.desided.voluum_combine.repository.UserRepository;
import ru.desided.voluum_combine.service.AuthService;

@Service
public class AuthServiceImpl {

    @Autowired
    UserRepository userRepository;

//    @Override
//    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
//
//        return null;
//    }
}
