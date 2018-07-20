package ru.desided.voluum_combine.service;

import ru.desided.voluum_combine.entity.AffiliateNetwork;
import ru.desided.voluum_combine.entity.Offer;
import ru.desided.voluum_combine.entity.User;

import java.util.List;

public interface OfferService  {
    Offer addOffer(Offer offer);
    void deleteOffer(Offer offer);
    Offer getByName(String name);
    Offer editOffer(Offer offer);
    List<Offer> getAll();
    void saveAll(List<Offer> offers);
    List<Offer> findByStatusEquals(String string);
    Offer findByOfferIdAndAffiliateNetwork(Offer offer, AffiliateNetwork affiliateNetwork);

}
