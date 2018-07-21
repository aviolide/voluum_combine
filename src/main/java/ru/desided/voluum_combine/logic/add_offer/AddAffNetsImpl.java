package ru.desided.voluum_combine.logic.add_offer;

import ru.desided.voluum_combine.entity.*;
import ru.desided.voluum_combine.logic.add_offer.AddAffNets;
import ru.desided.voluum_combine.logic.add_offer.impl.Adtrafico;
import ru.desided.voluum_combine.logic.add_offer.impl.Advidi;
import ru.desided.voluum_combine.logic.add_offer.impl.ClickDealer;
import ru.desided.voluum_combine.logic.add_offer.impl.Voluum;
import ru.desided.voluum_combine.service.CountryService;

import java.io.IOException;

public class AddAffNetsImpl implements AddAffNets {

    @Override
    public Offer addAdvidi(AffiliateNetwork affiliateNetwork, TrafficSource trafficSource,
                                 Offer offer, CountryService countryService, User user, Cloak cloak) throws IOException {
        Advidi advidi = new Advidi(affiliateNetwork, trafficSource, offer, countryService, user, cloak);
        offer.setUser(user);
        offer.setAffiliateNetwork(affiliateNetwork);
        advidi.login();
        advidi.addOffer();

        if (offer.getStatus().equalsIgnoreCase("Active")){
            advidi.getCreo();
            advidi.addVoluum();
            if (offer.getSmartHigh()){
                advidi.propellerHigh();
            } else {
                advidi.propellerSmart();
            }
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
            clickDealer.login();
            clickDealer.addOffer();

            if (offer.getStatus().equals("Active")) {
                clickDealer.getCreo();
                clickDealer.addVoluum();
                if (offer.getSmartHigh()){
                    clickDealer.propellerHigh();
                } else {
                    clickDealer.propellerSmart();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return offer;
    }

    @Override
    public Offer addAdtrafico(AffiliateNetwork affiliateNetwork, TrafficSource trafficSource, Offer offer,
                              CountryService countryService, User user, Cloak cloak) throws IOException {
        offer.setUser(user);

        offer.setAffiliateNetwork(affiliateNetwork);
        Adtrafico adtrafico = new Adtrafico(affiliateNetwork, trafficSource, offer, countryService, user, cloak);
        adtrafico.login();
        adtrafico.addOffer();
        if (offer.getStatus().equalsIgnoreCase("active")) {
            adtrafico.getCreo();
            adtrafico.addVoluum();
            if (offer.getSmartHigh()){
                adtrafico.propellerHigh();
            } else {
                adtrafico.propellerSmart();
            }
        }
    return offer;
    }

    @Override
    public void CheckPendingOffers() {

    }

}
