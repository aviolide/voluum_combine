package ru.desided.voluum_combine.logic.setCloak;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import ru.desided.voluum_combine.entity.Campaign;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.logging.Level;

public class CampaignResult {

    static Logger log = Logger.getLogger(Propeller.class.getName());

    public List<Campaign> compareSpent(List<Campaign> activeCampaignsList, HttpClient httpClientPropeller) throws IOException {

        for (int i = 0; i < activeCampaignsList.size(); i++) {

            Campaign campaigns = activeCampaignsList.get(i);
            BigDecimal conversions = campaigns.getConversions();
            BigDecimal price = campaigns.getPrice();
            BigDecimal spent = campaigns.getSpent();
            BigDecimal roi = null;
            BigDecimal revenue = null;
            BigDecimal profit = null;
            try {
                revenue = campaigns.getConversions().multiply(campaigns.getPrice());
                profit = revenue.subtract(spent).setScale(2, RoundingMode.HALF_UP);
                roi = profit.divide(spent, 2, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
            } catch (ArithmeticException e) {
                System.out.println("divide by zero" + e);
                activeCampaignsList.remove(i);
            } catch (NullPointerException nullPoint){
                System.out.println("NullPoint" + nullPoint);
            }


            String printInLog = String.format("compaign %s, price %s, conversions %s, spent %s, revenue %s, profit %s, roi %s",
                    campaigns.getName(), campaigns.getPrice(), campaigns.getConversions(), campaigns.getSpent(),
                    revenue, profit, roi);
            log.info( printInLog);

            if ((price.compareTo(new BigDecimal("5")) == -1)
                    && (((price.multiply(new BigDecimal("3")).compareTo(spent) == -1)
                    && (conversions.multiply(price).compareTo(spent) == -1))
                    || ((price.multiply(new BigDecimal("1")).compareTo(spent) == -1)
                    && (conversions.compareTo(new BigDecimal("0")) == 0)))){

                int idFormula = 1;
                stopCompaign(httpClientPropeller, campaigns, idFormula);
                activeCampaignsList.remove(i);
                i--;

            } else if ((price.compareTo(new BigDecimal("5")) == 1 && price.compareTo(new BigDecimal("10")) == -1)
                    && ((price.multiply(new BigDecimal("0.75")).compareTo(spent) == -1
                    && conversions.multiply(price).compareTo(spent) == -1)
                    || (price.compareTo(spent) == -1
                    && conversions.compareTo(new BigDecimal("0")) == 0))){

                int idFormula = 2;
                stopCompaign(httpClientPropeller, campaigns, idFormula);
                activeCampaignsList.remove(i);
                i--;
            } else if ((price.compareTo(new BigDecimal("10")) == 1)
                    && ((price.compareTo(spent) == -1
                    && conversions.multiply(price).compareTo(spent) == -1)
                    || (price.multiply(new BigDecimal("0.6")).compareTo(spent) == -1
                    && conversions.compareTo(new BigDecimal("0")) == 0))) {


                int idFormula = 3;
                stopCompaign(httpClientPropeller, campaigns, idFormula);
                activeCampaignsList.remove(i);
                i--;
            } else if ((conversions.multiply(price).compareTo(new BigDecimal("10")) == 1)
                    && (conversions.multiply(price).compareTo(spent) == 1)){

                printInLog = String.format("campaign get profit %s, price %s, conversions %s, spentAmount %s",
                        campaigns.getName(), campaigns.getPrice(), campaigns.getConversions(), campaigns.getSpent());
                log.info( printInLog);
            } else if (conversions.multiply(price).compareTo(new BigDecimal(10)) == 1
                    && conversions.multiply(price).compareTo(spent) == -1){

                printInLog = String.format("campaign not profit %s, price %s, conversions %s, spentAmount %s",
                        campaigns.getName(), campaigns.getPrice(), campaigns.getConversions(), campaigns.getSpent());
                log.info( printInLog);
                int idFormula = 4;
                stopCompaign(httpClientPropeller, campaigns, idFormula);
                activeCampaignsList.remove(i);
                i--;
            }

        }
        return activeCampaignsList;
    }


    private void stopCompaign(HttpClient httpClientPropeller, Campaign campaign, int idFormula) throws IOException {

        HttpPut httpPut = new HttpPut("https://partners.propellerads.com/v1.0/advertiser/campaigns/stop/");
        StringEntity stringEntity = new StringEntity("{\"campaignIds\":[" + campaign.getCampaignId().toString() + "]}");
        httpPut.setEntity(stringEntity);

        if (campaign.getStatusCampaign().toString().equals("6")) {
            makeRequest(httpClientPropeller, httpPut);
        }

        String printInLog = String.format("stop compaign %s, price %s, conversions %s, spentAmount %s, formula %d",
                campaign.getName(), campaign.getPrice(), campaign.getConversions(), campaign.getSpent(), idFormula);
        log.info( printInLog);
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
