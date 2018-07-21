package ru.desided.voluum_combine.logic.add_offer.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import ru.desided.voluum_combine.entity.Offer;
import ru.desided.voluum_combine.entity.TrafficSource;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Propeller {

    private static final String UA = "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:55.0) Gecko/20100101 Firefox/55.0";
    private Offer offer;
    private TrafficSource trafficSource;
    private HttpClient httpClient;

    public Propeller(Offer offer, TrafficSource trafficSource){
        this.offer = offer;
        this.trafficSource = trafficSource;
    }

    public void propellerAuth() throws IOException {

        List<Header> headers = Arrays.asList(
                new BasicHeader(HttpHeaders.USER_AGENT, UA),
                new BasicHeader(HttpHeaders.REFERER, "https://partners.propellerads.com/"),
                new BasicHeader(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"),
                new BasicHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate"),
                new BasicHeader(HttpHeaders.ACCEPT_LANGUAGE, "en-US,en;q=0.5"),
                new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json")

        );
        HttpClient propellerClient = HttpClientBuilder.create()
                .setDefaultHeaders(headers)
                .build();

        HttpPost httpPost = new HttpPost("https://partners.propellerads.com/v1.0/login_check");
        String body = "{\n" +
                "  \"username\": \"" + trafficSource.getLogin() + "\",\n" +
                "  \"password\": \"" + trafficSource.getPassword() + "\",\n" +
                "  \"gRecaptchaResponse\": null,\n" +
                "  \"type\": \"ROLE_ADVERTISER\",\n" +
                "  \"fingerprint\": \"6dabbc2b9ac8d06a961fd6efa56130bb\"\n" +
                "}";
        StringEntity entityBody = new StringEntity(body);
        httpPost.setEntity(entityBody);
        String responseBody = makeRequest(propellerClient, httpPost);
        JsonNode node = new ObjectMapper().readValue(responseBody, ObjectNode.class);
        String accessToken = node.get("result").get("accessToken").textValue();

        List<Header> authHeaders = new ArrayList<>(headers);
        authHeaders.add(new BasicHeader("Authorization", "Bearer " + accessToken));

        BasicCookieStore cookieStore = new BasicCookieStore();
        BasicClientCookie clientCookie = new BasicClientCookie("user", URLEncoder.encode(responseBody, "UTF-8"));
        clientCookie.setDomain("partners.propellerads.com");
        cookieStore.addCookie(clientCookie);

        httpClient = HttpClientBuilder.create()
                .setDefaultHeaders(authHeaders)
                .setDefaultCookieStore(cookieStore)
                .build();

    }

    public void propellerSmart() throws IOException {
        ZoneId zoneId = ZoneId.of("-05:00");
        ZonedDateTime time = ZonedDateTime.ofInstant(Instant.now(), zoneId);
        String time_now = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(time);

        String country_code = offer.getCountryCode().toLowerCase();
        String offer_name = String.format("%s - %s ($%s) %s %s", country_code.toUpperCase(), offer.getTypeTraffic(),
                offer.getPayoutConverted(), offer.getPostfix(), offer.getCampaignShort());
        HttpPost httpPost = new HttpPost("https://partners.propellerads.com/v1.0/advertiser/campaigns/?dateFrom=" + time_now + "&dateTill=" + time_now+ "&isArchived=0&orderBy=id&orderDest=desc&page=1&perPage=100&refresh=0");
        String body = null;

        if (offer.getTypeTraffic().contains("WEB")) {
            body = "{\"rateModel\":\"scpm\",\"direction\":\"onclick\",\"frequency\":3,\"capping\":86400,\"ipFrequency\":0,\"ipCapping\":0,\"status\":1,\"targeting\":{\"connection\":\"all\",\"proxy\":\"0\",\"country\":{\"isExcluded\":false,\"list\":[\"" + country_code + "\"]},\"osType\":{\"list\":[\"desktop\"],\"isExcluded\":false},\"os\":{\"list\":[\"mac\",\"windows\"],\"isExcluded\":false},\"osVersion\":{\"list\":[\"mac10.13\",\"mac10.12\",\"mac10.11\",\"win10\",\"win8\",\"win7\"],\"isExcluded\":false},\"deviceType\":{\"list\":[\"desktop\"],\"isExcluded\":false},\"device\":{\"list\":[\"desktop\"],\"isExcluded\":false},\"zone\":{\"list\":[],\"isExcluded\":false},\"mobileIsp\":{\"list\":[],\"isExcluded\":false},\"browser\":{\"list\":[\"safari\",\"chrome\",\"firefox\"],\"isExcluded\":false},\"userActivity\":{\"list\":[\"all\"],\"isExcluded\":false},\"language\":{\"list\":[],\"isExcluded\":false},\"city\":{\"list\":[],\"isExcluded\":false},\"timeTable\":{\"list\":[\"Mon00\",\"Mon01\",\"Mon02\",\"Mon03\",\"Mon04\",\"Mon05\",\"Mon06\",\"Mon07\",\"Mon08\",\"Mon09\",\"Mon10\",\"Mon11\",\"Mon12\",\"Mon13\",\"Mon14\",\"Mon15\",\"Mon16\",\"Mon17\",\"Mon18\",\"Mon19\",\"Mon20\",\"Mon21\",\"Mon22\",\"Mon23\",\"Tue00\",\"Tue01\",\"Tue02\",\"Tue03\",\"Tue04\",\"Tue05\",\"Tue06\",\"Tue07\",\"Tue08\",\"Tue09\",\"Tue10\",\"Tue11\",\"Tue12\",\"Tue13\",\"Tue14\",\"Tue15\",\"Tue16\",\"Tue17\",\"Tue18\",\"Tue19\",\"Tue20\",\"Tue21\",\"Tue22\",\"Tue23\",\"Wed00\",\"Wed01\",\"Wed02\",\"Wed03\",\"Wed04\",\"Wed05\",\"Wed06\",\"Wed07\",\"Wed08\",\"Wed09\",\"Wed10\",\"Wed11\",\"Wed12\",\"Wed13\",\"Wed14\",\"Wed15\",\"Wed16\",\"Wed17\",\"Wed18\",\"Wed19\",\"Wed20\",\"Wed21\",\"Wed22\",\"Wed23\",\"Thu00\",\"Thu01\",\"Thu02\",\"Thu03\",\"Thu04\",\"Thu05\",\"Thu06\",\"Thu07\",\"Thu08\",\"Thu09\",\"Thu10\",\"Thu11\",\"Thu12\",\"Thu13\",\"Thu14\",\"Thu15\",\"Thu16\",\"Thu17\",\"Thu18\",\"Thu19\",\"Thu20\",\"Thu21\",\"Thu22\",\"Thu23\",\"Fri00\",\"Fri01\",\"Fri02\",\"Fri03\",\"Fri04\",\"Fri05\",\"Fri06\",\"Fri07\",\"Fri08\",\"Fri09\",\"Fri10\",\"Fri11\",\"Fri12\",\"Fri13\",\"Fri14\",\"Fri15\",\"Fri16\",\"Fri17\",\"Fri18\",\"Fri19\",\"Fri20\",\"Fri21\",\"Fri22\",\"Fri23\",\"Sat00\",\"Sat01\",\"Sat02\",\"Sat03\",\"Sat04\",\"Sat05\",\"Sat06\",\"Sat07\",\"Sat08\",\"Sat09\",\"Sat10\",\"Sat11\",\"Sat12\",\"Sat13\",\"Sat14\",\"Sat15\",\"Sat16\",\"Sat17\",\"Sat18\",\"Sat19\",\"Sat20\",\"Sat21\",\"Sat22\",\"Sat23\",\"Sun00\",\"Sun01\",\"Sun02\",\"Sun03\",\"Sun04\",\"Sun05\",\"Sun06\",\"Sun07\",\"Sun08\",\"Sun09\",\"Sun10\",\"Sun11\",\"Sun12\",\"Sun13\",\"Sun14\",\"Sun15\",\"Sun16\",\"Sun17\",\"Sun18\",\"Sun19\",\"Sun20\",\"Sun21\",\"Sun22\",\"Sun23\"],\"isExcluded\":false}},\"evenlyLimitsUsage\":false,\"trafficQuality\":1,\"autoLinkNewZones\":false,\"linkNewZonesOnce\":false,\"isAdblockBuy\":true,\"startedAt\":\"" + time_now + "\",\"rates\":[{\"countries\":[\"" + country_code + "\"],\"amount\":\"" + offer.getOfferCost() + "\"}],\"name\":\"" + offer_name + "\",\"cpaGoalStatus\":false,\"cpaGoalBid\":null,\"targetUrl\":\"" + offer.getCampaignLink() + "\",\"totalAmount\":null}";
        } else {
            body = "{\"rateModel\":\"scpm\",\"direction\":\"onclick\",\"frequency\":3,\"capping\":86400,\"ipFrequency\":0,\"ipCapping\":0,\"status\":1,\"targeting\":{\"connection\":\"all\",\"proxy\":\"0\",\"country\":{\"isExcluded\":false,\"list\":[\"" + country_code + "\"]},\"osType\":{\"list\":[\"mobile\"],\"isExcluded\":false},\"os\":{\"list\":[\"android\",\"ios\"],\"isExcluded\":false},\"osVersion\":{\"list\":[\"android8\",\"android7\",\"android6\",\"android5\",\"ios11\",\"ios10\",\"ios9\"],\"isExcluded\":false},\"deviceType\":{\"list\":[\"phone\"],\"isExcluded\":false},\"device\":{\"list\":[],\"isExcluded\":false},\"zone\":{\"list\":[],\"isExcluded\":false},\"mobileIsp\":{\"list\":[],\"isExcluded\":false},\"browser\":{\"list\":[\"safari\",\"chrome\"],\"isExcluded\":false},\"language\":{\"list\":[],\"isExcluded\":false},\"userActivity\":{\"list\":[\"all\"],\"isExcluded\":false},\"city\":{\"list\":[],\"isExcluded\":false},\"timeTable\":{\"list\":[\"Mon00\",\"Mon01\",\"Mon02\",\"Mon03\",\"Mon04\",\"Mon05\",\"Mon06\",\"Mon07\",\"Mon08\",\"Mon09\",\"Mon10\",\"Mon11\",\"Mon12\",\"Mon13\",\"Mon14\",\"Mon15\",\"Mon16\",\"Mon17\",\"Mon18\",\"Mon19\",\"Mon20\",\"Mon21\",\"Mon22\",\"Mon23\",\"Tue00\",\"Tue01\",\"Tue02\",\"Tue03\",\"Tue04\",\"Tue05\",\"Tue06\",\"Tue07\",\"Tue08\",\"Tue09\",\"Tue10\",\"Tue11\",\"Tue12\",\"Tue13\",\"Tue14\",\"Tue15\",\"Tue16\",\"Tue17\",\"Tue18\",\"Tue19\",\"Tue20\",\"Tue21\",\"Tue22\",\"Tue23\",\"Wed00\",\"Wed01\",\"Wed02\",\"Wed03\",\"Wed04\",\"Wed05\",\"Wed06\",\"Wed07\",\"Wed08\",\"Wed09\",\"Wed10\",\"Wed11\",\"Wed12\",\"Wed13\",\"Wed14\",\"Wed15\",\"Wed16\",\"Wed17\",\"Wed18\",\"Wed19\",\"Wed20\",\"Wed21\",\"Wed22\",\"Wed23\",\"Thu00\",\"Thu01\",\"Thu02\",\"Thu03\",\"Thu04\",\"Thu05\",\"Thu06\",\"Thu07\",\"Thu08\",\"Thu09\",\"Thu10\",\"Thu11\",\"Thu12\",\"Thu13\",\"Thu14\",\"Thu15\",\"Thu16\",\"Thu17\",\"Thu18\",\"Thu19\",\"Thu20\",\"Thu21\",\"Thu22\",\"Thu23\",\"Fri00\",\"Fri01\",\"Fri02\",\"Fri03\",\"Fri04\",\"Fri05\",\"Fri06\",\"Fri07\",\"Fri08\",\"Fri09\",\"Fri10\",\"Fri11\",\"Fri12\",\"Fri13\",\"Fri14\",\"Fri15\",\"Fri16\",\"Fri17\",\"Fri18\",\"Fri19\",\"Fri20\",\"Fri21\",\"Fri22\",\"Fri23\",\"Sat00\",\"Sat01\",\"Sat02\",\"Sat03\",\"Sat04\",\"Sat05\",\"Sat06\",\"Sat07\",\"Sat08\",\"Sat09\",\"Sat10\",\"Sat11\",\"Sat12\",\"Sat13\",\"Sat14\",\"Sat15\",\"Sat16\",\"Sat17\",\"Sat18\",\"Sat19\",\"Sat20\",\"Sat21\",\"Sat22\",\"Sat23\",\"Sun00\",\"Sun01\",\"Sun02\",\"Sun03\",\"Sun04\",\"Sun05\",\"Sun06\",\"Sun07\",\"Sun08\",\"Sun09\",\"Sun10\",\"Sun11\",\"Sun12\",\"Sun13\",\"Sun14\",\"Sun15\",\"Sun16\",\"Sun17\",\"Sun18\",\"Sun19\",\"Sun20\",\"Sun21\",\"Sun22\",\"Sun23\"],\"isExcluded\":false}},\"evenlyLimitsUsage\":false,\"trafficQuality\":1,\"autoLinkNewZones\":false,\"linkNewZonesOnce\":false,\"isAdblockBuy\":true,\"startedAt\":\"" + time_now + "\",\"rates\":[{\"countries\":[\"" + country_code + "\"],\"amount\":\"" + offer.getOfferCost() + "\"}],\"name\":\"" + offer_name + "\",\"cpaGoalStatus\":false,\"cpaGoalBid\":null,\"targetUrl\":\"" + offer.getCampaignLink() + "\",\"totalAmount\":null}";
        }
        StringEntity entityBody = new StringEntity(body);
        httpPost.setEntity(entityBody);
        makeRequest(httpClient, httpPost);
    }

    public void propellerHigh() throws IOException {
        ZoneId zoneId = ZoneId.of("-05:00");
        ZonedDateTime time = ZonedDateTime.ofInstant(Instant.now(), zoneId);
        String time_now = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(time);

        String country_code = offer.getCountryCode().toLowerCase();
        String offer_name = String.format("%s - %s ($%s) HIGH %s %s", country_code.toUpperCase(), offer.getTypeTraffic(),
                offer.getPayoutConverted(), offer.getPostfix(), offer.getCampaignShort());
        HttpPost httpPost = new HttpPost("https://partners.propellerads.com/v1.0/advertiser/campaigns/?dateFrom=" + time_now + "&dateTill=" + time_now+ "&isArchived=0&orderBy=id&orderDest=desc&page=1&perPage=100&refresh=0");
        String body = null;

        if (offer.getTypeTraffic().contains("WEB")) {
            body = "{\"rateModel\":\"cpm\",\"direction\":\"onclick\",\"frequency\":1,\"capping\":86400,\"ipFrequency\":0,\"ipCapping\":0,\"status\":1,\"targeting\":{\"connection\":\"all\",\"proxy\":\"0\",\"browser\":{\"list\":[\"safari\",\"chrome\",\"firefox\"],\"isExcluded\":false},\"city\":{\"list\":[],\"isExcluded\":false},\"country\":{\"isExcluded\":false,\"list\":[\"" + country_code +"\"]},\"device\":{\"list\":[],\"isExcluded\":false},\"deviceType\":{\"list\":[\"desktop\"],\"isExcluded\":false},\"iab\":{\"list\":[],\"isExcluded\":false},\"language\":{\"list\":[],\"isExcluded\":false},\"mobileIsp\":{\"list\":[],\"isExcluded\":false},\"os\":{\"list\":[\"windows\",\"mac\"],\"isExcluded\":false},\"osType\":{\"list\":[\"desktop\"],\"isExcluded\":false},\"osVersion\":{\"list\":[\"mac10.10\",\"mac10.11\",\"mac10.12\",\"mac10.13\",\"mac10.14\",\"win10\",\"win7\",\"win8\"],\"isExcluded\":false},\"userActivity\":{\"list\":[\"1\"],\"isExcluded\":false},\"zone\":{\"list\":[],\"isExcluded\":false},\"timeTable\":{\"list\":[\"Mon00\",\"Mon01\",\"Mon02\",\"Mon03\",\"Mon04\",\"Mon05\",\"Mon06\",\"Mon07\",\"Mon08\",\"Mon09\",\"Mon10\",\"Mon11\",\"Mon12\",\"Mon13\",\"Mon14\",\"Mon15\",\"Mon16\",\"Mon17\",\"Mon18\",\"Mon19\",\"Mon20\",\"Mon21\",\"Mon22\",\"Mon23\",\"Tue00\",\"Tue01\",\"Tue02\",\"Tue03\",\"Tue04\",\"Tue05\",\"Tue06\",\"Tue07\",\"Tue08\",\"Tue09\",\"Tue10\",\"Tue11\",\"Tue12\",\"Tue13\",\"Tue14\",\"Tue15\",\"Tue16\",\"Tue17\",\"Tue18\",\"Tue19\",\"Tue20\",\"Tue21\",\"Tue22\",\"Tue23\",\"Wed00\",\"Wed01\",\"Wed02\",\"Wed03\",\"Wed04\",\"Wed05\",\"Wed06\",\"Wed07\",\"Wed08\",\"Wed09\",\"Wed10\",\"Wed11\",\"Wed12\",\"Wed13\",\"Wed14\",\"Wed15\",\"Wed16\",\"Wed17\",\"Wed18\",\"Wed19\",\"Wed20\",\"Wed21\",\"Wed22\",\"Wed23\",\"Thu00\",\"Thu01\",\"Thu02\",\"Thu03\",\"Thu04\",\"Thu05\",\"Thu06\",\"Thu07\",\"Thu08\",\"Thu09\",\"Thu10\",\"Thu11\",\"Thu12\",\"Thu13\",\"Thu14\",\"Thu15\",\"Thu16\",\"Thu17\",\"Thu18\",\"Thu19\",\"Thu20\",\"Thu21\",\"Thu22\",\"Thu23\",\"Fri00\",\"Fri01\",\"Fri02\",\"Fri03\",\"Fri04\",\"Fri05\",\"Fri06\",\"Fri07\",\"Fri08\",\"Fri09\",\"Fri10\",\"Fri11\",\"Fri12\",\"Fri13\",\"Fri14\",\"Fri15\",\"Fri16\",\"Fri17\",\"Fri18\",\"Fri19\",\"Fri20\",\"Fri21\",\"Fri22\",\"Fri23\",\"Sat00\",\"Sat01\",\"Sat02\",\"Sat03\",\"Sat04\",\"Sat05\",\"Sat06\",\"Sat07\",\"Sat08\",\"Sat09\",\"Sat10\",\"Sat11\",\"Sat12\",\"Sat13\",\"Sat14\",\"Sat15\",\"Sat16\",\"Sat17\",\"Sat18\",\"Sat19\",\"Sat20\",\"Sat21\",\"Sat22\",\"Sat23\",\"Sun00\",\"Sun01\",\"Sun02\",\"Sun03\",\"Sun04\",\"Sun05\",\"Sun06\",\"Sun07\",\"Sun08\",\"Sun09\",\"Sun10\",\"Sun11\",\"Sun12\",\"Sun13\",\"Sun14\",\"Sun15\",\"Sun16\",\"Sun17\",\"Sun18\",\"Sun19\",\"Sun20\",\"Sun21\",\"Sun22\",\"Sun23\"],\"isExcluded\":false}},\"evenlyLimitsUsage\":false,\"trafficQuality\":1,\"autoLinkNewZones\":false,\"linkNewZonesOnce\":false,\"isAdblockBuy\":true,\"trafficBoost\":true,\"startedAt\":\"" + time_now + "\",\"rates\":[{\"countries\":[\"" + country_code +"\"],\"amount\":\"" + offer.getOfferCost() + "\"}],\"name\":\"" + offer_name + "\",\"targetUrl\":\"" + offer.getCampaignLink() + "\"}";
        } else {
            body = "{\"rateModel\":\"cpm\",\"direction\":\"onclick\",\"frequency\":1,\"capping\":86400,\"ipFrequency\":0,\"ipCapping\":0,\"status\":1,\"targeting\":{\"connection\":\"all\",\"proxy\":\"0\",\"browser\":{\"list\":[\"native\",\"safari\",\"chrome\"],\"isExcluded\":false},\"city\":{\"list\":[],\"isExcluded\":false},\"country\":{\"isExcluded\":false,\"list\":[\"" + country_code + "\"]},\"device\":{\"list\":[],\"isExcluded\":false},\"deviceType\":{\"list\":[\"phone\"],\"isExcluded\":false},\"iab\":{\"list\":[],\"isExcluded\":false},\"language\":{\"list\":[],\"isExcluded\":false},\"mobileIsp\":{\"list\":[],\"isExcluded\":false},\"os\":{\"list\":[\"android\",\"ios\"],\"isExcluded\":false},\"osType\":{\"list\":[\"mobile\"],\"isExcluded\":false},\"osVersion\":{\"list\":[\"android5\",\"android6\",\"android7\",\"android8\",\"android9\",\"ios10\",\"ios11\",\"ios12\",\"ios9\"],\"isExcluded\":false},\"userActivity\":{\"list\":[\"1\"],\"isExcluded\":false},\"zone\":{\"list\":[],\"isExcluded\":false},\"timeTable\":{\"list\":[\"Mon00\",\"Mon01\",\"Mon02\",\"Mon03\",\"Mon04\",\"Mon05\",\"Mon06\",\"Mon07\",\"Mon08\",\"Mon09\",\"Mon10\",\"Mon11\",\"Mon12\",\"Mon13\",\"Mon14\",\"Mon15\",\"Mon16\",\"Mon17\",\"Mon18\",\"Mon19\",\"Mon20\",\"Mon21\",\"Mon22\",\"Mon23\",\"Tue00\",\"Tue01\",\"Tue02\",\"Tue03\",\"Tue04\",\"Tue05\",\"Tue06\",\"Tue07\",\"Tue08\",\"Tue09\",\"Tue10\",\"Tue11\",\"Tue12\",\"Tue13\",\"Tue14\",\"Tue15\",\"Tue16\",\"Tue17\",\"Tue18\",\"Tue19\",\"Tue20\",\"Tue21\",\"Tue22\",\"Tue23\",\"Wed00\",\"Wed01\",\"Wed02\",\"Wed03\",\"Wed04\",\"Wed05\",\"Wed06\",\"Wed07\",\"Wed08\",\"Wed09\",\"Wed10\",\"Wed11\",\"Wed12\",\"Wed13\",\"Wed14\",\"Wed15\",\"Wed16\",\"Wed17\",\"Wed18\",\"Wed19\",\"Wed20\",\"Wed21\",\"Wed22\",\"Wed23\",\"Thu00\",\"Thu01\",\"Thu02\",\"Thu03\",\"Thu04\",\"Thu05\",\"Thu06\",\"Thu07\",\"Thu08\",\"Thu09\",\"Thu10\",\"Thu11\",\"Thu12\",\"Thu13\",\"Thu14\",\"Thu15\",\"Thu16\",\"Thu17\",\"Thu18\",\"Thu19\",\"Thu20\",\"Thu21\",\"Thu22\",\"Thu23\",\"Fri00\",\"Fri01\",\"Fri02\",\"Fri03\",\"Fri04\",\"Fri05\",\"Fri06\",\"Fri07\",\"Fri08\",\"Fri09\",\"Fri10\",\"Fri11\",\"Fri12\",\"Fri13\",\"Fri14\",\"Fri15\",\"Fri16\",\"Fri17\",\"Fri18\",\"Fri19\",\"Fri20\",\"Fri21\",\"Fri22\",\"Fri23\",\"Sat00\",\"Sat01\",\"Sat02\",\"Sat03\",\"Sat04\",\"Sat05\",\"Sat06\",\"Sat07\",\"Sat08\",\"Sat09\",\"Sat10\",\"Sat11\",\"Sat12\",\"Sat13\",\"Sat14\",\"Sat15\",\"Sat16\",\"Sat17\",\"Sat18\",\"Sat19\",\"Sat20\",\"Sat21\",\"Sat22\",\"Sat23\",\"Sun00\",\"Sun01\",\"Sun02\",\"Sun03\",\"Sun04\",\"Sun05\",\"Sun06\",\"Sun07\",\"Sun08\",\"Sun09\",\"Sun10\",\"Sun11\",\"Sun12\",\"Sun13\",\"Sun14\",\"Sun15\",\"Sun16\",\"Sun17\",\"Sun18\",\"Sun19\",\"Sun20\",\"Sun21\",\"Sun22\",\"Sun23\"],\"isExcluded\":false}},\"evenlyLimitsUsage\":false,\"trafficQuality\":1,\"autoLinkNewZones\":false,\"linkNewZonesOnce\":false,\"isAdblockBuy\":true,\"trafficBoost\":true,\"startedAt\":\"" + time_now+ "\",\"rates\":[{\"countries\":[\"" + country_code + "\"],\"amount\":\"" + offer.getOfferCost() + "\"}],\"targetUrl\":\"" + offer.getCampaignLink() + "\",\"name\":\"" + offer_name + "\"}";
        }
        StringEntity entityBody = new StringEntity(body);
        httpPost.setEntity(entityBody);
        makeRequest(httpClient, httpPost);
    }

    private static String makeRequest(HttpClient httpClient, HttpRequestBase http) throws IOException {
        ResponseHandler<String> responseHandler = response -> {
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {

                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            } else {
                return EntityUtils.toString(response.getEntity());
            }
        };

        String responseBody = httpClient.execute(http, responseHandler);
        return responseBody;
    }
}
