package ru.desided.voluum_combine.logic.setCloak;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import ru.desided.voluum_combine.entity.Campaign;
import ru.desided.voluum_combine.logic.add_offer.POJO_JSON.voluum_serealize.*;
import ru.desided.voluum_combine.logic.add_offer.impl.Voluum;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;

public class VoluumCloak {

    private static String UA = "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:55.0) Gecko/20100101 Firefox/55.0";
    private static String TOKEN;
    private static String CONSTVAR3 = "19";
    private static String DATE = "2018-04-05T00:00:00Z&to=2018-04-06T00:00:00Z";
    private HttpClient httpClient;
    private HttpGet httpGet;
    private HttpPost httpPost;
    static Logger log = Logger.getLogger(Voluum.class.getName());

    public VoluumCloak(boolean loginApi) throws IOException {


        Date date = new Date();
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd'T'00:00:00'Z'");
        simpleDate.setTimeZone(TimeZone.getTimeZone("EST"));
        System.out.println(simpleDate.format(date));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 2);
        System.out.println(simpleDate.format(calendar.getTime()));
        DATE = simpleDate.format(date) + "&to=" + simpleDate.format(calendar.getTime());

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
//            ((ObjectNode) rootNode).put("accessId", VOLUUM_ACCESS_ID);
//            ((ObjectNode) rootNode).put("accessKey", VOLUUM_ACCESS_KEY);
//
//            StringEntity entityBody = new StringEntity(rootNode.toString());
//            httpPost.setEntity(entityBody);
//            httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
//            String responseBody = makeRequest(httpClient, httpPost);
//
//            ObjectNode node = new ObjectMapper().readValue(responseBody, ObjectNode.class);
//            TOKEN = node.get("token").textValue();
//
//            newHeaders = Arrays.asList(
//                    new BasicHeader("clientId", CLIENT_ID),
//                    new BasicHeader("cwauth-token", TOKEN),
//                    new BasicHeader(HttpHeaders.REFERER, "https://panel.voluum.com/?clientId=" + CLIENT_ID)
//            );
//
//        } else {
//
//            HttpPost httpPost = new HttpPost("https://panel-api.voluum.com/auth/session");
//            ObjectMapper mapper = new ObjectMapper();
//            JsonNode rootNode = mapper.createObjectNode();
//            ((ObjectNode) rootNode).put("email", VOLUUM_EMAIL);
//            ((ObjectNode) rootNode).put("password", VOLUUM_PASSWORD);
//
//            StringEntity entityBody = new StringEntity(rootNode.toString());
//            httpPost.setEntity(entityBody);
//            httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
//
//            String responseBody = makeRequest(httpClient, httpPost);
//            ObjectNode node = new ObjectMapper().readValue(responseBody, ObjectNode.class);
//            TOKEN = node.get("token").textValue();
////            logger.info(TOKEN);
//
//            newHeaders = Arrays.asList(
//                    new BasicHeader("clientId", CLIENT_ID),
//                    new BasicHeader("cwauth-token", TOKEN),
//                    new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json"),
//                    new BasicHeader(HttpHeaders.REFERER, "https://panel.voluum.com/?clientId=" + CLIENT_ID)
//            );
        }

        headersFinal.addAll(newHeaders);
        httpClient = HttpClientBuilder.create()
//                .setProxy(proxy)
                .setDefaultHeaders(headersFinal)
                .build();

    }



    public List<Campaign> ConversionsList(List<Campaign> updateList) throws IOException {

//        httpGet = new HttpGet("https://panel-api.voluum.com/report?from=" + DATE + "&tz=America%2FNew_York&filter=finik&sort=visits&direction=desc&columns=campaignName&columns=campaignId&columns=visits&columns=clicks&columns=conversions&columns=revenue&columns=cost&columns=profit&columns=cpv&columns=ictr&columns=ctr&columns=cr&columns=cv&columns=roi&columns=epv&columns=epc&columns=ap&columns=errors&columns=rpm&columns=ecpm&columns=ecpc&columns=ecpa&groupBy=campaign&offset=0&limit=1000&include=ALL&conversionTimeMode=VISIT");
//        String responseBody = makeRequest(httpClient, httpGet);
//
//        object = new JSONObject();
//        object = (JSONObject) parser.parse(responseBody);
//        JSONArray array = (JSONArray) object.get("rows");
//        Iterator<JSONObject> iterator = array.iterator();
//        while (iterator.hasNext()){
//
//            JSONObject slide = iterator.next();
//            String compainID = (String) slide.get("campaignId");
//
//            for (int i = 0; i < updateList.size(); i++) {
//
//                Campaign campaigns = updateList.get(i);
//                if (compainID.contains(campaigns.getVoluumShortId())) {
//                    BigDecimal conversions = new BigDecimal(slide.get("conversions").toString());
//                    campaigns.setVoluumFullId(compainID);
//                    campaigns.setConversions(conversions);
//                    MyLogger.log(Level.INFO, "change in " + campaigns.getName() + " convers " + conversions + " ID " + compainID);
//                    updateList.set(i, campaigns);
//                }
//            }
//
//        }

        return updateList;
    }

    private String makeRequest(HttpClient httpClient, HttpRequestBase httpPost) throws IOException {
        ResponseHandler<String> responseHandler = response -> {
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        };
        String responseBody = httpClient.execute(httpPost, responseHandler);
        return responseBody;
    }

}


