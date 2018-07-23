package ru.desided.voluum_combine.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.desided.voluum_combine.LogHandler.CustomAppender;
import ru.desided.voluum_combine.LogHandler.CustomSingletonAdapter;
import ru.desided.voluum_combine.entity.Cloak;
import ru.desided.voluum_combine.entity.Offer;
import ru.desided.voluum_combine.entity.TrafficSource;
import ru.desided.voluum_combine.entity.User;
import ru.desided.voluum_combine.logic.add_offer.impl.Propeller;
import ru.desided.voluum_combine.logic.monitoring.StartMonitoring;
import ru.desided.voluum_combine.repository.AuthRepository;
import ru.desided.voluum_combine.service.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/monitoring")
public class MonitoringController {

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

    private User user;

    static Logger log = Logger.getLogger(MonitoringController.class.getName());
    private StartMonitoring monitoring;
    private Propeller propeller;

    @RequestMapping(value = "/")
    public ModelAndView setup(){

        CustomAppender customAppender = new CustomAppender();
        log.addAppender(customAppender);
        user = authRepository.getUser();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("monitoring");
        modelAndView.addObject("variable", user.getCloakConst());
        modelAndView.addObject("trafficList", trafficSourceService.findAllByUser(user));

        return modelAndView;
    }

    @RequestMapping(value = "/start", method = RequestMethod.POST)
    public void start(@RequestBody String string) throws IOException, InterruptedException {

        JsonNode node = new ObjectMapper().readTree(string);
        System.out.println(node.get("traffic_sources").toString());
        List<TrafficSource> list = Arrays.asList(new ObjectMapper().readValue(node.get("traffic_sources").toString(), TrafficSource[].class));
        TrafficSource trafficSource = trafficSourceService.findByNameVoluum(list.get(0).getNameVoluum());
        user = authRepository.getUser();
        propeller =  new Propeller(new Offer(), trafficSource);
        propeller.setStartBol(true);
        monitoring = propeller;
        monitoring.propellerAuth();
        monitoring.monitoring(user);
    }

    @RequestMapping(value = "/logging", method = RequestMethod.GET)
    public ModelAndView logging(){

        CustomSingletonAdapter.getTheInstance().getEventsList().clear();
        user = authRepository.getUser();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("logging");
        modelAndView.addObject("url", "/voluum_combine/monitoring/logging/refresh");
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

    @RequestMapping(value = "/stop", method = RequestMethod.GET)
    public void stop(){
        propeller.setStartBol(false);
    }
}
