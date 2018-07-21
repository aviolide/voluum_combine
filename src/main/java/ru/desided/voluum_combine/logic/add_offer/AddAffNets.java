package ru.desided.voluum_combine.logic.add_offer;

import ru.desided.voluum_combine.entity.*;
import ru.desided.voluum_combine.service.CountryService;

import java.io.IOException;
import java.util.List;

public interface AddAffNets {
    Offer addAdvidi(AffiliateNetwork affiliateNetwork, TrafficSource trafficSource,
                        Offer offer, CountryService countryService,
                        User user, Cloak cloak) throws IOException;
    Offer addClickDealer(AffiliateNetwork affiliateNetwork, TrafficSource trafficSource,
                        Offer offer, CountryService countryService, User user, Cloak cloak);
    Offer addAdtrafico(AffiliateNetwork affiliateNetwork, TrafficSource trafficSource,
                       Offer offer, CountryService countryService, User user, Cloak cloak) throws IOException;
    void CheckPendingOffers();
}
