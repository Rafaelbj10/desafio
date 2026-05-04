package com.desafio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.desafio.infra.client")
@SpringBootApplication
public class DesafioApplication {

    public static void main(String[] args) {
        SpringApplication.run(DesafioApplication.class, args);
    }

}
