package ru.desided.voluum_combine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.desided.voluum_combine.entity.TrafficSource;
import ru.desided.voluum_combine.entity.User;

import java.util.List;

@Repository
public interface TrafficSourceRepository extends JpaRepository<TrafficSource, Integer> {
    List<TrafficSource> findAllByUser(User nickname);
    TrafficSource findByNameVoluum(String string);
    TrafficSource findByIdVoluum(String string);

}
