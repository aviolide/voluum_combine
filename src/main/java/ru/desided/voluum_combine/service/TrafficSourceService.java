package ru.desided.voluum_combine.service;

import ru.desided.voluum_combine.entity.TrafficSource;
import ru.desided.voluum_combine.entity.User;

import java.util.List;

public interface TrafficSourceService {
    void delete(TrafficSource trafficSource);
    TrafficSource edit(TrafficSource trafficSource);
    List<TrafficSource> findAll();
    TrafficSource add(TrafficSource trafficSource);
    List<TrafficSource> findAllByUser(User nickname);
    TrafficSource findByNameVoluum(String string);
    TrafficSource findByIdVoluum(String string);
}
