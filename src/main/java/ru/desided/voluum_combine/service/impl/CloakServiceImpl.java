package ru.desided.voluum_combine.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.desided.voluum_combine.entity.Cloak;
import ru.desided.voluum_combine.entity.User;
import ru.desided.voluum_combine.repository.CloakRepository;
import ru.desided.voluum_combine.service.CloakService;

import java.util.List;

@Service
public class CloakServiceImpl implements CloakService {

    @Autowired
    CloakRepository cloakRepository;

    @Override
    public void delete(Cloak cloak) {
        cloakRepository.delete(cloak);
    }

    @Override
    public Cloak add(Cloak cloak) {
        Cloak cloak1 = cloak;
        return cloakRepository.saveAndFlush(cloak1);
    }

    @Override
    public Cloak edit(Cloak cloak) {
        return cloakRepository.saveAndFlush(cloak);
    }

    @Override
    public List<Cloak> findAllByNick(User user) {
        return cloakRepository.findAllByNick(user);
    }

    @Override
    public Cloak findByNick(User name) {
        return cloakRepository.findByNick(name);
    }

    @Override
    public Cloak findByCloakOfferId(String string) {
        return cloakRepository.findByCloakOfferId(string);
    }
}
