package ru.desided.voluum_combine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.desided.voluum_combine.entity.AffiliateNetwork;
import ru.desided.voluum_combine.entity.User;

import java.util.List;

@Repository
public interface AffiliateNetworkRepository extends JpaRepository<AffiliateNetwork, Integer>  {

    List<AffiliateNetwork> findAllByUser(User nick);
    AffiliateNetwork findByNameVoluum(String string);
    AffiliateNetwork findByIdVoluum(String string);

}
