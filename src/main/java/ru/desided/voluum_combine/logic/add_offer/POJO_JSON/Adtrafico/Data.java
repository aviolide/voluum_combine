package ru.desided.voluum_combine.logic.add_offer.POJO_JSON.Adtrafico;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.util.JSONPObject;
import ru.desided.voluum_combine.Main;
import ru.desided.voluum_combine.entity.Offer;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Data {
    @JsonProperty(value = "Offer")
    private OfferAdtrafico Offer;
    @JsonProperty(value = "Countries")
    private Map<String, JsonNode> Countries;
    private String click_url;

    public Map<String, JsonNode> getCountries() {
        return Countries;
    }

    public void setCountries(Map<String, JsonNode> countries) {
        Countries = countries;
    }

    public OfferAdtrafico getOffer() {
        return Offer;
    }

    public void setOffer(OfferAdtrafico offer) {
        Offer = offer;
    }

    public String getClick_url() {
        return click_url;
    }

    public void setClick_url(String click_url) {
        this.click_url = click_url;
    }
}
