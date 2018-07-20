package ru.desided.voluum_combine.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.desided.voluum_combine.entity.TrafficSource;
import ru.desided.voluum_combine.entity.User;
import ru.desided.voluum_combine.repository.TrafficSourceRepository;
import ru.desided.voluum_combine.service.TrafficSourceService;

import java.util.List;

@Service
public class TrafficSourceServiceImpl implements TrafficSourceService {

    @Autowired
    private TrafficSourceRepository trafficSourceRepository;

    @Override
    public void delete(TrafficSource trafficSource) {
        trafficSourceRepository.delete(trafficSource);
    }

    @Override
    public TrafficSource edit(TrafficSource trafficSource) {
        return trafficSourceRepository.saveAndFlush(trafficSource);
    }

    @Override
    public List<TrafficSource> findAll() {
        return trafficSourceRepository.findAll();
    }

    @Override
    public TrafficSource add(TrafficSource trafficSource) {
        TrafficSource trafficSourceSave = trafficSource;
        return trafficSourceRepository.saveAndFlush(trafficSourceSave);
    }

    @Override
    public List<TrafficSource> findAllByUser(User nickname) {
        return trafficSourceRepository.findAllByUser(nickname);
    }

    @Override
    public TrafficSource findByNameVoluum(String string) {
        return trafficSourceRepository.findByNameVoluum(string);
    }

    @Override
    public TrafficSource findByIdVoluum(String string) {
        return trafficSourceRepository.findByIdVoluum(string);
    }


}
