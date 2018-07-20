package ru.desided.voluum_combine.logic.add_offer.POJO_JSON.ClickDealer;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(value = { "row_count", "success" })
public class Campaigns {

    private Map<String, CommonObjectCD> properties = new HashMap<>();

    public Map<String, CommonObjectCD> get() {
        return properties;
    }
    @JsonAnySetter
    public void set(String fieldName, CommonObjectCD value) {
        System.out.println(fieldName);
        this.properties.put(fieldName, value);
    }
}
