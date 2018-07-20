package ru.desided.voluum_combine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.desided.voluum_combine.entity.Countries;
import ru.desided.voluum_combine.entity.User;

@Repository
public interface CountriesRepository extends JpaRepository<Countries, Integer> {
    Countries findByCountryCode(String code);
    Countries findByCountryName(String name);

}
