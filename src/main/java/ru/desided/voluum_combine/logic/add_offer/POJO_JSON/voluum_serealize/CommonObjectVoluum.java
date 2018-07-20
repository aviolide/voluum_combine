package ru.desided.voluum_combine.logic.add_offer.POJO_JSON.voluum_serealize;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonObjectVoluum {

    private String id;
    private String name;
    private String offerName;
    private String os;
    private String browser;
    private String flowName;
    private String flowWorkspaceName;
    private String flowId;
    private String flowWorkspaceId;
    private String campaignId;
    private BigDecimal visits;
    private JsonNode redirectTarget;
    private String customVariable3;

    public String getCustomVariable3() {
        return customVariable3;
    }

    public void setCustomVariable3(String customVariable3) {
        this.customVariable3 = customVariable3;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public BigDecimal getVisits() {
        return visits;
    }

    public void setVisits(BigDecimal visits) {
        this.visits = visits;
    }

    public JsonNode getRedirectTarget() {
        return redirectTarget;
    }

    public void setRedirectTarget(JsonNode redirectTarget) {
        this.redirectTarget = redirectTarget;
    }

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

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

    public String getFlowWorkspaceName() {
        return flowWorkspaceName;
    }

    public void setFlowWorkspaceName(String flowWorkspaceName) {
        this.flowWorkspaceName = flowWorkspaceName;
    }

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getFlowWorkspaceId() {
        return flowWorkspaceId;
    }

    public void setFlowWorkspaceId(String flowWorkspaceId) {
        this.flowWorkspaceId = flowWorkspaceId;
    }
}
