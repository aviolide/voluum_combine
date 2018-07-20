package ru.desided.voluum_combine.logic.add_offer.POJO_JSON.voluum_serealize;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OfferListVoluum {
    private Integer weight;
    private OfferVoluum offer;

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public OfferVoluum getOffer() {
        return offer;
    }

    public void setOffer(OfferVoluum offer) {
        this.offer = offer;
    }
}
