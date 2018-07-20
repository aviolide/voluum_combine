package ru.desided.voluum_combine.logic.add_offer.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import ru.desided.voluum_combine.LogHandler.CustomAppender;
import ru.desided.voluum_combine.controllers.OfferController;
import ru.desided.voluum_combine.entity.*;
import ru.desided.voluum_combine.logic.add_offer.POJO_JSON.Advidi.CommonObject;
import ru.desided.voluum_combine.logic.add_offer.POJO_JSON.CommonObjectList;
import ru.desided.voluum_combine.logic.add_offer.POJO_JSON.voluum_serealize.*;
import ru.desided.voluum_combine.service.CountryService;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.math.BigDecimal.ROUND_HALF_UP;

public class Voluum {

    private static Boolean flowExist;
    private static final String UA = "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:55.0) Gecko/20100101 Firefox/55.0";
    private static String TOKEN;
    private static String DATE;
    private HttpClient httpClient;
    private Offer offer;
    private User user;

    private static String OFFER_ID;
    private static String VOLUUM_ACCESS_ID;
    private static String VOLUUM_ACCESS_KEY;
    private static String CLIENT_ID;
    private static String VOLUUM_EMAIL;
    private static String VOLUUM_PASSWORD;
    private static String TRAFFICSOURCE_EMAIL;
    private static String TRAFFICSOURCE_PASSWORD;
    private static String TRAFFIC_SOURCE_ID_VOLUUM;
    private static String TRAFFICSOURCE_NAME;
    private static String DOMAIN;
    private static String TRAFFIC_TYPE;
    private static String USER_NAME;
    private static String AFFILIATE_NETWORK_NAME_VOLUUM;
    private static String AFFILIATE_NETWOK_ID;
    private static String OFFER_SHORT_URL;
    private static String COST_PAY;
    private static String VOLUUM_CAMPAIGN_LINK;
    private static String CLOAK_OFFER_NAME;
    private static String CLOAK_OFFER_ID;
    private static String CLOAK_LANDER_NAME;
    private static String CLOAK_LANDER_ID;
    private static String OFFER_WORKSPACE;
    private static String OFFER_WORKSPACE_ID;
    static Logger log = Logger.getLogger(Voluum.class.getName());

    public Voluum(boolean loginApi, Offer offer, AffiliateNetwork affiliateNetwork, TrafficSource trafficSource,
                  User user, Cloak cloak, CountryService countryService) throws IOException, ParseException {

        this.offer = offer;
        this.flowExist = offer.getOldCampaign();
        Date date = new Date();
        SimpleDateFormat simpleDate = new SimpleDateFormat("ddMMyy");
        simpleDate.setTimeZone(TimeZone.getTimeZone("EST"));
        DATE = simpleDate.format(date);

        USER_NAME = user.getNick();
        VOLUUM_ACCESS_ID = user.getVoluumAccessId();
        VOLUUM_EMAIL = user.getVoluumLogin();
        VOLUUM_ACCESS_KEY = user.getVoluumAccessKey();
        VOLUUM_PASSWORD = user.getVoluumPassword();
        CLIENT_ID = user.getVoluumClientId();
        TRAFFIC_SOURCE_ID_VOLUUM = trafficSource.getIdVoluum();
        TRAFFICSOURCE_NAME = trafficSource.getNameVoluum();
        TRAFFICSOURCE_PASSWORD = trafficSource.getPassword();
        TRAFFICSOURCE_EMAIL = trafficSource.getLogin();
        AFFILIATE_NETWORK_NAME_VOLUUM = affiliateNetwork.getNameVoluum();
        AFFILIATE_NETWOK_ID = affiliateNetwork.getIdVoluum();
        CLOAK_LANDER_NAME = cloak.getCloakLanderName();
        CLOAK_LANDER_ID = cloak.getCloakLanderId();
        CLOAK_OFFER_NAME = cloak.getCloakOfferName();
        CLOAK_OFFER_ID = cloak.getCloakOfferId();
        OFFER_WORKSPACE = user.getWorkspace();
        OFFER_WORKSPACE_ID = user.getWorkspaceId();
        OFFER_ID = offer.getOfferId();
        CustomAppender customAppender = new CustomAppender();
        log.addAppender(customAppender);

        /**
         * may be null if country empty
         */
        if (offer.getCountryCode().equalsIgnoreCase("UK")){
            offer.setCountryCode("gb");
        }
        String country_code = offer.getCountryCode().toUpperCase();

        Countries country = countryService.findByCountryCode(country_code);
        COST_PAY = country.getCost();

        if (offer.getTypeTraffic().equalsIgnoreCase("WEB")){
            TRAFFIC_TYPE = offer.getTypeTraffic();
            DOMAIN = affiliateNetwork.getDomainWeb();
            if (country_code.equalsIgnoreCase("gb")) {
                DOMAIN = DOMAIN.replace("{country}", "UK");
            } else {
                DOMAIN = DOMAIN.replace("{country}", country_code.toUpperCase());
            }
        } else {
            TRAFFIC_TYPE = offer.getTypeTraffic();
            DOMAIN = affiliateNetwork.getDomainMob();
            if (country_code.equalsIgnoreCase("gb")) {
                DOMAIN = DOMAIN.replace("{country}", "uk");
            } else {
                DOMAIN = DOMAIN.replace("{country}", country_code.toLowerCase());
            }
        }
    }

    public Voluum(User user, String shortId) {
        Date date = new Date();
        SimpleDateFormat simpleDate = new SimpleDateFormat("ddMMyy");
        simpleDate.setTimeZone(TimeZone.getTimeZone("EST"));
        DATE = simpleDate.format(date);

        this.user = user;
        VOLUUM_ACCESS_ID = user.getVoluumAccessId();
        VOLUUM_EMAIL = user.getVoluumLogin();
        VOLUUM_ACCESS_KEY = user.getVoluumAccessKey();
        VOLUUM_PASSWORD = user.getVoluumPassword();
        CLIENT_ID = user.getVoluumClientId();

        CustomAppender customAppender = new CustomAppender();
        log.addAppender(customAppender);
    }

    public void voluumAuth() throws IOException {
//        HttpHost proxy = new HttpHost("127.0.0.1", 8080);
        List<Header> headers = Arrays.asList(
                new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json"),
//                new BasicHeader(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"),
                new BasicHeader(HttpHeaders.USER_AGENT, UA),
                new BasicHeader(HttpHeaders.ACCEPT_LANGUAGE, "en-US,en;q=0.5"),
                new BasicHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate, br")
        );
        List<Header> headersFinal = new ArrayList<>(headers);

        httpClient = HttpClientBuilder.create()
                .setDefaultHeaders(headers)
//                .setProxy(proxy)
                .build();

        List<Header> newHeaders = new ArrayList<>();

        if (true) {
            HttpPost httpPost = new HttpPost("https://api.voluum.com/auth/access/session");
            ObjectMapper mapper = new ObjectMapper();
//            mapper.configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES, true);
            JsonNode rootNode = mapper.createObjectNode();
            ((ObjectNode) rootNode).put("accessId", VOLUUM_ACCESS_ID);
            ((ObjectNode) rootNode).put("accessKey", VOLUUM_ACCESS_KEY);

            StringEntity entityBody = new StringEntity(rootNode.toString());
            httpPost.setEntity(entityBody);
            httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            String responseBody = makeRequest(httpClient, httpPost);

            ObjectNode node = new ObjectMapper().readValue(responseBody, ObjectNode.class);
            TOKEN = node.get("token").textValue();

            newHeaders = Arrays.asList(
                    new BasicHeader("clientId", CLIENT_ID),
                    new BasicHeader("cwauth-token", TOKEN),
                    new BasicHeader(HttpHeaders.REFERER, "https://panel.voluum.com/?clientId=" + CLIENT_ID)
            );

        } else {

            HttpPost httpPost = new HttpPost("https://panel-api.voluum.com/auth/session");
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.createObjectNode();
            ((ObjectNode) rootNode).put("email", VOLUUM_EMAIL);
            ((ObjectNode) rootNode).put("password", VOLUUM_PASSWORD);

            StringEntity entityBody = new StringEntity(rootNode.toString());
            httpPost.setEntity(entityBody);
            httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

            String responseBody = makeRequest(httpClient, httpPost);
            ObjectNode node = new ObjectMapper().readValue(responseBody, ObjectNode.class);
            TOKEN = node.get("token").textValue();
//            logger.info(TOKEN);

            newHeaders = Arrays.asList(
                    new BasicHeader("clientId", CLIENT_ID),
                    new BasicHeader("cwauth-token", TOKEN),
                    new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json"),
                    new BasicHeader(HttpHeaders.REFERER, "https://panel.voluum.com/?clientId=" + CLIENT_ID)
            );
        }

        headersFinal.addAll(newHeaders);
        httpClient = HttpClientBuilder.create()
//                .setProxy(proxy)
                .setDefaultHeaders(headersFinal)
                .build();

    }

    public void setupVoluum() throws IOException {

        /**
         * setup list counties
         * change affiliate id
         * change affiliate name
         *
         */
        String responseBody = null;
        String country_code = offer.getCountryCode().toUpperCase();
        String country_name = offer.getCountryName();
        HttpPost httpPost = new HttpPost("https://panel-api.voluum.com/offer");

        String offer_name = String.format("%s ($%s) %s %s %s", offer.getName() + " " + offer.getPostfix(), offer.getPayoutConverted(), TRAFFIC_TYPE, USER_NAME, DATE);
        String body ="{\"name\": \"" + AFFILIATE_NETWORK_NAME_VOLUUM + " - " + offer.getCountryName() + " - " + offer_name + "\",\"namePostfix\" : \"" + offer_name + "\",\"deleted\" : false,\"url\": \"" + offer.getLink() + "\",\"country\": {\"code\": \"" + offer.getCountryCode() + "\",\"name\": \"" + offer.getCountryName() + "\"},\"affiliateNetwork\" : {\"id\" : \"" + AFFILIATE_NETWOK_ID + "\"},\"payout\" : {\"type\" : \"AUTO\"},\"tags\": [\"" + USER_NAME + "\"],\"workspace\" : {\"id\" : \"" + OFFER_WORKSPACE_ID + "\"},\"allowedActions\" : [ \"EDIT\", \"DELETE\" ]}";
//
        StringEntity entityBody = new StringEntity(body);
        httpPost.setEntity(entityBody);
        responseBody = makeRequest(httpClient, httpPost);

        boolean check = true;
        int i = 0;

        /**
         * check for unique naming
         */
        while (check) {
            i++;
            if (responseBody.contains("Name must be unique")) {
                TRAFFIC_TYPE = TRAFFIC_TYPE + i;
                offer_name = String.format("%s ($%s) %s %s %s", offer.getName() + " " + offer.getPostfix(), offer.getPayoutConverted(), TRAFFIC_TYPE, USER_NAME, DATE);
                body = "{\"name\": \"" + AFFILIATE_NETWORK_NAME_VOLUUM + " - " + offer.getCountryName() + " - " + offer_name + "\",\"namePostfix\" : \"" + offer_name + "\",\"deleted\" : false,\"url\": \"" + offer.getLink() + "\",\"country\": {\"code\": \"" + offer.getCountryCode() + "\",\"name\": \"" + offer.getCountryName() + "\"},\"affiliateNetwork\" : {\"id\" : \"" + AFFILIATE_NETWOK_ID + "\"},\"payout\" : {\"type\" : \"AUTO\"},\"tags\": [\"" + USER_NAME + "\"],\"workspace\" : {\"id\" : \"" + OFFER_WORKSPACE_ID + "\"},\"allowedActions\" : [ \"EDIT\", \"DELETE\" ]}";
//
                entityBody = new StringEntity(body);
                httpPost.setEntity(entityBody);
                responseBody = makeRequest(httpClient, httpPost);
            } else {
                check = false;
            }
        }
        ObjectNode node = new ObjectMapper().readValue(responseBody, ObjectNode.class);
        String guid_offer = node.get("id").textValue();

        /**
         * link format http://athellas.net/sexbadoo/coutnry/index.html
         *
         */

        httpPost = new HttpPost("https://panel-api.voluum.com/lander");
        String lander_name = TRAFFIC_TYPE + " " + USER_NAME + " " + DATE + " " + offer.getPostfix();
        body = "{\"namePostfix\": \"" + lander_name + "\",\"url\": \"" + DOMAIN + "\",\"numberOfOffers\": 1,\"country\": {\"code\": \"" + country_code + "\",\"name\": \"" + country_name + "\"},\"tags\": [\"" + DATE + "\",\"" + USER_NAME + "\"],\"workspace\" : {\"id\" : \"" + OFFER_WORKSPACE_ID +"\"}}";

        entityBody = new StringEntity(body);
        httpPost.setEntity(entityBody);
        responseBody = makeRequest(httpClient, httpPost);
        node = new ObjectMapper().readValue(responseBody, ObjectNode.class);
        String guid_lander = null;
        try {
            guid_lander = node.get("id").textValue();
        } catch (NullPointerException ex){
            System.out.println(responseBody);
        }

        UUID guid_flow = UUID.randomUUID();
        UUID guid_flow_path = UUID.randomUUID();
        UUID guid_flow_path_groups = UUID.randomUUID();
        UUID guid_flow_path_groups_paths = UUID.randomUUID();

        /**
         * flow list add new compaign
         */

        responseBody = checkCampaigns();
        CommonObjectListVoluum campaignsList = new ObjectMapper().readValue(responseBody, CommonObjectListVoluum.class);

        if(flowExist) {
            for (CommonObjectVoluum campaignVoluum : campaignsList.getCampaigns()) {
                if (campaignVoluum.getId().contains(offer.getCampaignEdit())) {
                    HttpGet get = new HttpGet("https://api.voluum.com/campaign/" + campaignVoluum.getId());
                    responseBody = makeRequest(httpClient, get);

                    CommonObjectVoluum objectVoluum = new ObjectMapper().readValue(responseBody, CommonObjectVoluum.class);
                    JsonNode jsonNode = objectVoluum.getRedirectTarget();
                    JsonNode flow = jsonNode.get("flow");
                    guid_flow = UUID.fromString(flow.get("id").textValue());

                    get = new HttpGet("https://api.voluum.com/flow/" + guid_flow);
                    responseBody = makeRequest(httpClient, get);
                    FlowVoluum flowVoluum = new ObjectMapper().readValue(responseBody, FlowVoluum.class);
                    OfferListVoluum offerListVoluum = new OfferListVoluum();
                    OfferVoluum offerVoluum = new OfferVoluum();
                    offerVoluum.setId(guid_offer);
                    offerListVoluum.setOffer(offerVoluum);
                    offerListVoluum.setWeight(100);
                    flowVoluum.getDefaultPaths().get(0).getOffers().add(offerListVoluum);

                    log.info(String.format("offerId %s, offerName %s, set to %s", offer.getId(), offer.getName(),flowVoluum.getId()));
                    HttpPut httpPut = new HttpPut("https://panel-api.voluum.com/flow/" + guid_flow);
                    body = new ObjectMapper().writeValueAsString(flowVoluum);
                    entityBody = new StringEntity(body);
                    httpPut.setEntity(entityBody);
                    makeRequest(httpClient, httpPut);
                    return;
                }
            }
        }


        /**
         * add to near by visits campaign
         */
//        if(flowExist) {
//            String flowsRequest = checkFlows();
//            CommonObjectListVoluum flowList = new ObjectMapper().readValue(flowsRequest, CommonObjectListVoluum.class);
//            for (CommonObjectVoluum flow : flowList.getRows()) {
//                if (StringUtils.containsIgnoreCase(flow.getFlowName(), country_name) &&
//                        StringUtils.containsIgnoreCase(flow.getFlowWorkspaceName(), OFFER_WORKSPACE) &&
//                        flow.getFlowName().contains(TRAFFIC_TYPE.substring(0, 2))) {
//                    guid_flow = UUID.fromString(flow.getFlowId());
//
//                    HttpGet get = new HttpGet("https://api.voluum.com/flow/" + guid_flow);
//                    responseBody = makeRequest(httpClient, get);
//                    FlowVoluum flowVoluum = new ObjectMapper().readValue(responseBody, FlowVoluum.class);
//                    OfferListVoluum offerListVoluum = new OfferListVoluum();
//                    OfferVoluum offerVoluum = new OfferVoluum();
//                    offerVoluum.setId(guid_offer);
//                    offerListVoluum.setOffer(offerVoluum);
//                    offerListVoluum.setWeight(100);
//                    flowVoluum.getDefaultPaths().get(0).getOffers().add(offerListVoluum);
//
//                    log.info(flowVoluum.getId());
//                    HttpPut httpPut = new HttpPut("https://panel-api.voluum.com/flow/" + guid_flow);
//                    body = new ObjectMapper().writeValueAsString(flowVoluum);
//                    entityBody = new StringEntity(body);
//                    httpPut.setEntity(entityBody);
//                    makeRequest(httpClient, httpPut);
//                    return;
//                }
//            }
//        }


        String name_flow = country_name + " " + TRAFFIC_TYPE + " " + USER_NAME + " " + DATE + " " + offer.getPostfix();

        /**
         * flows
         * cloak ids - are constants
         */

        httpPost = new HttpPost("https://panel-api.voluum.com/flow");
        body = "{\"id\": \"" + guid_flow+ "\",\"name\": \"" + name_flow + "\",\"countries\": [{\"code\": \"" + country_code + "\",\"name\": \"" + country_name + "\"}],\"defaultPaths\": [{\"id\": \"" + guid_flow_path + "\",\"name\": \"Path 1\",\"active\": true,\"weight\": 100,\"landers\": [{\"lander\": {\"id\": \"" + guid_lander+ "\",\"name\": \"" + lander_name + "\"},\"weight\": 100}],\"offers\": [{\"offer\": {\"id\": \"" + guid_offer + "\",\"name\": \"" + offer_name + "\"},\"weight\": 100}],\"offerRedirectMode\": \"REGULAR\",\"realtimeRoutingApiState\": \"DISABLED\",\"autoOptimized\" : false}],\"conditionalPathsGroups\": [{\"id\": \"" + guid_flow_path_groups + "\",\"name\": \"cloak\",\"active\": true,\"conditions\": {\"customVariable\": {\"values\": [{\"predicate\": \"MUST_NOT_BE\",\"variableIndex\": 3,\"variableValues\": [\"00000000\"]},{\"predicate\": \"MUST_BE\",\"variableIndex\": 4,\"variableValues\": [\"propellerads\"]}]}},\"paths\": [{\"id\": \"" + guid_flow_path_groups_paths + "\",\"name\": \"Path 1\",\"active\": true,\"weight\": 100,\"landers\": [{\"lander\": {\"id\": \"" + CLOAK_LANDER_ID + "\",\"name\": \"" + CLOAK_LANDER_NAME + "\"},\"weight\": 100}],\"offers\": [{\"offer\": {\"id\": \"" + CLOAK_OFFER_ID + "\",\"name\": \"" + CLOAK_OFFER_NAME +"\"},\"weight\": 100}],\"offerRedirectMode\": \"REGULAR\",\"realtimeRoutingApiState\": \"DISABLED\"}]}],\"realtimeRoutingApi\": \"DISABLED\",\"defaultOfferRedirectMode\": \"REGULAR\",\"workspace\" : {\"id\" : \"" + OFFER_WORKSPACE_ID + "\"}}";

        entityBody = new StringEntity(body);
        httpPost.setEntity(entityBody);
        responseBody = makeRequest(httpClient, httpPost);
        check = true;

        while (check) {
            i++;
            if (responseBody.contains("Name must be unique")) {
                TRAFFIC_TYPE = TRAFFIC_TYPE + i;
                offer_name = String.format("%s ($%s) %s %s %s", offer.getName() + " " + offer.getPostfix(), offer.getPayoutConverted(), TRAFFIC_TYPE, USER_NAME, DATE);
                body = "{\"id\": \"" + guid_flow+ "\",\"name\": \"" + name_flow + "\",\"countries\": [{\"code\": \"" + country_code + "\",\"name\": \"" + country_name + "\"}],\"defaultPaths\": [{\"id\": \"" + guid_flow_path + "\",\"name\": \"Path 1\",\"active\": true,\"weight\": 100,\"landers\": [{\"lander\": {\"id\": \"" + guid_lander+ "\",\"name\": \"" + lander_name + "\"},\"weight\": 100}],\"offers\": [{\"offer\": {\"id\": \"" + guid_offer + "\",\"name\": \"" + offer_name + "\"},\"weight\": 100}],\"offerRedirectMode\": \"REGULAR\",\"realtimeRoutingApiState\": \"DISABLED\",\"autoOptimized\" : false}],\"conditionalPathsGroups\": [{\"id\": \"" + guid_flow_path_groups + "\",\"name\": \"cloak\",\"active\": true,\"conditions\": {\"customVariable\": {\"values\": [{\"predicate\": \"MUST_NOT_BE\",\"variableIndex\": 3,\"variableValues\": [\"00000000\"]},{\"predicate\": \"MUST_BE\",\"variableIndex\": 4,\"variableValues\": [\"propellerads\"]}]}},\"paths\": [{\"id\": \"" + guid_flow_path_groups_paths + "\",\"name\": \"Path 1\",\"active\": true,\"weight\": 100,\"landers\": [{\"lander\": {\"id\": \"" + CLOAK_LANDER_ID + "\",\"name\": \"" + CLOAK_LANDER_NAME + "\"},\"weight\": 100}],\"offers\": [{\"offer\": {\"id\": \"" + CLOAK_OFFER_ID + "\",\"name\": \"" + CLOAK_OFFER_NAME +"\"},\"weight\": 100}],\"offerRedirectMode\": \"REGULAR\",\"realtimeRoutingApiState\": \"DISABLED\"}]}],\"realtimeRoutingApi\": \"DISABLED\",\"defaultOfferRedirectMode\": \"REGULAR\",\"workspace\" : {\"id\" : \"" + OFFER_WORKSPACE_ID + "\"}}";
                entityBody = new StringEntity(body);
                httpPost.setEntity(entityBody);
                responseBody = makeRequest(httpClient, httpPost);
            } else {
                check = false;
            }
        }

        node = new ObjectMapper().readValue(responseBody, ObjectNode.class);
        String guid_flow_for_campaign = node.get("id").textValue();
        String campaign_name = String.format("%s %s ($%s) %s", TRAFFIC_TYPE, USER_NAME, offer.getPayoutConverted(), offer.getPostfix());
        BigDecimal voluum_pay = new BigDecimal(COST_PAY).multiply(new BigDecimal(1)).divide(new BigDecimal(1000), 4, ROUND_HALF_UP);

        httpPost = new HttpPost("https://panel-api.voluum.com/campaign");
        body = "{\"namePostfix\": \"" + campaign_name+ "\",\"url\": \"\",\"impressionUrl\": \"\",\"costModel\": {\"type\": \"CPC\",\"value\": \"" + voluum_pay + "\"},\"country\": {\"code\": \"" + country_code + "\",\"name\": \"" + country_name + "\"},\"trafficSource\": {\"id\": \"" + TRAFFIC_SOURCE_ID_VOLUUM + "\",\"name\": \"" + TRAFFICSOURCE_NAME + "\",\"impressionSpecific\": false,\"customVariables\": [{\"index\": 1,\"name\": \"zoneid\",\"parameter\": \"zoneid\",\"placeholder\": \"{zoneid}\",\"trackedInReports\": true},{\"index\": 2,\"name\": \"campaignid\",\"parameter\": \"campaignid\",\"placeholder\": \"{campaignid}\",\"trackedInReports\": true},{\"index\": 3,\"name\": \"bannerid\",\"parameter\": \"bannerid\",\"placeholder\": \"{bannerid}\",\"trackedInReports\": true},{\"index\": 4,\"name\": \"propellerads\",\"parameter\": \"propellerads\",\"placeholder\": \"propellerads\",\"trackedInReports\": true}],\"workspace\": \"" + OFFER_WORKSPACE_ID + "\"},\"redirectTarget\": {\"flow\": {\"id\": \"" + guid_flow_for_campaign +"\"}},\"tags\": [],\"workspace\": {\"id\": \"" + OFFER_WORKSPACE_ID + "\"},\"customPostbacksConfiguration\": {\"customConversionPostbacks\": []}}";


        entityBody = new StringEntity(body);
        httpPost.setEntity(entityBody);
        responseBody = makeRequest(httpClient, httpPost);
        node = new ObjectMapper().readValue(responseBody, ObjectNode.class);
        VOLUUM_CAMPAIGN_LINK = node.get("url").textValue();
        Pattern pattern = Pattern.compile("(?<=\\/).{8,8}(?=-)");
        Matcher matcher = pattern.matcher(VOLUUM_CAMPAIGN_LINK);
        matcher.find();
        OFFER_SHORT_URL = matcher.group();
        propeller();
        log.info(String.format("add complete offerId %s, offerName %s, offerLink %s, offerShortCampaignId %s, offerDomain %s", OFFER_ID, offer.getName(), offer.getLink(),
                OFFER_SHORT_URL, DOMAIN));
    }

    public void VoluumStartCloak(String shortId) throws IOException {
        Date date = new Date();
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd'T'00:00:00'Z'");
        simpleDate.setTimeZone(TimeZone.getTimeZone("EST"));
        System.out.println(simpleDate.format(date));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 2);
        System.out.println(simpleDate.format(calendar.getTime()));
        DATE = simpleDate.format(date) + "&to=" + simpleDate.format(calendar.getTime());

        String constVar3 = user.getCloakConst();
        HttpGet httpGet = new HttpGet("https://panel-api.voluum.com/report?from=" + DATE + "&tz=America%2FNew_York&sort=visits&direction=desc&columns=campaignName&columns=campaignId&columns=campaignWorkspaceName&columns=campaignWorkspaceId&columns=visits&columns=clicks&columns=conversions&columns=revenue&columns=cost&columns=profit&columns=cpv&columns=ctr&columns=cr&columns=cv&columns=roi&columns=epv&columns=epc&columns=ap&groupBy=campaign&offset=0&limit=500&include=ACTIVE&conversionTimeMode=VISIT");
        String responseBody = makeRequest(httpClient, httpGet);

        CommonObjectList campaignsList = new ObjectMapper().readValue(responseBody, CommonObjectList.class);

        for (CommonObject campaignVoluum : campaignsList.getRows()) {
            if (campaignVoluum.getVisits().compareTo(new BigDecimal(5)) == 1 &&
                    campaignVoluum.getCampaignIdVoluum().contains(shortId)){

                httpGet= new HttpGet("https://api.voluum.com/campaign/" + campaignVoluum.getCampaignIdVoluum());
                responseBody = makeRequest(httpClient, httpGet);
                CommonObjectVoluum objectVoluum = new ObjectMapper().readValue(responseBody, CommonObjectVoluum.class);
                JsonNode jsonNode = objectVoluum.getRedirectTarget();
                JsonNode flow = jsonNode.get("flow");
                UUID guid_flow = UUID.fromString(flow.get("id").textValue());

                httpGet = new HttpGet("https://api.voluum.com/flow/" + guid_flow);
                responseBody = makeRequest(httpClient, httpGet);

                FlowVoluum flowVoluum = new ObjectMapper().readValue(responseBody, FlowVoluum.class);
                ArrayNode objectNode = (ArrayNode) flowVoluum.getConditionalPathsGroups().get(0).get("conditions").get("customVariable").get("values").get(0).get("variableValues");

                String strVarValue = objectNode.get(0).toString();
                System.out.println("var " + strVarValue + " contains - " + strVarValue.contains(constVar3));

                if (!strVarValue.contains(constVar3)){
                    httpGet = new HttpGet("https://panel-api.voluum.com/report?from=" + DATE + "&tz=Etc%2FGMT&sort=visits&direction=desc&columns=customVariable3&columns=visits&columns=clicks&columns=conversions&columns=revenue&columns=cost&columns=profit&columns=cpv&columns=ictr&columns=ctr&columns=cr&columns=cv&columns=roi&columns=epv&columns=epc&columns=ap&columns=errors&columns=rpm&columns=ecpm&columns=ecpc&columns=ecpa&groupBy=custom-variable-3&offset=0&limit=100&include=ACTIVE&conversionT" +
                            "imeMode=VISIT&filter1=campaign&filter1Value=" + campaignVoluum.getCampaignIdVoluum());
                    responseBody = makeRequest(httpClient, httpGet);
                    CommonObjectList listVar3 = new ObjectMapper().readValue(responseBody, CommonObjectList.class);

                    for (CommonObject campaignIncludeVar3 : listVar3.getRows()){
                        String customVariable3 = campaignIncludeVar3.getCustomVariable3();
                        BigDecimal customVarVisits = campaignIncludeVar3.getVisits();

                        if ((customVarVisits.compareTo(new BigDecimal(25)) == 1) && (customVariable3.length() == 7)
                                || (customVariable3.contains(constVar3)) && (customVariable3.length() == 7)){

                            JsonNode putNode = new ObjectMapper().readTree("\"" + customVariable3 + "\"");
                            objectNode.set(0, putNode);
                            HttpPut httpPut = new HttpPut("https://api.voluum.com/flow/" + guid_flow);
                            String body = new ObjectMapper().writeValueAsString(flowVoluum);
                            StringEntity putEntity = new StringEntity(body);
                            httpPut.setEntity(putEntity);
                            httpPut.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
                            makeRequest(httpClient, httpPut);
                            log.info("set cloak variable " + strVarValue + " == " + customVariable3 + " " +  strVarValue.equals(customVariable3));
                        }
                    }
                }
            }
        }
    }

    private void propeller() throws IOException {

        List<Header> headers = Arrays.asList(
                new BasicHeader(HttpHeaders.USER_AGENT, UA),
                new BasicHeader(HttpHeaders.REFERER, "https://partners.propellerads.com/"),
                new BasicHeader(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"),
                new BasicHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate"),
                new BasicHeader(HttpHeaders.ACCEPT_LANGUAGE, "en-US,en;q=0.5"),
                new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json")

        );
//        HttpHost proxy = new HttpHost("127.0.0.1", 8080);
        HttpClient propellerClient = HttpClientBuilder.create()
//                .setProxy(proxy)
                .setDefaultHeaders(headers)
                .build();

        HttpPost httpPost = new HttpPost("https://partners.propellerads.com/v1.0/login_check");
        String body = "{\n" +
                "  \"username\": \"" + TRAFFICSOURCE_EMAIL + "\",\n" +
                "  \"password\": \"" + TRAFFICSOURCE_PASSWORD + "\",\n" +
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

        propellerClient = HttpClientBuilder.create()
                .setDefaultHeaders(authHeaders)
//                .setProxy(proxy)
                .setDefaultCookieStore(cookieStore)
                .build();

        ZoneId zoneId = ZoneId.of("-05:00");
        ZonedDateTime time = ZonedDateTime.ofInstant(Instant.now(), zoneId);
        String time_now = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(time);

        String country_code = offer.getCountryCode().toLowerCase();
        String offer_name = String.format("%s - %s ($%s) %s %s", country_code.toUpperCase(), TRAFFIC_TYPE,
                offer.getPayoutConverted(), offer.getPostfix(), OFFER_SHORT_URL);
        httpPost = new HttpPost("https://partners.propellerads.com/v1.0/advertiser/campaigns/?dateFrom=" + time_now + "&dateTill=" + time_now+ "&isArchived=0&orderBy=id&orderDest=desc&page=1&perPage=100&refresh=0");

        if (TRAFFIC_TYPE.contains("WEB")) {
            body = "{\"rateModel\":\"scpm\",\"direction\":\"onclick\",\"frequency\":3,\"capping\":86400,\"ipFrequency\":0,\"ipCapping\":0,\"status\":1,\"targeting\":{\"connection\":\"all\",\"proxy\":\"0\",\"country\":{\"isExcluded\":false,\"list\":[\"" + country_code + "\"]},\"osType\":{\"list\":[\"desktop\"],\"isExcluded\":false},\"os\":{\"list\":[\"mac\",\"windows\"],\"isExcluded\":false},\"osVersion\":{\"list\":[\"mac10.13\",\"mac10.12\",\"mac10.11\",\"win10\",\"win8\",\"win7\"],\"isExcluded\":false},\"deviceType\":{\"list\":[\"desktop\"],\"isExcluded\":false},\"device\":{\"list\":[\"desktop\"],\"isExcluded\":false},\"zone\":{\"list\":[],\"isExcluded\":false},\"mobileIsp\":{\"list\":[],\"isExcluded\":false},\"browser\":{\"list\":[\"safari\",\"chrome\",\"firefox\"],\"isExcluded\":false},\"userActivity\":{\"list\":[\"all\"],\"isExcluded\":false},\"language\":{\"list\":[],\"isExcluded\":false},\"city\":{\"list\":[],\"isExcluded\":false},\"timeTable\":{\"list\":[\"Mon00\",\"Mon01\",\"Mon02\",\"Mon03\",\"Mon04\",\"Mon05\",\"Mon06\",\"Mon07\",\"Mon08\",\"Mon09\",\"Mon10\",\"Mon11\",\"Mon12\",\"Mon13\",\"Mon14\",\"Mon15\",\"Mon16\",\"Mon17\",\"Mon18\",\"Mon19\",\"Mon20\",\"Mon21\",\"Mon22\",\"Mon23\",\"Tue00\",\"Tue01\",\"Tue02\",\"Tue03\",\"Tue04\",\"Tue05\",\"Tue06\",\"Tue07\",\"Tue08\",\"Tue09\",\"Tue10\",\"Tue11\",\"Tue12\",\"Tue13\",\"Tue14\",\"Tue15\",\"Tue16\",\"Tue17\",\"Tue18\",\"Tue19\",\"Tue20\",\"Tue21\",\"Tue22\",\"Tue23\",\"Wed00\",\"Wed01\",\"Wed02\",\"Wed03\",\"Wed04\",\"Wed05\",\"Wed06\",\"Wed07\",\"Wed08\",\"Wed09\",\"Wed10\",\"Wed11\",\"Wed12\",\"Wed13\",\"Wed14\",\"Wed15\",\"Wed16\",\"Wed17\",\"Wed18\",\"Wed19\",\"Wed20\",\"Wed21\",\"Wed22\",\"Wed23\",\"Thu00\",\"Thu01\",\"Thu02\",\"Thu03\",\"Thu04\",\"Thu05\",\"Thu06\",\"Thu07\",\"Thu08\",\"Thu09\",\"Thu10\",\"Thu11\",\"Thu12\",\"Thu13\",\"Thu14\",\"Thu15\",\"Thu16\",\"Thu17\",\"Thu18\",\"Thu19\",\"Thu20\",\"Thu21\",\"Thu22\",\"Thu23\",\"Fri00\",\"Fri01\",\"Fri02\",\"Fri03\",\"Fri04\",\"Fri05\",\"Fri06\",\"Fri07\",\"Fri08\",\"Fri09\",\"Fri10\",\"Fri11\",\"Fri12\",\"Fri13\",\"Fri14\",\"Fri15\",\"Fri16\",\"Fri17\",\"Fri18\",\"Fri19\",\"Fri20\",\"Fri21\",\"Fri22\",\"Fri23\",\"Sat00\",\"Sat01\",\"Sat02\",\"Sat03\",\"Sat04\",\"Sat05\",\"Sat06\",\"Sat07\",\"Sat08\",\"Sat09\",\"Sat10\",\"Sat11\",\"Sat12\",\"Sat13\",\"Sat14\",\"Sat15\",\"Sat16\",\"Sat17\",\"Sat18\",\"Sat19\",\"Sat20\",\"Sat21\",\"Sat22\",\"Sat23\",\"Sun00\",\"Sun01\",\"Sun02\",\"Sun03\",\"Sun04\",\"Sun05\",\"Sun06\",\"Sun07\",\"Sun08\",\"Sun09\",\"Sun10\",\"Sun11\",\"Sun12\",\"Sun13\",\"Sun14\",\"Sun15\",\"Sun16\",\"Sun17\",\"Sun18\",\"Sun19\",\"Sun20\",\"Sun21\",\"Sun22\",\"Sun23\"],\"isExcluded\":false}},\"evenlyLimitsUsage\":false,\"trafficQuality\":1,\"autoLinkNewZones\":false,\"linkNewZonesOnce\":false,\"isAdblockBuy\":true,\"startedAt\":\"" + time_now + "\",\"rates\":[{\"countries\":[\"" + country_code + "\"],\"amount\":\"" + COST_PAY + "\"}],\"name\":\"" + offer_name + "\",\"cpaGoalStatus\":false,\"cpaGoalBid\":null,\"targetUrl\":\"" + VOLUUM_CAMPAIGN_LINK + "\",\"totalAmount\":null}";
        } else {
            body = "{\"rateModel\":\"scpm\",\"direction\":\"onclick\",\"frequency\":3,\"capping\":86400,\"ipFrequency\":0,\"ipCapping\":0,\"status\":1,\"targeting\":{\"connection\":\"all\",\"proxy\":\"0\",\"country\":{\"isExcluded\":false,\"list\":[\"" + country_code + "\"]},\"osType\":{\"list\":[\"mobile\"],\"isExcluded\":false},\"os\":{\"list\":[\"android\",\"ios\"],\"isExcluded\":false},\"osVersion\":{\"list\":[\"android8\",\"android7\",\"android6\",\"android5\",\"ios11\",\"ios10\",\"ios9\"],\"isExcluded\":false},\"deviceType\":{\"list\":[\"phone\"],\"isExcluded\":false},\"device\":{\"list\":[],\"isExcluded\":false},\"zone\":{\"list\":[],\"isExcluded\":false},\"mobileIsp\":{\"list\":[],\"isExcluded\":false},\"browser\":{\"list\":[\"safari\",\"chrome\"],\"isExcluded\":false},\"language\":{\"list\":[],\"isExcluded\":false},\"userActivity\":{\"list\":[\"all\"],\"isExcluded\":false},\"city\":{\"list\":[],\"isExcluded\":false},\"timeTable\":{\"list\":[\"Mon00\",\"Mon01\",\"Mon02\",\"Mon03\",\"Mon04\",\"Mon05\",\"Mon06\",\"Mon07\",\"Mon08\",\"Mon09\",\"Mon10\",\"Mon11\",\"Mon12\",\"Mon13\",\"Mon14\",\"Mon15\",\"Mon16\",\"Mon17\",\"Mon18\",\"Mon19\",\"Mon20\",\"Mon21\",\"Mon22\",\"Mon23\",\"Tue00\",\"Tue01\",\"Tue02\",\"Tue03\",\"Tue04\",\"Tue05\",\"Tue06\",\"Tue07\",\"Tue08\",\"Tue09\",\"Tue10\",\"Tue11\",\"Tue12\",\"Tue13\",\"Tue14\",\"Tue15\",\"Tue16\",\"Tue17\",\"Tue18\",\"Tue19\",\"Tue20\",\"Tue21\",\"Tue22\",\"Tue23\",\"Wed00\",\"Wed01\",\"Wed02\",\"Wed03\",\"Wed04\",\"Wed05\",\"Wed06\",\"Wed07\",\"Wed08\",\"Wed09\",\"Wed10\",\"Wed11\",\"Wed12\",\"Wed13\",\"Wed14\",\"Wed15\",\"Wed16\",\"Wed17\",\"Wed18\",\"Wed19\",\"Wed20\",\"Wed21\",\"Wed22\",\"Wed23\",\"Thu00\",\"Thu01\",\"Thu02\",\"Thu03\",\"Thu04\",\"Thu05\",\"Thu06\",\"Thu07\",\"Thu08\",\"Thu09\",\"Thu10\",\"Thu11\",\"Thu12\",\"Thu13\",\"Thu14\",\"Thu15\",\"Thu16\",\"Thu17\",\"Thu18\",\"Thu19\",\"Thu20\",\"Thu21\",\"Thu22\",\"Thu23\",\"Fri00\",\"Fri01\",\"Fri02\",\"Fri03\",\"Fri04\",\"Fri05\",\"Fri06\",\"Fri07\",\"Fri08\",\"Fri09\",\"Fri10\",\"Fri11\",\"Fri12\",\"Fri13\",\"Fri14\",\"Fri15\",\"Fri16\",\"Fri17\",\"Fri18\",\"Fri19\",\"Fri20\",\"Fri21\",\"Fri22\",\"Fri23\",\"Sat00\",\"Sat01\",\"Sat02\",\"Sat03\",\"Sat04\",\"Sat05\",\"Sat06\",\"Sat07\",\"Sat08\",\"Sat09\",\"Sat10\",\"Sat11\",\"Sat12\",\"Sat13\",\"Sat14\",\"Sat15\",\"Sat16\",\"Sat17\",\"Sat18\",\"Sat19\",\"Sat20\",\"Sat21\",\"Sat22\",\"Sat23\",\"Sun00\",\"Sun01\",\"Sun02\",\"Sun03\",\"Sun04\",\"Sun05\",\"Sun06\",\"Sun07\",\"Sun08\",\"Sun09\",\"Sun10\",\"Sun11\",\"Sun12\",\"Sun13\",\"Sun14\",\"Sun15\",\"Sun16\",\"Sun17\",\"Sun18\",\"Sun19\",\"Sun20\",\"Sun21\",\"Sun22\",\"Sun23\"],\"isExcluded\":false}},\"evenlyLimitsUsage\":false,\"trafficQuality\":1,\"autoLinkNewZones\":false,\"linkNewZonesOnce\":false,\"isAdblockBuy\":true,\"startedAt\":\"" + time_now + "\",\"rates\":[{\"countries\":[\"" + country_code + "\"],\"amount\":\"" + COST_PAY + "\"}],\"name\":\"" + offer_name + "\",\"cpaGoalStatus\":false,\"cpaGoalBid\":null,\"targetUrl\":\"" + VOLUUM_CAMPAIGN_LINK + "\",\"totalAmount\":null}";
        }
        entityBody = new StringEntity(body);
        httpPost.setEntity(entityBody);
        makeRequest(propellerClient, httpPost);

    }

    private String checkFlows() throws IOException {

        String date = setDateYear();
        StringBuffer url = new StringBuffer("https://panel-api.voluum.com/report?from=" + date + "&tz=America%2FNew_York&sort=visits&direction=desc&columns=flowName&columns=flowWorkspaceName&columns=impressions&columns=visits&columns=clicks&columns=conversions&columns=revenue&columns=cost&columns=profit&columns=cpv&columns=ctr&columns=cr&columns=cv&columns=roi&columns=epv&columns=epc&groupBy=flow&offset=0&limit=100&include=ACTIVE&conversionTimeMode=VISIT");
        HttpGet get = new HttpGet(url.toString());
        return makeRequest(httpClient, get);
    }

    private String checkCampaigns() throws IOException {

        String url = "https://api.voluum.com/campaign";
        HttpGet get = new HttpGet(url);
        return makeRequest(httpClient, get);
    }

    private String setDateYear(){
        Date date = new Date();
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
        simpleDate.setTimeZone(TimeZone.getTimeZone("EST"));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return "2018-05-05&to=" + simpleDate.format(calendar.getTime());
    }

    private static String makeRequest(HttpClient httpClient, HttpRequestBase http) throws IOException {
        ResponseHandler<String> responseHandler = response -> {
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {

                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            } else {
//                logger.info("error");

                return EntityUtils.toString(response.getEntity());

//                return makeRequest(httpClient, http);
            }
        };

        String responseBody = httpClient.execute(http, responseHandler);
//        if (responseBody.contains("Name must be unique")){
//            String out =  "exception in create " + httpMethod + " - Name must be unique";
//            throw new MyException(out);
//        }
        return responseBody;
    }
}