package ru.desided.voluum_combine.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.desided.voluum_combine.LogHandler.CustomAppender;
import ru.desided.voluum_combine.LogHandler.CustomSingletonAdapter;
import ru.desided.voluum_combine.entity.*;
import ru.desided.voluum_combine.logic.add_offer.AddOffers;
import ru.desided.voluum_combine.logic.add_offer.AddOffersImpl;
import ru.desided.voluum_combine.repository.AuthRepository;
import ru.desided.voluum_combine.service.*;
import ru.desided.voluum_combine.service.impl.CountryServiceImpl;
import ru.desided.voluum_combine.service.impl.OfferServiceImpl;

import javax.jws.WebParam;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/add_offer")
public class OfferController {

    @Autowired
    private UserService userService;
    @Autowired
    private AffiliateNetworkService affiliateNetworkService;
    @Autowired
    TrafficSourceService trafficSourceService;
    @Autowired
    OfferService offerService;
    @Autowired
    CountryService countryService;
    @Autowired
    CloakService cloakService;
    @Autowired
    AuthRepository authRepository;
    static Logger log = Logger.getLogger(OfferController.class.getName());

    private User user;
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView setup() throws JsonProcessingException {

        System.out.println(authRepository.getUser().getVoluumLogin() + authRepository.getUser().getVoluumClientId());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("add_offer");
        String offerList = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(offerService.findByStatusEquals("Pending"));

        user = userService.findByNick(authRepository.getUser().getNick());
        CustomAppender customAppender = new CustomAppender();
        log.addAppender(customAppender);

        List<AffiliateNetwork> affiliateNetworkList = affiliateNetworkService.findAllByUser(user);
        modelAndView.addObject("affiliateList", affiliateNetworkList);
        modelAndView.addObject("trafficList", trafficSourceService.findAllByUser(user));
        modelAndView.addObject("offerList", offerList);

        return modelAndView;
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public void add_Offer(@RequestParam(value = "campaign_id") String campaign_id,
                           @RequestParam(value = "land_id") String land_id) throws JsonProcessingException {
        Offer offer = new Offer();
        offer.setOfferId(campaign_id);
        offer.setLandId(land_id);
        offer.setUser(user);
        offerService.addOffer(offer);
        System.out.println(new ObjectMapper().writeValueAsString(offer));
    }

    @RequestMapping(value = "/check_pending", method = RequestMethod.GET)
    public void check_pending() throws JsonProcessingException {
        AddOffers addOffers = new AddOffersImpl();
        List<Offer> offers = offerService.findByStatusEquals("Pending");
        for (Offer offer :offers) {

            AffiliateNetwork affiliateNetwork = offer.getAffiliateNetwork();
//            TrafficSource trafficSource =
            if (StringUtils.containsIgnoreCase(affiliateNetwork.getNameVoluum(), "advidi")) {
                CountryService countryService = this.countryService;
//                addOffers.AddAdvidi(affiliateNetwork, trafficSource, newCampaign, offer, countryService, user, cloak);
                if (offer.getStatus().equals("Active")){
                    offerService.deleteOffer(offer);
                }
            } else {
//            offerListToSave = addOffers.AddClickDealer(affiliateNetwork, trafficSource, newCampaign, offers);
            }
        }
    }

    @RequestMapping(value = "/start", method = RequestMethod.POST)
    public void start(@RequestBody String string) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.readValue(string, ObjectNode.class);

        AffiliateNetwork affiliateNetwork = affiliateNetworkService.findByNameVoluum(node.get("affiliate_network").asText());
        TrafficSource trafficSource = trafficSourceService.findByNameVoluum(node.get("traffic_source").asText());
        Cloak cloak = cloakService.findByNick(user);
        ArrayNode node1 = (ArrayNode) node.get("campaigns_list");
        List<Offer> offers = new ArrayList<>();

        node1.elements().forEachRemaining(n -> {
            try {
                Offer offer = objectMapper.treeToValue(n, Offer.class);
                offers.add(offer);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });

        CustomSingletonAdapter.getTheInstance().getEventsList().clear();
        AddOffers addOffers = new AddOffersImpl();
        CountryService countryService = this.countryService;
        for (Offer offer : offers) {
            if (StringUtils.containsIgnoreCase(affiliateNetwork.getNameVoluum(), "advidi")) {
                addOffers.addAdvidi(affiliateNetwork, trafficSource, offer, countryService, user, cloak);
                if (offer.getStatus() != "Not Available" || offerService.findByOfferIdAndAffiliateNetwork(offer, affiliateNetwork) != null) {
                    offerService.addOffer(offer);
                }
            } else if (StringUtils.containsIgnoreCase(affiliateNetwork.getNameVoluum(), "clickdealer")){
                addOffers.addClickDealer(affiliateNetwork, trafficSource, offer, countryService, user, cloak);
                if (offer.getStatus() != "Not Available" || offerService.findByOfferIdAndAffiliateNetwork(offer, affiliateNetwork) != null) {
                    offerService.addOffer(offer);
                }
            } else if (StringUtils.containsIgnoreCase(affiliateNetwork.getNameVoluum(), "adtrafico")){
                addOffers.addAdtrafico(affiliateNetwork, trafficSource, offer, countryService, user, cloak);
                if (offer.getStatus() != "Not Available" || offerService.findByOfferIdAndAffiliateNetwork(offer, affiliateNetwork) != null) {
                    offerService.addOffer(offer);
                }
            }

            log.info("<br />");
        }


    }

    @RequestMapping(value = "/logging", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView logging() {
        CustomSingletonAdapter.getTheInstance().getEventsList().clear();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("logging");
        modelAndView.addObject("url", "/voluum_combine/add_offer/logging/refresh");
        return modelAndView;
    }

    @RequestMapping(value = "/logging/refresh", method = RequestMethod.GET)
    @ResponseBody
    public String loggingPost() {
        String str = CustomSingletonAdapter.getTheInstance().getEventsList().
                stream().
                map(s -> s.getMessage().toString()).
                collect(Collectors.joining("<br />"));
        System.out.println(str);
        return str;
    }
}
