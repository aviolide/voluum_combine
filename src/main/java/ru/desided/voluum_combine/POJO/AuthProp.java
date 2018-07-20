package ru.desided.voluum_combine.POJO;

import org.apache.http.client.HttpClient;

public class AuthProp {

    private String DATE;
    private HttpClient httpClient;
    private Login login;

    public String getDATE() {
        return DATE;
    }

    public void setDATE(String DATE) {
        this.DATE = DATE;
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }
}
