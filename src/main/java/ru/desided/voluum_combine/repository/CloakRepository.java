package ru.desided.voluum_combine.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.desided.voluum_combine.entity.Cloak;
import ru.desided.voluum_combine.entity.User;

import java.util.List;

@Repository
public interface CloakRepository extends JpaRepository<Cloak, Integer> {
    Cloak findByNick(User user);
    List<Cloak> findAllByNick(User user);
    Cloak findByCloakOfferId(String string);
}
