package ru.desided.voluum_combine.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Cloak {
    @JsonProperty(value = "cloak_id")
    private int cloakId;
    @JsonProperty(value = "cloak_offer_name")
    private String cloakOfferName;
    @JsonProperty(value = "cloak_offer_id")
    private String cloakOfferId;
    @JsonProperty(value = "cloak_lander_name")
    private String cloakLanderName;
    @JsonProperty(value = "cloak_lander_id")
    private String cloakLanderId;
    private User nick;

    @ManyToOne
    @JoinColumn(name = "nick")
    public User getNick() {
        return nick;
    }

    public void setNick(User nick) {
        this.nick = nick;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "cloak_id")
    public int getCloakId() {
        return cloakId;
    }

    public void setCloakId(int cloakId) {
        this.cloakId = cloakId;
    }

    @Basic
    @Column(name = "cloak_offer_name")
    public String getCloakOfferName() {
        return cloakOfferName;
    }

    public void setCloakOfferName(String cloakOfferName) {
        this.cloakOfferName = cloakOfferName;
    }

    @Basic
    @Column(name = "cloak_offer_id")
    public String getCloakOfferId() {
        return cloakOfferId;
    }

    public void setCloakOfferId(String cloakOfferId) {
        this.cloakOfferId = cloakOfferId;
    }

    @Basic
    @Column(name = "cloak_lander_name")
    public String getCloakLanderName() {
        return cloakLanderName;
    }

    public void setCloakLanderName(String cloakLanderName) {
        this.cloakLanderName = cloakLanderName;
    }

    @Basic
    @Column(name = "cloak_lander_id")
    public String getCloakLanderId() {
        return cloakLanderId;
    }

    public void setCloakLanderId(String cloakLanderId) {
        this.cloakLanderId = cloakLanderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cloak cloak = (Cloak) o;
        return cloakId == cloak.cloakId &&
                Objects.equals(cloakOfferName, cloak.cloakOfferName) &&
                Objects.equals(cloakOfferId, cloak.cloakOfferId) &&
                Objects.equals(cloakLanderName, cloak.cloakLanderName) &&
                Objects.equals(cloakLanderId, cloak.cloakLanderId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(cloakId, cloakOfferName, cloakOfferId, cloakLanderName, cloakLanderId);
    }
}
