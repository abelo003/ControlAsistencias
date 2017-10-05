package com.cruz.mx.control;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author acruzb
 */
import com.cruz.mx.control.vistas.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.cruz.mx.control.dao", "com.cruz.mx.control.config"})
public class SpringBootConsoleApplication implements CommandLineRunner {
        
    @Autowired
    private ApplicationContext applicationContext;

    public static void main(String[] args) throws Exception {
        //disabled banner, don't want to see the spring logo
        SpringApplication app = new SpringApplication(SpringBootConsoleApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.setHeadless(false);
        app.setWebEnvironment(false);
        app.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        Principal.applicationContext = applicationContext;
        Principal.main(args);
    }
}
