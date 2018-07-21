package ru.desided.voluum_combine.logic.add_offer.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import ru.desided.voluum_combine.LogHandler.CustomAppender;
import ru.desided.voluum_combine.entity.*;
import ru.desided.voluum_combine.logic.add_offer.POJO_JSON.Adtrafico.Data;
import ru.desided.voluum_combine.logic.add_offer.POJO_JSON.Adtrafico.OfferAdtrafico;
import ru.desided.voluum_combine.logic.add_offer.POJO_JSON.Adtrafico.Response;
import ru.desided.voluum_combine.service.CountryService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Adtrafico implements AddOffersLogic {

    private static HttpClient httpClient;
    private static String UA = "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:55.0) Gecko/20100101 Firefox/55.0";
    private AffiliateNetwork affiliateNetwork;
    private Offer offer;
    private TrafficSource trafficSource;
    private CountryService countryService;
    private User user;
    private Cloak cloak;
    private String TRAFFIC_TYPE;
    private String AFFILIATE_LOGIN;
    private String AFFILIATE_PASSWORD;
    private String AFFILIATE_NETWORK_NAME;
    private String OFFER_ID;
    private String CREO_ID;
    private String API_KEY;
    private String AFFILIATE_ID;
    static Logger log = Logger.getLogger(Adtrafico.class.getName());

    public Adtrafico(AffiliateNetwork affiliateNetwork, TrafficSource trafficSource,
                     Offer offer, CountryService countryService, User user, Cloak cloak){
        this.affiliateNetwork = affiliateNetwork;
        this.offer = offer;
        this.trafficSource = trafficSource;
        this.countryService = countryService;
        this.user = user;
        this.cloak = cloak;
        AFFILIATE_NETWORK_NAME = affiliateNetwork.getNameVoluum();
        AFFILIATE_LOGIN = affiliateNetwork.getLogin();
        AFFILIATE_PASSWORD = affiliateNetwork.getPassword();
        OFFER_ID = offer.getOfferId();
        CREO_ID = offer.getLandId();
        API_KEY = affiliateNetwork.getApiKey();
        AFFILIATE_ID = affiliateNetwork.getAffiliateId();
        CustomAppender customAppender = new CustomAppender();
        log.addAppender(customAppender);
    }

    @Override
    public void login() {

        List<Header> headers = Arrays.asList(
                new BasicHeader(HttpHeaders.USER_AGENT, UA),
                new BasicHeader(HttpHeaders.ACCEPT, "application/json"),
                new BasicHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate"),
                new BasicHeader(HttpHeaders.ACCEPT_LANGUAGE, "en-US,en;q=0.5"),
                new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json"));
        httpClient = HttpClientBuilder.create()
                .setDefaultHeaders(headers)
                .build();
    }

    @Override
    public Offer addOffer() throws IOException {
        HttpGet get = new HttpGet("https://" + AFFILIATE_ID + ".api.hasoffers.com/Apiv3/json?api_key=" + API_KEY + "&Target=Affiliate_Offer&Method=findById&id=" + OFFER_ID);
        String out = makeRequest(httpClient, get);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);
        OfferAdtrafico offerAdtrafico = objectMapper.readValue(out, Response.class).getResponse().getData().getOffer();

        if (offerAdtrafico.getName().toLowerCase().contains("web") || offerAdtrafico.getName().toLowerCase().contains("responsive")){
            offerAdtrafico.setType_traffic("WEB");
        } else {
            offerAdtrafico.setType_traffic("MOB");
        }

        if (offerAdtrafico.getApproval_status().equalsIgnoreCase("rejected")){
            offerAdtrafico.setStatus("rejected");
        }
        GenericOffer<OfferAdtrafico> genericOffer = new GenericOffer<>();
        genericOffer.compare(offer, offerAdtrafico);
        log.info(String.format("offer %s, name %s, status %s", offerAdtrafico.getOfferId(), offerAdtrafico.getName(), offerAdtrafico.getStatus()));
        return offer;
    }

    @Override
    public Offer getCreo() throws IOException {
        HttpGet get = new HttpGet("https://" + AFFILIATE_ID + ".api.hasoffers.com/Apiv3/json?api_key=" + API_KEY + "&Target=Affiliate_Offer&Method=getGeoTargeting&id=" + OFFER_ID);
        String out = makeRequest(httpClient, get);

        OfferAdtrafico offerAdtrafico = new OfferAdtrafico();
        ObjectMapper objectMapper = new ObjectMapper();
        Data data = objectMapper.readValue(out, Response.class).getResponse().getData();
        Map.Entry<String, JsonNode> map = data.getCountries().entrySet().iterator().next();
        offerAdtrafico.setCountry_code(map.getValue().get("code").textValue());
        offerAdtrafico.setCountry_name(map.getValue().get("name").textValue());

        get = new HttpGet("https://" + AFFILIATE_ID + ".api.hasoffers.com/Apiv3/json?api_key=" + API_KEY + "&Target=Affiliate_Offer&Method=generateTrackingLink&offer_id=" + OFFER_ID);
        out = makeRequest(httpClient, get);
        data = objectMapper.readValue(out, Response.class).getResponse().getData();
        offerAdtrafico.setLink(data.getClick_url() + "&aff_click_id=");

        GenericOffer<OfferAdtrafico> genericOffer = new GenericOffer<>();
        genericOffer.compare(offer, offerAdtrafico);
        log.info(String.format("link %s", offerAdtrafico.getLink()));
        return offer;
    }

    @Override
    public void addVoluum() throws IOException {
        Voluum voluum = new Voluum(true, offer, affiliateNetwork, trafficSource, user, cloak, countryService);
        voluum.voluumAuth();
        voluum.setupVoluum();
    }

    @Override
    public void propellerSmart() throws IOException {
        Propeller propeller = new Propeller(offer, trafficSource);
        propeller.propellerAuth();
        propeller.propellerSmart();
    }

    @Override
    public void propellerHigh() throws IOException {
        Propeller propeller = new Propeller(offer, trafficSource);
        propeller.propellerAuth();
        propeller.propellerHigh();
    }

    @Override
    public Offer setProp() {
        return null;
    }

    private static String makeRequest(HttpClient httpClient, HttpRequestBase httpPost) throws IOException {
        ResponseHandler<String> responseHandler = response -> {
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            } else {
                log.info("error");
                return makeRequest(httpClient, httpPost);
            }
        };
        String responseBody = httpClient.execute(httpPost, responseHandler);

        if (responseBody == null) {
            makeRequest(httpClient, httpPost);
        }
        return responseBody;
    }
}
