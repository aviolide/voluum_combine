package ru.desided.voluum_combine.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.desided.voluum_combine.entity.AffiliateNetwork;
import ru.desided.voluum_combine.entity.User;
import ru.desided.voluum_combine.repository.AffiliateNetworkRepository;
import ru.desided.voluum_combine.repository.AuthRepository;
import ru.desided.voluum_combine.repository.UserRepository;


@RestController
@RequestMapping(value = "/")
public class LoginController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AffiliateNetworkRepository AffiliateNetworkRepository;
    @Autowired
    private AuthRepository authRepository;

    @GetMapping("/")
    private ModelAndView login(){
        return new ModelAndView("login");
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView modelAndView(@RequestParam(value = "api_id") String apiId,
                                     @RequestParam(value = "api_key") String apiKey){

        User user = userRepository.findByVoluumLoginAndVoluumPassword(apiId, apiKey);
        authRepository.setUser(user);
        System.out.println(user.getNick() + user.getVoluumAccessId());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("dashboard");

        return modelAndView;
    }
}
