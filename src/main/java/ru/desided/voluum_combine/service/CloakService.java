package ru.desided.voluum_combine.service;

import ru.desided.voluum_combine.entity.Cloak;
import ru.desided.voluum_combine.entity.User;

import java.util.List;

public interface CloakService {
    void delete(Cloak cloak);
    Cloak add(Cloak cloak);
    Cloak edit(Cloak cloak);
    List<Cloak> findAllByNick(User user);
    Cloak findByNick(User name);
    Cloak findByCloakOfferId(String string);
}
