package com.kwon.myshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MyshopApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyshopApiApplication.class, args);
    }

}
