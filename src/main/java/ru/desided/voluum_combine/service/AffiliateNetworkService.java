package ru.desided.voluum_combine.service;

import ru.desided.voluum_combine.entity.AffiliateNetwork;
import ru.desided.voluum_combine.entity.User;

import javax.jws.soap.SOAPBinding;
import java.util.List;

public interface AffiliateNetworkService {
    void delete(AffiliateNetwork affiliateNetwork);
    AffiliateNetwork add(AffiliateNetwork affiliateNetwork);
    AffiliateNetwork edit(AffiliateNetwork affiliateNetwork);
    List<AffiliateNetwork> getAll();
    AffiliateNetwork findByNameVoluum(String name);
    List<AffiliateNetwork> findAllByUser(User nick);
    AffiliateNetwork findByIdVoluum(String string);
}
