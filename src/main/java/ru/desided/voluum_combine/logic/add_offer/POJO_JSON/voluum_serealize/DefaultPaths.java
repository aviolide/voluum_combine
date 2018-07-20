package ru.desided.voluum_combine.logic.add_offer.POJO_JSON.voluum_serealize;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DefaultPaths {
    private String id;
    private String name;
    private Boolean active;
    private Integer weight;
    private List<OfferListVoluum> offers;
    private List<JsonNode> landers;
    private JsonNode offerRedirectMode;
    private JsonNode realtimeRoutingApiState;
    private Boolean autoOptimized;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public List<OfferListVoluum> getOffers() {
        return offers;
    }

    public void setOffers(List<OfferListVoluum> offers) {
        this.offers = offers;
    }

    public List<JsonNode> getLanders() {
        return landers;
    }

    public void setLanders(List<JsonNode> landers) {
        this.landers = landers;
    }

    public JsonNode getOfferRedirectMode() {
        return offerRedirectMode;
    }

    public void setOfferRedirectMode(JsonNode offerRedirectMode) {
        this.offerRedirectMode = offerRedirectMode;
    }

    public JsonNode getRealtimeRoutingApiState() {
        return realtimeRoutingApiState;
    }

    public void setRealtimeRoutingApiState(JsonNode realtimeRoutingApiState) {
        this.realtimeRoutingApiState = realtimeRoutingApiState;
    }

    public Boolean getAutoOptimized() {
        return autoOptimized;
    }

    public void setAutoOptimized(Boolean autoOptimized) {
        this.autoOptimized = autoOptimized;
    }
}
