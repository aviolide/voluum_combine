package ru.desided.voluum_combine.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.desided.voluum_combine.entity.AffiliateNetwork;
import ru.desided.voluum_combine.entity.Offer;
import ru.desided.voluum_combine.entity.User;
import ru.desided.voluum_combine.repository.OfferRepository;
import ru.desided.voluum_combine.service.OfferService;

import java.util.List;

@Service
public class OfferServiceImpl implements OfferService {

    @Autowired
    private OfferRepository offerRepository;

    @Override
    public Offer addOffer(Offer offer) {
        Offer saveOffer = offerRepository.saveAndFlush(offer);
        return saveOffer;
    }

    @Override
    public void deleteOffer(Offer offer) {
        offerRepository.delete(offer);
    }

    @Override
    public Offer getByName(String name) {
        return offerRepository.findByOfferId(name);
    }

    @Override
    public Offer editOffer(Offer offer) {
        return offerRepository.saveAndFlush(offer);
    }

    @Override
    public List<Offer> getAll() {
        return offerRepository.findAll();
    }

    @Override
    public void saveAll(List<Offer> offers) {
        for (Offer offer : offers){
            offerRepository.saveAndFlush(offer);
        }
    }

    @Override
    public List<Offer> findByStatusEquals(String string) {
        return offerRepository.findAllByStatusEquals(string);
    }

    @Override
    public Offer findByOfferIdAndAffiliateNetwork(Offer offer, AffiliateNetwork affiliateNetwork) {
        return offerRepository.findByOfferIdAndAffiliateNetwork(offer, affiliateNetwork);
    }

}
