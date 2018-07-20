package ru.desided.voluum_combine.logic.add_offer.POJO_JSON.voluum_serealize;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.desided.voluum_combine.logic.add_offer.POJO_JSON.Advidi.CommonObject;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonObjectListVoluum {
    @JsonProperty
    private List<CommonObjectVoluum> campaigns;

    public List<CommonObjectVoluum> getCampaigns() {
        return campaigns;
    }

    public void setCampaigns(List<CommonObjectVoluum> campaigns) {
        this.campaigns = campaigns;
    }
}
