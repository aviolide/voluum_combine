package ru.desided.voluum_combine.logic.add_offer.POJO_JSON.ClickDealer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.desided.voluum_combine.logic.add_offer.POJO_JSON.Advidi.Allowed_Countries;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonObjectCD {

    @JsonProperty(value = "offer_contract_id")
    private String offerContractId;
    @JsonProperty(value = "campaign_id")
    private String campaignId;
    @JsonProperty(value = "offer_id")
    private String offerId;
    @JsonProperty(value = "offer_name")
    private String offerName;
    @JsonProperty(value = "vertical_id")
    private String verticalId;
    @JsonProperty(value = "offer_status")
    private Map<String, String> offerStatus;
    @JsonProperty(value = "payout_converted")
    private String payout;
    @JsonProperty(value = "allowed_countries")
    private List<Allowed_Countries> allowedCountries;
    private List<Creative> creatives;
    private String link;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<Creative> getCreatives() {
        return creatives;
    }

    public void setCreatives(List<Creative> creatives) {
        this.creatives = creatives;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getOfferContractId() {
        return offerContractId;
    }

    public void setOfferContractId(String offerContractId) {
        this.offerContractId = offerContractId;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public String getVerticalId() {
        return verticalId;
    }

    public void setVerticalId(String verticalId) {
        this.verticalId = verticalId;
    }

    public Map<String, String> getOfferStatus() {
        return offerStatus;
    }

    public void setOfferStatus(Map<String, String> offerStatus) {
        this.offerStatus = offerStatus;
    }

    public String getPayout() {
        return payout;
    }

    public void setPayout(String payout) {
        this.payout = payout;
    }

    public List<Allowed_Countries> getAllowedCountries() {
        return allowedCountries;
    }

    public void setAllowedCountries(List<Allowed_Countries> allowedCountries) {
        this.allowedCountries = allowedCountries;
    }
}
