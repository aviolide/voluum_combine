package ru.desided.voluum_combine.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.desided.voluum_combine.entity.Countries;
import ru.desided.voluum_combine.repository.CountriesRepository;
import ru.desided.voluum_combine.repository.OfferRepository;
import ru.desided.voluum_combine.service.CountryService;

@Service
public class CountryServiceImpl implements CountryService {

    @Autowired
    CountriesRepository countriesRepository;


    @Override
    public Countries findByCountryCode(String code) {
        return countriesRepository.findByCountryCode(code);
    }

    @Override
    public Countries findByCountryName(String name) {
        return countriesRepository.findByCountryName(name);
    }
}
