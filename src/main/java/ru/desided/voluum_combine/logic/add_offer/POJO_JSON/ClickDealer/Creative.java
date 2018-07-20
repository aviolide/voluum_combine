package ru.desided.voluum_combine.logic.add_offer.POJO_JSON.ClickDealer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Creative {
    private String creative_id;
    private String id;
    private String unique_link;

    public String getUnique_link() {
        return unique_link;
    }

    public void setUnique_link(String unique_link) {
        this.unique_link = unique_link;
    }

    public String getCreative_id() {
        return creative_id;
    }

    public void setCreative_id(String creative_id) {
        this.creative_id = creative_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
