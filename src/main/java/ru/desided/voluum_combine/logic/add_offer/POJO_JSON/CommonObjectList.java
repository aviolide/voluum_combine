package ru.desided.voluum_combine.logic.add_offer.POJO_JSON;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.desided.voluum_combine.logic.add_offer.POJO_JSON.Advidi.CommonObject;
import ru.desided.voluum_combine.logic.add_offer.POJO_JSON.ClickDealer.CommonObjectCD;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonObjectList {

    @JsonProperty
    private List<CommonObject> rows;

    public List<CommonObject> getRows() {
        return rows;
    }

    public void setRows(List<CommonObject> rows) {
        this.rows = rows;
    }


    @JsonProperty
    private Map<String, CommonObjectCD> offers;

    public Map<String, CommonObjectCD> getOffers() {
        return offers;
    }

    public void setOffers(Map<String, CommonObjectCD> offers) {
        this.offers = offers;
    }
}
