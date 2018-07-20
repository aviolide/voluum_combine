package ru.desided.voluum_combine.logic.add_offer.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import ru.desided.voluum_combine.LogHandler.CustomAppender;
import ru.desided.voluum_combine.controllers.OfferController;
import ru.desided.voluum_combine.entity.*;
import ru.desided.voluum_combine.logic.add_offer.AddOffers1;
import ru.desided.voluum_combine.logic.add_offer.POJO_JSON.ClickDealer.Campaigns;
import ru.desided.voluum_combine.logic.add_offer.POJO_JSON.ClickDealer.CommonObjectCD;
import ru.desided.voluum_combine.logic.add_offer.POJO_JSON.ClickDealer.Creative;
import ru.desided.voluum_combine.logic.add_offer.POJO_JSON.CommonObjectList;
import ru.desided.voluum_combine.service.CountryService;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ClickDealer implements AddOffers1 {

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
    static Logger log = Logger.getLogger(ClickDealer.class.getName());

    public ClickDealer(AffiliateNetwork affiliateNetwork, TrafficSource trafficSource,
                       Offer offer, CountryService countryService, User user, Cloak cloak) throws IOException {

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
    public void setup() {
        ZoneId zoneId = ZoneId.of("-05:00");
        ZonedDateTime time = ZonedDateTime.ofInstant(Instant.now(), zoneId);
        String time_now = DateTimeFormatter.ofPattern("yyyy-M-dd").format(time);
        System.out.println(time_now);

        List<Header> headers = Arrays.asList(
                new BasicHeader(HttpHeaders.USER_AGENT, UA),
                new BasicHeader(HttpHeaders.ACCEPT, "application/json"),
                new BasicHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate"),
                new BasicHeader(HttpHeaders.ACCEPT_LANGUAGE, "en-US,en;q=0.5"),
                new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json"));

        HttpHost proxy = new HttpHost(affiliateNetwork.getProxyHost(), affiliateNetwork.getProxyPort());
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(affiliateNetwork.getProxyHost(), affiliateNetwork.getProxyPort()),
                new UsernamePasswordCredentials(affiliateNetwork.getProxyUser(), affiliateNetwork.getProxyPassword()));
        httpClient = HttpClientBuilder.create()
                .setDefaultCredentialsProvider(credsProvider)
                .setProxy(proxy)
                .setDefaultHeaders(headers)
                .build();
    }

    @Override
    public void login() {

    }


    @Override
    public Offer addOffer() throws IOException {
        HttpGet get = new HttpGet("https://partners.clickdealer.com/affiliates/api/1/offers.asmx/OfferFeed?api_key=" + API_KEY + "&affiliate_id=" + AFFILIATE_ID +"&campaign_name=" + OFFER_ID);
        String out = makeRequest(httpClient, get);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);
        CommonObjectList offers = objectMapper.readValue(out, CommonObjectList.class); //exception catch if offer not available


        if (offers.getOffers() != null) {
            for (Map.Entry<String, CommonObjectCD> entry : offers.getOffers().entrySet()) {
                CommonObjectCD commonOffer = entry.getValue();

                offer.setStatus(commonOffer.getOfferStatus().get("status_name"));
                offer.setCountryName(commonOffer.getAllowedCountries().get(0).getCountry_name());
                offer.setCountryCode(commonOffer.getAllowedCountries().get(0).getCountry_code());


                if (commonOffer.getOfferStatus().containsValue("2")) {
                    get = new HttpGet("https://partners.clickdealer.com/affiliates/api/1/offers.asmx/ApplyForOffer?api_key=" + API_KEY + "&affiliate_id=" + AFFILIATE_ID +"&" +
                            "offer_id=" + commonOffer.getOfferId() + "&offer_contract_id=" + commonOffer.getOfferContractId() + "&media_type_id=7&notes=banners&agreed_to_terms=TRUE");
                    out = makeRequest(httpClient, get);
                    log.warn("offer apply" + commonOffer.getOfferStatus());
                    offer.setOfferId(commonOffer.getOfferId());
                    offer.setStatus("Pending");
                    offer.setAffiliateNetwork(affiliateNetwork);
                }
                if (commonOffer.getOfferStatus().containsValue("3")) {

                    log.warn("active" + commonOffer.getOfferStatus());
                    offer.setOfferId(commonOffer.getOfferId());
                    offer.setStatus("Active");
                    offer.setAffiliateNetwork(affiliateNetwork);
                }

            }
        }
        return offer;
    }

    @Override
    public Offer getCreo() throws IOException {

        HttpGet get = new HttpGet("https://partners.clickdealer.com/affiliates/api/1/offers.asmx/GetCampaign?api_key=" + API_KEY + "&affiliate_id=" + AFFILIATE_ID +"&offer_id=" + OFFER_ID);
        String out = makeRequest(httpClient, get);
        Campaigns campaigns = new ObjectMapper().readValue(out, Campaigns.class);

        for (Map.Entry<String, CommonObjectCD> entryInner : campaigns.get().entrySet()) {
            CommonObjectCD comonOffer = entryInner.getValue();
            for (int countCreo = 0; countCreo < comonOffer.getCreatives().size(); countCreo++) {
                Creative creative = comonOffer.getCreatives().get(countCreo);
                log.info("creo " + creative.getCreative_id());

                if (!CREO_ID.equals("") && creative.getCreative_id().contains(CREO_ID)) {
                    comonOffer.setLink(comonOffer.getCreatives().get(countCreo).getUnique_link().replace("\\/", "/"));
                    comonOffer.setLink(comonOffer.getLink() + "&s1={os}&s3={osversion}&s2=");
                    break;
                } else {
                    comonOffer.setLink(comonOffer.getCreatives().get(0).getUnique_link().replace("\\/", "/"));
                    comonOffer.setLink(comonOffer.getLink() + "&s1={os}&s3={osversion}&s2=");
                }
            }
            System.out.println(CREO_ID + " " + comonOffer.getLink());
            offer.setLink(comonOffer.getLink());
            offer.setStatus("Active");
            offer.setPayoutConverted(comonOffer.getPayout());
            offer.setName(comonOffer.getOfferName());
            if (offer.getName().toLowerCase().contains("web") || offer.getName().toLowerCase().contains("responsive")){
                offer.setTypeTraffic("WEB");
            } else {
                offer.setTypeTraffic("MOB");
            }
            out = String.format("offer %s complete. link - %s, compaign id - %s, name - %s",
                    OFFER_ID, comonOffer.getLink(), comonOffer.getCampaignId(), comonOffer.getOfferName());
            log.info(out);
        }
        return offer;
    }

    @Override
    public void addVoluum() throws IOException {
        Voluum voluum = new Voluum(true, offer, affiliateNetwork, trafficSource, user, cloak, countryService);
        voluum.voluumAuth();
        voluum.setupVoluum();
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
        String responseBody = responseBody = httpClient.execute(httpPost, responseHandler);

        if (responseBody == null) {
            makeRequest(httpClient, httpPost);
        }
        return responseBody;
    }


    public void addAllDatingOffers() {
//        HttpGet get = new HttpGet("https://partners.clickdealer.com/affiliates/api/1/offers.asmx/OfferFeed?api_key=" + API_KEY + "&affiliate_id=" + AFFILIATE_ID +"&all" +
//                "owed_countries=au,ca,de,it,no,uk,nz,pl,es,gb,us,dk,cz,fi,be,at,bg,gr,pt,ch,sw&vertical_id=50,53&offer_status_id=1,2,3&&platform=1,2,3,4&flow=5,6&adult=1");
//        String out = makeRequest(httpClient, get);
//        ObjectMapper objectMapper = new ObjectMapper();
//        Offers offers = objectMapper.readValue(out, Offers.class);
//        String[] stringBuffer = new String[offers.getOffers().size()];
//        int i = 0;
//
//        for (Map.Entry<String, Offer> entry : offers.getOffers().entrySet()){
//            Offer offer = entry.getValue();
//            System.out.println(offer.getOffer_id());
//            System.out.println(offer.getAllowed_countries().get(0));
//            System.out.println(offer.getOffer_name());
//            System.out.println(offer.getPayout_converted());
//            stringBuffer[i] = offer.getOffer_id();
//            System.out.println(offer.getOffer_id());
//            i++;
//
//            if (i % 10 == 0){
//                Thread.sleep(10000);
//            }
//
//            if (offer.getOffer_status().containsValue("2")){
//                get = new HttpGet("https://partners.clickdealer.com/affiliates/api/1/offers.asmx/ApplyForOffer?api_key=" + API_KEY + "&affiliate_id=" + AFFILIATE_ID +"&" +
//                        "offer_id=" + offer.getOffer_id() + "&offer_contract_id=" + offer.getOffer_contract_id() + "&media_type_id=7&notes=banners&agreed_to_terms=TRUE");
//                makeRequest(httpClient, get);
//            }
//        }
//
//        String join = String.join(",", Arrays.asList(stringBuffer));
//        System.out.println(join);
//
//        get = new HttpGet("https://partners.clickdealer.com/affiliates/api/1/offers.asmx/GetCampaign?api_key=" + API_KEY + "&affiliate_id=" + AFFILIATE_ID +"&offer_id=" + join);
//        out = makeRequest(httpClient, get);
//
//        int ind = 0;
//        Campaigns campaigns = objectMapper.readValue(out, Campaigns.class);
//
//        for (Map.Entry<String, Offer> entry : campaigns.get().entrySet()){
//            ind++;
//            Offer offer = entry.getValue();
//            offerList.add(offer);
//            offer.setLink(offer.getCreatives().get(0).getUnique_link().replace("\\/", "/"));
//            offer.setLink(offer.getLink()  + "&s1={os}&s3={osversion}&s2=");
//            System.out.println(offer.getLink());
//
//            if (ind > 22) {
//    //                new Voluum(true, offer, ind, );
//            }
//        }
    }
}
