package ru.desided.voluum_combine.service;

import org.springframework.stereotype.Service;
import ru.desided.voluum_combine.entity.Countries;

import javax.xml.ws.ServiceMode;


public interface CountryService {
    Countries findByCountryCode(String code);
    Countries findByCountryName(String name);
}
