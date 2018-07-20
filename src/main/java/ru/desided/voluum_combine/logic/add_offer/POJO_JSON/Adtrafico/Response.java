package ru.desided.voluum_combine.logic.add_offer.POJO_JSON.Adtrafico;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Response {
    private WrapperMain response;

    public WrapperMain getResponse() {
        return response;
    }

    public void setResponse(WrapperMain response) {
        this.response = response;
    }
}
