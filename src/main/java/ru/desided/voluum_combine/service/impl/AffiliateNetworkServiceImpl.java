package ru.desided.voluum_combine.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.desided.voluum_combine.entity.AffiliateNetwork;
import ru.desided.voluum_combine.entity.User;
import ru.desided.voluum_combine.repository.AffiliateNetworkRepository;
import ru.desided.voluum_combine.service.AffiliateNetworkService;

import java.util.List;

@Service
public class AffiliateNetworkServiceImpl implements AffiliateNetworkService {
    @Autowired
    AffiliateNetworkRepository affiliateNetworkRepository;

    @Override
    public void delete(AffiliateNetwork affiliateNetwork) {
        affiliateNetworkRepository.delete(affiliateNetwork);
    }

    @Override
    public AffiliateNetwork add(AffiliateNetwork affiliateNetwork) {
        AffiliateNetwork saveAffiliateNetwork = affiliateNetwork;
        return affiliateNetworkRepository.saveAndFlush(saveAffiliateNetwork);

    }

    @Override
    public AffiliateNetwork edit(AffiliateNetwork affiliateNetwork) {
        return affiliateNetworkRepository.saveAndFlush(affiliateNetwork);
    }

    @Override
    public List<AffiliateNetwork> getAll() {
        return affiliateNetworkRepository.findAll();
    }

    @Override
    public AffiliateNetwork findByNameVoluum(String name) {
        return affiliateNetworkRepository.findByNameVoluum(name);
    }

    @Override
    public List<AffiliateNetwork> findAllByUser(User nick) {
        return affiliateNetworkRepository.findAllByUser(nick);
    }

    @Override
    public AffiliateNetwork findByIdVoluum(String string) {
        return affiliateNetworkRepository.findByIdVoluum(string);
    }

}
