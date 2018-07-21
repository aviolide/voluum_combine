package ru.desided.voluum_combine.logic.add_offer.impl;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.nashorn.internal.ir.ObjectNode;
import org.apache.http.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import ru.desided.voluum_combine.LogHandler.CustomAppender;
import ru.desided.voluum_combine.controllers.OfferController;
import ru.desided.voluum_combine.entity.*;
import ru.desided.voluum_combine.logic.add_offer.POJO_JSON.Advidi.Allowed_Countries;
import ru.desided.voluum_combine.logic.add_offer.POJO_JSON.Advidi.CommonObject;
import ru.desided.voluum_combine.logic.add_offer.POJO_JSON.CommonObjectList;
import ru.desided.voluum_combine.service.CountryService;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Advidi implements AddOffersLogic{

    private HttpClient httpClient;
    private String UA = "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:55.0) Gecko/20100101 Firefox/55.0";
    private AffiliateNetwork affiliateNetwork;
    private Offer offer;
    private TrafficSource trafficSource;
    private CountryService countryService;
    private User user;
    private Cloak cloak;
    private String TRAFFIC_TYPE;
    private String AFFILIATE_LOGIN;
    private String AFFILIATE_PASSWORD;
    private String OFFER_ID;
    private String CREO_ID;
    private String BOT_ANSWER;
    private String offerContractId;
    static Logger log = Logger.getLogger(ClickDealer.class.getName());

    public Advidi(AffiliateNetwork affiliateNetwork, TrafficSource trafficSource,
                  Offer offer, CountryService countryService, User user, Cloak cloak){
        this.affiliateNetwork = affiliateNetwork;
        this.offer = offer;
        this.trafficSource = trafficSource;
        this.countryService = countryService;
        this.user = user;
        this.cloak = cloak;
        AFFILIATE_LOGIN = affiliateNetwork.getLogin();
        AFFILIATE_PASSWORD = affiliateNetwork.getPassword();
        OFFER_ID = offer.getOfferId();
        CREO_ID = offer.getLandId();
        CustomAppender customAppender = new CustomAppender();
        log.addAppender(customAppender);
    }

    @Override
    public void login() throws IOException {
        ZoneId zoneId = ZoneId.of("-05:00");
        ZonedDateTime time = ZonedDateTime.ofInstant(Instant.now(), zoneId);
        String time_now = DateTimeFormatter.ofPattern("yyyy-M-dd").format(time);
        log.info(time_now);

        List<Header> headers = Arrays.asList(
                new BasicHeader(HttpHeaders.USER_AGENT, UA),
                new BasicHeader(HttpHeaders.ACCEPT, "*/*"),
                new BasicHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate"),
                new BasicHeader(HttpHeaders.ACCEPT_LANGUAGE, "en-US,en;q=0.5"),
                new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded; charset=UTF-8")
        );

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

        HttpPost post = new HttpPost("https://ctrack.advidi.com/login.ashx");
        String body = "u=" + URLEncoder.encode(AFFILIATE_LOGIN, "UTF-8") + "&p=" + URLEncoder.encode(AFFILIATE_PASSWORD, "UTF-8") + "";
        StringEntity stringEntity = new StringEntity(body);
        post.setEntity(stringEntity);

        HttpResponse response = httpClient.execute(post);
        Header[] headers1 = response.getHeaders("Set-Cookie");
        Header hd = headers1[0];
        List<Header> authHeaders = Arrays.asList(response.getHeaders("Set-Cookie"));
        String cookieJoin = String.join("; ", authHeaders.get(0).getValue(),
                authHeaders.get(1).getValue());

        List<Header> newHeaders = Arrays.asList(
                new BasicHeader("X-Requested-With", "XMLHttpRequest"),
                new BasicHeader(HttpHeaders.REFERER, "https://ctrack.advidi.com/affiliates/"),
                new BasicHeader("Cookie", cookieJoin)
        );
        List<Header> finalHeaders = new ArrayList<>(headers);
        finalHeaders.addAll(newHeaders);
        httpClient = HttpClientBuilder.create()
                .setDefaultHeaders(finalHeaders)
                .setDefaultCredentialsProvider(credsProvider)
                .setProxy(proxy)
                .build();
    }

    @Override
    public Offer addOffer() throws IOException {

        HttpPost post = new HttpPost("https://ctrack.advidi.com/affiliates/Extjs.ashx?s=contracts");
        String body = "groupBy=&groupDir=ASC&cu=1&c=" + OFFER_ID + "&cat=0&sv=&cn=&pf=&st=0&m=&ct=&pmin=&pmax=&mycurr=true&t=&p=0&n=30";
        StringEntity stringEntity = new StringEntity(body);
        post.setEntity(stringEntity);
        String out = makeRequest(httpClient, post);
        String output = null;

        ObjectMapper objectMapper = new ObjectMapper();
        CommonObjectList commonOfferList = objectMapper.readValue(out, CommonObjectList.class);

        for (CommonObject commonOffer : commonOfferList.getRows()) {
            String offerName = commonOffer.getName();
            offer.setAffiliateNetwork(affiliateNetwork);
            offer.setOfferId(commonOffer.getCampaignId());
            offer.setStatus(commonOffer.getStatus());
            offer.setName(offerName);
            offer.setPayoutConverted(new BigDecimal(commonOffer.getPriceConverted()).setScale(3, RoundingMode.HALF_UP).stripTrailingZeros().toString());
            offerContractId = commonOffer.getContractId();

            if (commonOffer.getStatus().equals("Public")) {
                out = applyPublicOffer(commonOffer);
                ObjectNode node = new ObjectMapper().configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
                        .readValue(out, ObjectNode.class);
            } else if (commonOffer.getStatus().equals("Pending")) {
                BOT_ANSWER = OFFER_ID + " also Pending";
                offer.setStatus("Pending");
                log.info(BOT_ANSWER);
                return offer;
            } else if (commonOffer.getStatus().equals("Apply To Run")) {
                applyPublicOffer(commonOffer);
                BOT_ANSWER = OFFER_ID + " Apply To Run";
                offer.setStatus("Pending");
                log.info(BOT_ANSWER);
                return offer;
            } else if (commonOffer.getStatus().equals("Pending")) {
                BOT_ANSWER = OFFER_ID + " also Pending";
                offer.setStatus("Pending");
                log.info(BOT_ANSWER);
                return offer;
            } else if (commonOffer.getPriceConverted() == null || offer.getPayoutConverted().equals("0")) {
                BOT_ANSWER = OFFER_ID + " not available price";
                offer.setStatus("Pending");
                return offer;
            }

            try {
                Pattern pattern = Pattern.compile("- [A-Z]{2,} (\\[|-)");
                Matcher matcher = pattern.matcher(offerName);
                matcher.find();
                String countryLine = matcher.group();
                countryLine = countryLine.replace("-", "").replace("[", "").trim();

                if (countryLine.contains("UK")) {
                    countryLine = countryLine.replace("UK", "GB");
                }

                if (countryLine.matches(".*\\d+.*")) {
                    break;
                } else if (countryLine.contains("/")) {

                    /**
                     * add first county of list - change
                     */
                    List<String> splitList = Arrays.asList(countryLine.split("/"));
                    List<Allowed_Countries> allowedCountriesList = new ArrayList<>();
                    for (String codeStr : splitList) {

                        Allowed_Countries allowed_countries = new Allowed_Countries();
                        allowed_countries.setCountry_code(codeStr.toLowerCase());
                        allowed_countries.setCountry_name(countryService.findByCountryCode(codeStr.toLowerCase()).getCountryName());

                        allowedCountriesList.add(allowed_countries);
                    }
//                    commonOffer.setAllowedCountries(allowedCountriesList);
                    offer.setCountryCode(allowedCountriesList.get(0).getCountry_code());
                    offer.setCountryName(allowedCountriesList.get(0).getCountry_name());
                } else {
                    List<Allowed_Countries> allowedCountriesList = new ArrayList<>();
                    Allowed_Countries allowed_countries = new Allowed_Countries();
                    allowed_countries.setCountry_code(countryLine.toLowerCase());
                    allowed_countries.setCountry_name(countryService.findByCountryCode(countryLine.toLowerCase()).getCountryName());

                    allowedCountriesList.add(allowed_countries);
                    offer.setCountryCode(allowedCountriesList.get(0).getCountry_code());
                    offer.setCountryName(allowedCountriesList.get(0).getCountry_name());
                }
                log.info(countryLine);

            } catch (StringIndexOutOfBoundsException ex) {
                log.info(ex.toString());
            }

            if (offer.getName().toLowerCase().contains("web") || offer.getName().toLowerCase().contains("responsive")) {
                TRAFFIC_TYPE = "WEB";
            } else {
                TRAFFIC_TYPE = "MOB";
            }

            offer.setTypeTraffic(TRAFFIC_TYPE);
            break;

        }

        BOT_ANSWER = output;
        if (commonOfferList.getRows().size() == 0) {
            BOT_ANSWER = OFFER_ID + " offer not available";
            log.info(BOT_ANSWER);
            offer.setStatus("Not Available");
            return offer;
        }
        return offer;
    }

    @Override
    public Offer getCreo() throws IOException {

        HttpPost post= new HttpPost("https://ctrack.advidi.com/affiliates/Extjs.ashx?s=creatives&cont_id=" + offerContractId + "");
        String body = "groupBy=&groupDir=ASC";
        StringEntity stringEntity = new StringEntity(body);
        post.setEntity(stringEntity);
        String out = makeRequest(httpClient, post); //return null if offer pending

        if (out.equals("")) {
            offer.setStatus("Pending");
            return offer;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        CommonObjectList commonCreativesList = objectMapper.readValue(out, CommonObjectList.class);

        for (CommonObject commonObjectOffer : commonCreativesList.getRows()){

            log.info("creo " + commonObjectOffer.getId());

            if (!CREO_ID.equals ("") && commonObjectOffer.getId().contains(CREO_ID)){
                offer.setLink(commonObjectOffer.getUniqueLink()  + "{os}&s3={osversion}&s2=");
                break;
            } else {
                offer.setLink(commonCreativesList.getRows().get(0).getUniqueLink() + "{os}&s3={osversion}&s2=");
            }
            log.info(commonObjectOffer.getId() + " " + offer.getLink());
        }

        out = String.format("offer %s complete. link - %s, campaign id - %s, name - %s",
                OFFER_ID, offer.getLink(), offer.getOfferId(), offer.getName());
//
        log.info(TRAFFIC_TYPE);
        log.info(offer.getName());
        log.info(offer.getPayoutConverted());
        log.info(out);
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

    private String applyPublicOffer(CommonObject offer) throws IOException {

        HttpPost post = new HttpPost("https://ctrack.advidi.com/affiliates/Extjs.ashx?s=public&agreed=1");
        String body = "media_type=7&notes=&ccid=" + offer.getId() + "";
        StringEntity stringEntity = new StringEntity(body);
        post.setEntity(stringEntity);
        return makeRequest(httpClient, post);
    }

    private String makeRequest(HttpClient httpClient, HttpRequestBase http) throws IOException {
        String httpMethod = http.toString().substring(http.toString().lastIndexOf("/"), http.toString().length() - 1);

        ResponseHandler<String> responseHandler = response -> {
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {

                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            } else {

                return makeRequest(httpClient, http);
            }
        };

        String responseBody = httpClient.execute(http, responseHandler);
        if (responseBody == null){
            makeRequest(httpClient, http);
        }
        return responseBody;
    }


}

//    public Offer startAdvidi(AffiliateNetwork affiliateNetwork, Offer offer, CountryService countryService) {
//
//        this.countryService = countryService;
//        this.affiliateNetwork = affiliateNetwork;
//        this.offer = offer;
//        AFFILIATE_NETWORK_NAME = affiliateNetwork.getNameVoluum();
//        AFFILIATE_LOGIN = affiliateNetwork.getLogin();
//        AFFILIATE_PASSWORD = affiliateNetwork.getPassword();
//        OFFER_ID = offer.getOfferId();
//        CREO_ID = offer.getLandId();
//        CustomAppender customAppender = new CustomAppender();
//        log.addAppender(customAppender);
//
//        try {
//            setupClient();
//            log.info(startOffers(affiliateNetwork));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return this.offer;
//    }