package ru.desided.voluum_combine.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Offer {

    private int id;
    @JsonProperty(value = "offer_id")
    private String offerId;
    @JsonProperty(value = "land_id")
    private String landId;
    private String status;
    @JsonProperty(value = "offer_name")
    private String name;
    private String link;
    private String countryCode;
    private String countryName;
    private String typeTraffic;
    private String payoutConverted;
    private User user;
    private String comment;
    private String postfix;
    private AffiliateNetwork affiliateNetwork;
    @JsonProperty(value = "old_campaign")
    private Boolean oldCampaign;
    private String campaignLink;
    @JsonProperty(value = "campaign_short")
    private String campaignShort;
    private String offerCost;
    @JsonProperty(value = "smart_high")
    private Boolean smartHigh;

    @ManyToOne
    @JoinColumn(name = "nick")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name = "affiliate_network")
    public AffiliateNetwork getAffiliateNetwork() {
        return affiliateNetwork;
    }

    public void setAffiliateNetwork(AffiliateNetwork affiliateNetwork) {
        this.affiliateNetwork = affiliateNetwork;
    }

    @Basic
    @Column(name = "old_campaign")
    public Boolean getOldCampaign() {
        return oldCampaign;
    }

    public void setOldCampaign(Boolean oldCampaign) {
        this.oldCampaign = oldCampaign;
    }

    @Basic
    @Column(name = "comment")
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Basic
    @Column(name = "posfix_name")
    public String getPostfix() {
        return postfix;
    }

    public void setPostfix(String postfix) {
        this.postfix = postfix;
    }

    @Id
        @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "offer_id")
    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    @Basic
    @Column(name = "land_id")
    public String getLandId() {
        return landId;
    }

    public void setLandId(String landId) {
        this.landId = landId;
    }

    @Basic
    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "link")
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Basic
    @Column(name = "country_code")
    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @Basic
    @Column(name = "country_name")
    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    @Basic
    @Column(name = "type_traffic")
    public String getTypeTraffic() {
        return typeTraffic;
    }

    public void setTypeTraffic(String typeTraffic) {
        this.typeTraffic = typeTraffic;
    }

    @Basic
    @Column(name = "payout_converted")
    public String getPayoutConverted() {
        return payoutConverted;
    }

    public void setPayoutConverted(String payoutConverted) {
        this.payoutConverted = new BigDecimal(payoutConverted).setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Offer offer = (Offer) o;
        return id == offer.id &&
                Objects.equals(offerId, offer.offerId) &&
                Objects.equals(landId, offer.landId) &&
                Objects.equals(status, offer.status) &&
                Objects.equals(name, offer.name) &&
                Objects.equals(link, offer.link) &&
                Objects.equals(countryCode, offer.countryCode) &&
                Objects.equals(countryName, offer.countryName) &&
                Objects.equals(typeTraffic, offer.typeTraffic) &&
                Objects.equals(payoutConverted, offer.payoutConverted);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, offerId, landId, status, name, link, countryCode, countryName, typeTraffic, payoutConverted);
    }

    @Basic
    @Column(name = "campaign_link")
    public String getCampaignLink() {
        return campaignLink;
    }

    public void setCampaignLink(String campaignLink) {
        this.campaignLink = campaignLink;
    }

    @Basic
    @Column(name = "campaign_short")
    public String getCampaignShort() {
        return campaignShort;
    }

    public void setCampaignShort(String campaignShort) {
        this.campaignShort = campaignShort;
    }

    @Basic
    @Column(name = "offer_cost")
    public String getOfferCost() {
        return offerCost;
    }

    public void setOfferCost(String offerCost) {
        this.offerCost = offerCost;
    }

    @Basic
    @Column(name = "smart_high")
    public Boolean getSmartHigh() {
        return smartHigh;
    }

    public void setSmartHigh(Boolean smartHigh) {
        this.smartHigh = smartHigh;
    }
}
