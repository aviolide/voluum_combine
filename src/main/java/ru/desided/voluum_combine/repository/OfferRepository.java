package ru.desided.voluum_combine.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.desided.voluum_combine.entity.AffiliateNetwork;
import ru.desided.voluum_combine.entity.Offer;

import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Integer> {
    Offer findByOfferId(String id);
    List<Offer> findAllByStatusEquals(String string);
    Offer findByOfferIdAndAffiliateNetwork(Offer offer, AffiliateNetwork affiliateNetwork);
}
