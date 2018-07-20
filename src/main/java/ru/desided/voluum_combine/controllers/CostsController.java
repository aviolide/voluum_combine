package ru.desided.voluum_combine.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping(value = "/costs")
public class CostsController {

    @RequestMapping(value = "/")
    public ModelAndView costs(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("costs");
        return modelAndView;
    }
}
