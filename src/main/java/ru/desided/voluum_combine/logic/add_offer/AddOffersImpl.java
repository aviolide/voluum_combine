package ru.desided.voluum_combine.logic.add_offer;

import ru.desided.voluum_combine.entity.*;
import ru.desided.voluum_combine.logic.add_offer.impl.Adtrafico;
import ru.desided.voluum_combine.logic.add_offer.impl.Advidi;
import ru.desided.voluum_combine.logic.add_offer.impl.ClickDealer;
import ru.desided.voluum_combine.logic.add_offer.impl.Voluum;
import ru.desided.voluum_combine.service.CountryService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddOffersImpl implements AddOffers {
    @Override
    public void Auth() {

    }

    @Override
    public Offer addAdvidi(AffiliateNetwork affiliateNetwork, TrafficSource trafficSource,
                                 Offer offer, CountryService countryService, User user, Cloak cloak) throws IOException {
        Advidi advidi = new Advidi();
        offer.setUser(user);
        offer.setAffiliateNetwork(affiliateNetwork);
        advidi.startAdvidi(affiliateNetwork, offer, countryService);

        if (offer.getStatus().equalsIgnoreCase("Active")){
            Voluum voluum = new Voluum(true, offer, affiliateNetwork, trafficSource, user, cloak, countryService);
            voluum.voluumAuth();
            voluum.setupVoluum();
        }

        return offer;
    }

    @Override
    public Offer addClickDealer(AffiliateNetwork affiliateNetwork, TrafficSource trafficSource,
                                Offer offer, CountryService countryService, User user, Cloak cloak) {
        try {
            offer.setUser(user);
            offer.setAffiliateNetwork(affiliateNetwork);
            ClickDealer clickDealer = new ClickDealer(affiliateNetwork, trafficSource, offer, countryService, user, cloak);
            clickDealer.setup();
            clickDealer.addOffer();
            if (offer.getStatus().equals("Active")) {
                clickDealer.getCreo();
                clickDealer.addVoluum();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return offer;
    }

    @Override
    public Offer addAdtrafico(AffiliateNetwork affiliateNetwork, TrafficSource trafficSource, Offer offer, CountryService countryService, User user, Cloak cloak) throws IOException {
        offer.setUser(user);

        offer.setAffiliateNetwork(affiliateNetwork);
        Adtrafico adtrafico = new Adtrafico(affiliateNetwork, trafficSource, offer, countryService, user, cloak);
        adtrafico.setup();
        adtrafico.addOffer();
        if (offer.getStatus().equalsIgnoreCase("active")) {
            adtrafico.getCreo();
            adtrafico.addVoluum();
        }
    return offer;
    }

    @Override
    public void AddVoluum() {

    }

    @Override
    public void CheckPendingOffers() {

    }

    @Override
    public void Start() {

    }
}
