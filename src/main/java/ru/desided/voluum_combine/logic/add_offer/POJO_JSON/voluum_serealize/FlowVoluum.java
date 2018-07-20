package ru.desided.voluum_combine.logic.add_offer.POJO_JSON.voluum_serealize;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FlowVoluum {
    private String id;
    private String name;
    private List<JsonNode> countries;
    private List<DefaultPaths> defaultPaths;
    private List<JsonNode> conditionalPathsGroups;
    private JsonNode defaultOfferRedirectMode;
    private JsonNode realtimeRoutingApi;
    private JsonNode workspace;
    private List<JsonNode> allowedActions;

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

    public List<JsonNode> getCountries() {
        return countries;
    }

    public void setCountries(List<JsonNode> countries) {
        this.countries = countries;
    }

    public List<DefaultPaths> getDefaultPaths() {
        return defaultPaths;
    }

    public void setDefaultPaths(List<DefaultPaths> defaultPaths) {
        this.defaultPaths = defaultPaths;
    }

    public List<JsonNode> getConditionalPathsGroups() {
        return conditionalPathsGroups;
    }

    public void setConditionalPathsGroups(List<JsonNode> conditionalPathsGroups) {
        this.conditionalPathsGroups = conditionalPathsGroups;
    }

    public JsonNode getDefaultOfferRedirectMode() {
        return defaultOfferRedirectMode;
    }

    public void setDefaultOfferRedirectMode(JsonNode defaultOfferRedirectMode) {
        this.defaultOfferRedirectMode = defaultOfferRedirectMode;
    }

    public JsonNode getRealtimeRoutingApi() {
        return realtimeRoutingApi;
    }

    public void setRealtimeRoutingApi(JsonNode realtimeRoutingApi) {
        this.realtimeRoutingApi = realtimeRoutingApi;
    }

    public JsonNode getWorkspace() {
        return workspace;
    }

    public void setWorkspace(JsonNode workspace) {
        this.workspace = workspace;
    }

    public List<JsonNode> getAllowedActions() {
        return allowedActions;
    }

    public void setAllowedActions(List<JsonNode> allowedActions) {
        this.allowedActions = allowedActions;
    }
}
