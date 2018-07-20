package ru.desided.voluum_combine.logic.setCloak;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.ParseException;
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
import ru.desided.voluum_combine.logic.add_offer.impl.Voluum;
import ru.desided.voluum_combine.service.CountryService;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class Propeller implements SetupCloak{

    private String DATE = null;
    private String TOKEN = null;
    private String LOGIN;
    private String PASS;
    private String UA = "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:55.0) Gecko/20100101 Firefox/55.0";
    private String VOLUUM_ACCESS_ID;
    private String VOLUUM_ACCESS_KEY;
    private String CLIENT_ID;
    private String VOLUUM_EMAIL;
    private String VOLUUM_PASSWORD;
    private String CONSTVAR3;

    private List<Campaign> campaignsList = new ArrayList<>();
    private List<Campaign> activeCampaignsList = new ArrayList<>();
    private HttpClient httpClient;
    private boolean isNewbie = true;
    static Logger log = Logger.getLogger(Propeller.class.getName());
    private User user;
    private TrafficSource trafficSource;

    public Propeller(TrafficSource trafficSource, User user){
        this.user = user;
        this.trafficSource = trafficSource;
        VOLUUM_ACCESS_ID = user.getVoluumAccessId();
        VOLUUM_EMAIL = user.getVoluumLogin();
        VOLUUM_ACCESS_KEY = user.getVoluumAccessKey();
        VOLUUM_PASSWORD = user.getVoluumPassword();
        CONSTVAR3 = user.getCloakConst();
        CustomAppender customAppender = new CustomAppender();
        log.addAppender(customAppender);
    }

    public void propellerAuth() throws IOException {

        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("EST"));
        DATE = simpleDateFormat.format(date);

        List<Header> headers = Arrays.asList(
                new BasicHeader(HttpHeaders.USER_AGENT, UA),
                new BasicHeader(HttpHeaders.REFERER, "https://partners.propellerads.com/"),
                new BasicHeader(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"),
                new BasicHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate"),
                new BasicHeader(HttpHeaders.ACCEPT_LANGUAGE, "en-US,en;q=0.5"),
                new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json")
        );

//      HttpHost proxy = new HttpHost("zproxy.luminati.io", 22225);
//        CredentialsProvider credsProvider = new BasicCredentialsProvider();
//        credsProvider.setCredentials(
//                new AuthScope("zproxy.luminati.io", 22225),
//                new UsernamePasswordCredentials("lum-customer-hl_17d43f88-zone-md_1", "mk389141"));
        httpClient = HttpClientBuilder.create()
//                .setDefaultCredentialsProvider(credsProvider)
//                .setProxy(proxy)
                .setDefaultHeaders(headers)
                .build();

        HttpPost httpPost = new HttpPost("https://partners.propellerads.com/v1.0/login_check");

        StringEntity entity = new StringEntity("{\"username\":\"" + LOGIN + "\",\"password\":\"" + PASS + "\",\"gRecaptchaResponse\":null,\"type\":\"ROLE_ADVERTISER\",\"fingerprint\":\"6dabbc2b9ac8d06a961fd6efa56130bb\"}");
        httpPost.setEntity(entity);
        JsonNode node = new ObjectMapper().readTree(makeRequest(httpClient, httpPost));
        String resultJson = URLEncoder.encode(node.get("result").toString(), "UTF-8");

        TOKEN = node.get("result").get("accessToken").textValue();
        System.out.println(TOKEN);

        List<Header> authHeaders = new ArrayList<>(headers);
        authHeaders.add(new BasicHeader("Authorization", "Bearer " + TOKEN));

        BasicCookieStore cookieStore = new BasicCookieStore();
        BasicClientCookie clientCookie = new BasicClientCookie("user", resultJson);
        clientCookie.setDomain("partners.propellerads.com");
        cookieStore.addCookie(clientCookie);


        httpClient = HttpClientBuilder.create()
                .setDefaultHeaders(authHeaders)
//                .setDefaultCredentialsProvider(credsProvider)
//                .setProxy(proxy)
                .setDefaultCookieStore(cookieStore)
                .build();

    }

        public void parsingCampaigns() throws IOException, InterruptedException {

            campaignsList = getDataFromPropeller();
            /**
             * GET ACTIVE COMPAIGNS LIST, AND START COMPAING
             * status 1 - inactive
             * status 2 - moderation
             * status 6 - active
             * status 8 - stop
             */


            if (isNewbie) {
                for (Campaign comp : campaignsList) {

//                if (!comp.getName().contains("SE -") && !comp.getName().contains("ce844829") &&
//                        !comp.getName().contains("33ebb782") && !comp.getName().contains("c6582282")) {//del

                    if (comp.getStatusCampaign().compareTo(new BigDecimal("1")) == 0) {

                        System.out.println(comp.getStatusCampaign());
                        HttpPut httpPut = new HttpPut("https://partners.propellerads.com/v1.0/advertiser/campaigns/start/");
                        StringEntity stringEntity = new StringEntity("{\"campaignIds\":[" + comp.getId() + "]}");
                        httpPut.setEntity(stringEntity);
                        makeRequest(httpClient, httpPut);

                        activeCampaignsList.add(comp);
                        log.info("Start " + comp.getName());

                    } else if (comp.getStatusCampaign().compareTo(new BigDecimal("2")) == 0) {
                        activeCampaignsList.add(comp);
                        log.info( "Start " + comp.getName());
                    } else if (comp.getImpressions().compareTo(new BigDecimal("5000")) == -1
                            && !(comp.getStatusCampaign().compareTo(new BigDecimal("8")) == 0)) {
                        log.info( "Start " + comp.getName());
                        activeCampaignsList.add(comp);
                    }
//                }
                }

                Thread.sleep(1000);

                campaignsList = getDataFromPropeller();
                /**
                 * STARTING CLOAK*
                 */
                boolean check = true;

                while (check) {
                    for (Campaign comp : activeCampaignsList) {
                        log.info( "active name " + comp.getName());
                        for (Campaign innComp : campaignsList) {

                            if (innComp.getName().equals(comp.getName())) {

                                if (innComp.getImpressions().compareTo(new BigDecimal("10")) == 1) {

                                    if (!comp.getCloak()) {

                                        System.out.println(comp.getImpressions());
                                        log.info( "Start cloak " + comp.getName());
                                        //change cloak ID
                                        setupVoluum(user, comp.getVoluumShortId());
                                        comp.setCloak(true);
                                        comp.setSpent(innComp.getSpent());
                                    }
                                }
                            }
                        }

                        //System.out.println(comp.getCloak());
                        System.out.println(comp.getName() + " is cloak - " + comp.getCloak());
                        log.info(comp.getName() + " is cloak - " + comp.getCloak());
                    }

                    if (isNewbie){
                        log.info( "sleep 1 min");
                        TimeUnit.MINUTES.sleep(1);
                    }

                    int countCloak = 0;
                    for (int i = 0; i < activeCampaignsList.size(); i++) {
                        Campaign compCheckBoolean =  activeCampaignsList.get(i);

                        if (compCheckBoolean.getCloak()) {
                            countCloak++;
                        }
                        if (countCloak == activeCampaignsList.size()) {
                            check = false;
                        }
                    }
                    campaignsList = getDataFromPropeller();
                }
            } else activeCampaignsList.addAll(getDataFromPropeller());

            log.info( "finish changing cloak");

//            while (activeCampaignsList.size() > 0) {
//
//                CampaignResult priceCompare = new CampaignResult();
//                activeCampaignsList = updateCompaign(activeCampaignsList);
//                System.out.println(activeCampaignsList.size());
//                activeCampaignsList = new VoluumCloak(false).ConversionsList(activeCampaignsList);
//                log.info( "size before " + activeCampaignsList.size());
//                priceCompare.compareSpent(activeCampaignsList, httpClient); //==activecoma
//                log.info( "sleep 3 min, size " + activeCampaignsList.size());
//                TimeUnit.MINUTES.sleep(3);
//            }

        }


    private List<Campaign> getDataFromPropeller() throws IOException {

        campaignsList.clear();
        HttpGet httpGet = new HttpGet("https://partners.propellerads.com/v1.0/advertiser/campaigns/?" +
                "dateFrom=" + DATE + "&dateTill=" + DATE + "&isArchived=0&orderBy=id&orderDest=desc&page=1&perPage=200&refresh=0");
        String request = makeRequest(httpClient, httpGet);

        JsonNode object = new ObjectMapper().readTree(request).get("result").get("items");
        List<Campaign> campaigns = new ObjectMapper().readValue(object.toString(), new TypeReference<List<Campaign>>(){});

        for (Campaign campaign : campaigns) {

            String idVoluumComp = campaign.getName();
            System.out.println(idVoluumComp);
            BigDecimal price = new BigDecimal(idVoluumComp.substring(idVoluumComp.indexOf("(") + 2, idVoluumComp.indexOf(")")));
            idVoluumComp = idVoluumComp.substring(idVoluumComp.lastIndexOf(" ") + 1, idVoluumComp.length());
            campaign.setPrice(price);
            campaign.setVoluumShortId(idVoluumComp);

        }
        return campaigns;
    }

    private List<Campaign> updateCompaign(List<Campaign> updateList) throws IOException {

        campaignsList.clear();

        HttpGet httpGet = new HttpGet("https://partners.propellerads.com/v1.0/advertiser/campaigns/?" +
                "dateFrom=" + DATE + "&dateTill=" + DATE + "&isArchived=0&orderBy=id&orderDest=desc&page=1&perPage=100&refresh=0");
        String request = makeRequest(httpClient, httpGet);
        JsonNode object = new ObjectMapper().readTree(request).get("result").get("items");
        List<Campaign> campaigns = Arrays.asList(new ObjectMapper().readValue(object.toString(), Campaign[].class));

        for (Campaign campaign : campaigns) {

            for (int i = 0; i < updateList.size(); i++) {

                Campaign campaignUpdate = updateList.get(i);
                if (campaignUpdate.getName().equals(campaign.getName())) {

                    BigDecimal spent = campaign.getSpent();
                    campaignUpdate.setSpent(spent);
                    updateList.set(i, campaignUpdate);
                }
            }
        }
        return updateList;
    }

    @Override
    public void setupVoluum(User user, String shortId) throws IOException {
        Voluum voluum = new Voluum(user, shortId);
        voluum.voluumAuth();
        voluum.VoluumStartCloak(shortId);
    }


    private static String makeRequest(HttpClient httpClient, HttpRequestBase httpPost) throws IOException {
        ResponseHandler<String> responseHandler = response -> {
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            } else {
                return EntityUtils.toString(response.getEntity());
            }
        };
        String responseBody = httpClient.execute(httpPost, responseHandler);

        return responseBody;
    }
}
