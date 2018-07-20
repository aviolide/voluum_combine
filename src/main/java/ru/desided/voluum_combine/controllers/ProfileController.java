package ru.desided.voluum_combine.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.desided.voluum_combine.entity.AffiliateNetwork;
import ru.desided.voluum_combine.entity.Cloak;
import ru.desided.voluum_combine.entity.TrafficSource;
import ru.desided.voluum_combine.entity.User;
import ru.desided.voluum_combine.repository.AffiliateNetworkRepository;
import ru.desided.voluum_combine.repository.AuthRepository;
import ru.desided.voluum_combine.repository.OfferRepository;
import ru.desided.voluum_combine.repository.UserRepository;
import ru.desided.voluum_combine.service.*;

import java.awt.*;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/profile")
public class ProfileController {

    @Autowired
    AffiliateNetworkService affiliateNetworkService;
    @Autowired
    TrafficSourceService trafficSourceService;
    @Autowired
    CloakService cloakService;
    @Autowired
    UserService userService;
    @Autowired
    OfferService offerService;
    @Autowired
    AuthRepository authRepository;

    private User user;


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView modelAndView() throws JsonProcessingException {
        user = authRepository.getUser();
        List<AffiliateNetwork> affiliateNetwork = affiliateNetworkService.findAllByUser(user);
        List<TrafficSource> trafficSources = trafficSourceService.findAllByUser(user);
        List<Cloak> cloakServices = cloakService.findAllByNick(user);
        List<User> users = userService.findAllByNick(user.getNick());

        String json = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(affiliateNetwork);
        String jsonCloak = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(cloakServices);
        String jsonTraff = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(trafficSources);
        String jsonUser = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(users);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("profile");
        modelAndView.addObject("var", json  );
        modelAndView.addObject("traffic_sources", jsonTraff);
        modelAndView.addObject("cloak", jsonCloak);
        modelAndView.addObject("users", jsonUser );
        return modelAndView;
    }

    @RequestMapping(value = "/affiliate_network/clear", method = RequestMethod.POST)
    @ResponseBody
    public String affNetworkClear(@RequestBody String string) throws IOException {
        System.out.println(string);
        AffiliateNetwork affiliateNetwork = new ObjectMapper().readValue(string, AffiliateNetwork.class);
        affiliateNetworkService.delete(affiliateNetworkService.findByIdVoluum(affiliateNetwork.getIdVoluum()));
        List<AffiliateNetwork> affiliateNetworks = affiliateNetworkService.findAllByUser(user);
        return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(affiliateNetworks);
    }

    @RequestMapping(value = "/affiliate_network/add", method = RequestMethod.POST)
    @ResponseBody
    public String affNetworkAdd(@RequestBody String string) throws IOException {
        System.out.println(string);
        AffiliateNetwork affiliateNetwork = new ObjectMapper().readValue(string, AffiliateNetwork.class);
        affiliateNetwork.setUser(userService.findByNick(user.getNick()));
        affiliateNetworkService.add(affiliateNetwork);
        List<AffiliateNetwork> affiliateNetworks = affiliateNetworkService.findAllByUser(userService.findByNick("Finik"));
        return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(affiliateNetworks);
    }

    @RequestMapping(value = "/affiliate_network/edit", method = RequestMethod.POST)
    @ResponseBody
    public String affNetworkEdit(@RequestBody String string) throws IOException {
        AffiliateNetwork affiliateNetwork = new ObjectMapper().readValue(string, AffiliateNetwork.class);
        affiliateNetworkService.edit(affiliateNetworkService.findByNameVoluum(affiliateNetwork.getNameVoluum()));
        List<AffiliateNetwork> affiliateNetworks = affiliateNetworkService.findAllByUser(user);
        return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(affiliateNetworks);
    }


    @RequestMapping(value = "/traffic_source/clear", method = RequestMethod.POST)
    @ResponseBody
    public String trafSourceDelete(@RequestBody String string) throws IOException {
        TrafficSource trafficSource = new ObjectMapper().readValue(string, TrafficSource.class);
        trafficSourceService.delete(trafficSourceService.findByIdVoluum(trafficSource.getIdVoluum()));
        List<TrafficSource> trafficSources = trafficSourceService.findAllByUser(user);
        return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(trafficSources);
    }

    @RequestMapping(value = "/traffic_source/add", method = RequestMethod.POST)
    @ResponseBody
    public String trafSourceAdd(@RequestBody String string) throws IOException {
        TrafficSource trafficSource = new ObjectMapper().readValue(string, TrafficSource.class);
        trafficSource.setUser(user);
        trafficSourceService.add(trafficSource);
        List<TrafficSource> trafficSources = trafficSourceService.findAllByUser(user);
        return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(trafficSources);
    }

    @RequestMapping(value = "/voluum/clear", method = RequestMethod.POST)
    @ResponseBody
    public String voluumDelete(@RequestBody String string) throws IOException {
        User user = new ObjectMapper().readValue(string, User.class);
        userService.delete(user);
        List<User> userList = userService.findAllByNick(user.getNick());
        return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(userList);
    }

    @RequestMapping(value = "/voluum/add", method = RequestMethod.POST)
    @ResponseBody
    public String voluumAdd(@RequestBody String string) throws IOException {

        User user = new ObjectMapper().readValue(string, User.class);
        user.setNick(this.user.getNick());
        userService.add(user);
        List<User> userList = userService.findAllByNick(user.getNick());
        return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(userList);
    }

    @RequestMapping(value = "/cloak/clear", method = RequestMethod.POST)
    @ResponseBody
    public String cloakDelete(@RequestBody String string) throws IOException {
        System.out.println(string);
        Cloak cloak = new ObjectMapper().readValue(string, Cloak.class);
        cloakService.delete(cloakService.findByCloakOfferId(cloak.getCloakOfferId()));
        List<Cloak> cloakList = cloakService.findAllByNick(user);
        return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(cloakList);
    }

    @RequestMapping(value = "/cloak/add", method = RequestMethod.POST)
    @ResponseBody
    public String cloakAdd(@RequestBody String string) throws IOException {
        Cloak cloak = new ObjectMapper().readValue(string, Cloak.class);
        cloak.setNick(this.user);
        cloakService.add(cloak);
        List<Cloak> cloakList = cloakService.findAllByNick(user);
        return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(cloakList);
    }
}
