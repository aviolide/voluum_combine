package ru.desided.voluum_combine.logic.add_offer.POJO_JSON.Advidi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonObject {

    private String id;
    private String status;
    @JsonProperty(value = "price_converted")
    private String priceConverted;
    @JsonProperty(value = "campaign_id")
    private String campaignId;
    @JsonProperty(value = "campaignId")
    private String campaignIdVoluum;
    @JsonProperty(value = "contract_id")
    private String contractId;
    private String name;
    @JsonProperty(value = "offer_id")
    private String offerId;
    @JsonProperty(value = "offer_name")
    private String offerName;
    @JsonProperty(value = "payout_converted")
    private String payoutConverted;
    @JsonProperty(value = "allowed_countries")
    private List<Allowed_Countries> allowedCountries;
    @JsonProperty(value = "unique_link")

    private String uniqueLink;
    private BigDecimal visits;
    private BigDecimal clicks;
    private BigDecimal conversions;
    private BigDecimal revenue;
    private BigDecimal roi;
    private BigDecimal cost;
    private BigDecimal profit;
    private BigDecimal cpv;
    private BigDecimal ctr;
    private BigDecimal cr;
    private BigDecimal cv;
    private BigDecimal epv;
    private BigDecimal epc;
    private Date day;
    private String customVariable3;

    public String getCampaignIdVoluum() {
        return campaignIdVoluum;
    }

    public void setCampaignIdVoluum(String campaignIdVoluum) {
        this.campaignIdVoluum = campaignIdVoluum;
    }

    public String getCustomVariable3() {
        return customVariable3;
    }

    public void setCustomVariable3(String customVariable3) {
        this.customVariable3 = customVariable3;
    }

    public String getUniqueLink() {
        return uniqueLink;
    }

    public void setUniqueLink(String uniqueLink) {
        this.uniqueLink = uniqueLink;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public List<Allowed_Countries> getAllowedCountries() {
        return allowedCountries;
    }

    public void setAllowedCountries(List<Allowed_Countries> allowedCountries) {
        this.allowedCountries = allowedCountries;
    }

    public String getPriceConverted() {
        return priceConverted;
    }

    public void setPriceConverted(String priceConverted) {
        this.priceConverted = priceConverted;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPayoutConverted() {
        return payoutConverted;
    }

    public void setPayoutConverted(String payoutConverted) {
        this.payoutConverted = payoutConverted;
    }

    public BigDecimal getCpv() {
        return cpv;
    }

    public void setCpv(BigDecimal cpv) {
        this.cpv = cpv;
    }

    public BigDecimal getCtr() {
        return ctr;
    }

    public void setCtr(BigDecimal ctr) {
        this.ctr = ctr;
    }

    public BigDecimal getCr() {
        return cr;
    }

    public void setCr(BigDecimal cr) {
        this.cr = cr;
    }

    public BigDecimal getCv() {
        return cv;
    }

    public void setCv(BigDecimal cv) {
        this.cv = cv;
    }

    public BigDecimal getEpv() {
        return epv;
    }

    public void setEpv(BigDecimal epv) {
        this.epv = epv;
    }

    public BigDecimal getEpc() {
        return epc;
    }

    public void setEpc(BigDecimal epc) {
        this.epc = epc;
    }

    public BigDecimal getConversions() {
        return conversions;
    }

    public void setConversions(BigDecimal conversions) {
        this.conversions = conversions;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }
    public BigDecimal getVisits() {
        return visits;
    }

    public void setVisits(BigDecimal visits) {
        this.visits = visits;
    }

    public BigDecimal getClicks() {
        return clicks;
    }

    public void setClicks(BigDecimal clicks) {
        this.clicks = clicks;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }

    public void setRevenue(BigDecimal revenue) {
        this.revenue = revenue.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getRoi() {
        return roi;
    }

    public void setRoi(BigDecimal roi) {
        this.roi = roi.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit.setScale(2, RoundingMode.HALF_UP);
    }
}
