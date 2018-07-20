package ru.desided.voluum_combine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = {"ru.desided","ru.desided.voluum_combine.logic.add_offer.impl"})
//@ComponentScan("ru.desided")
public class VoluumCombineApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(VoluumCombineApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(VoluumCombineApplication.class);
    }
}
