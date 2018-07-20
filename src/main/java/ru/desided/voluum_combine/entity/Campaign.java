package ru.desided.voluum_combine.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Campaign {
    private int id;
    private String name;
    private String flow;
    private String voluumId;
    private String voluumShortId;
    private BigDecimal impressions;
    private BigDecimal spent;
    @JsonProperty(value = "status")
    private BigDecimal statusCampaign;
    private BigDecimal conversions;
    private Boolean cloak = false;
    private BigDecimal price;
    private Integer campaignId;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
    @Column(name = "flow")
    public String getFlow() {
        return flow;
    }

    public void setFlow(String flow) {
        this.flow = flow;
    }

    @Basic
    @Column(name = "voluum_id")
    public String getVoluumId() {
        return voluumId;
    }

    public void setVoluumId(String voluumId) {
        this.voluumId = voluumId;
    }

    @Basic
    @Column(name = "voluum_short_id")
    public String getVoluumShortId() {
        return voluumShortId;
    }

    public void setVoluumShortId(String voluumShortId) {
        this.voluumShortId = voluumShortId;
    }

    @Basic
    @Column(name = "impressions")
    public BigDecimal getImpressions() {
        return impressions;
    }

    public void setImpressions(BigDecimal impressions) {
        this.impressions = impressions;
    }

    @Basic
    @Column(name = "spent")
    public BigDecimal getSpent() {
        return spent;
    }

    public void setSpent(BigDecimal spent) {
        this.spent = spent;
    }

    @Basic
    @Column(name = "status_campaign")
    public BigDecimal getStatusCampaign() {
        return statusCampaign;
    }

    public void setStatusCampaign(BigDecimal statusCampaign) {
        this.statusCampaign = statusCampaign;
    }

    @Basic
    @Column(name = "conversions")
    public BigDecimal getConversions() {
        return conversions;
    }

    public void setConversions(BigDecimal conversions) {
        this.conversions = conversions;
    }

    @Basic
    @Column(name = "cloak")
    public Boolean getCloak() {
        return cloak;
    }

    public void setCloak(Boolean cloak) {
        this.cloak = cloak;
    }

    @Basic
    @Column(name = "price")
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Campaign campaign = (Campaign) o;
        return id == campaign.id &&
                Objects.equals(name, campaign.name) &&
                Objects.equals(flow, campaign.flow) &&
                Objects.equals(voluumId, campaign.voluumId) &&
                Objects.equals(voluumShortId, campaign.voluumShortId) &&
                Objects.equals(impressions, campaign.impressions) &&
                Objects.equals(spent, campaign.spent) &&
                Objects.equals(statusCampaign, campaign.statusCampaign) &&
                Objects.equals(conversions, campaign.conversions) &&
                Objects.equals(cloak, campaign.cloak) &&
                Objects.equals(price, campaign.price);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, flow, voluumId, voluumShortId, impressions, spent, statusCampaign, conversions, cloak, price);
    }

    @Basic
    @Column(name = "campaign_id")
    public Integer getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Integer campaignId) {
        this.campaignId = campaignId;
    }
}
