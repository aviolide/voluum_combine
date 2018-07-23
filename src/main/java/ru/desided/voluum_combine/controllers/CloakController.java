package ru.desided.voluum_combine.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.desided.voluum_combine.LogHandler.CustomAppender;
import ru.desided.voluum_combine.LogHandler.CustomSingletonAdapter;
import ru.desided.voluum_combine.entity.Cloak;
import ru.desided.voluum_combine.entity.TrafficSource;
import ru.desided.voluum_combine.entity.User;
import ru.desided.voluum_combine.logic.setCloak.Propeller;
import ru.desided.voluum_combine.logic.setCloak.SetupCloak;
import ru.desided.voluum_combine.repository.AuthRepository;
import ru.desided.voluum_combine.service.*;

import javax.jws.WebParam;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/set_cloak")
public class CloakController {

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

    //todo change login to auth rep

    private User user;
    static Logger log = Logger.getLogger(Cloak.class.getName());

    @RequestMapping(value = "/")
    public ModelAndView setCloak(){

        CustomAppender customAppender = new CustomAppender();
        log.addAppender(customAppender);
        user = authRepository.getUser();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("set_cloak");
        modelAndView.addObject("variable", user.getCloakConst());
        modelAndView.addObject("trafficList", trafficSourceService.findAllByUser(user));

        return modelAndView;
    }
    @RequestMapping(value = "/variable/edit", method = RequestMethod.POST)
    public ModelAndView setVariable(@RequestBody String string) throws IOException {

        user.setCloakConst(string);
        userService.edit(user);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("set_cloak");
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
        SetupCloak setupCloak = new Propeller(trafficSource, user);
        setupCloak.propellerAuth();
        setupCloak.parsingCampaigns();

    }

    @RequestMapping(value = "/logging", method = RequestMethod.GET)
    public ModelAndView logging(){

        CustomSingletonAdapter.getTheInstance().getEventsList().clear();
        user = authRepository.getUser();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("logging");
        modelAndView.addObject("url", "/voluum_combine/set_cloak/logging/refresh");
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
