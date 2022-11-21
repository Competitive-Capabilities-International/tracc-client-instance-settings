package com.traccsolution.tracc_client_instance_settings;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author KeeshanReddy
 */
@SpringBootApplication
@EnableFeignClients(basePackages = "com.traccsolution")
public class tracc_client_instance_settingsApplication {

    public static void main(String[] args) {
        SpringApplication.run(tracc_client_instance_settingsApplication.class, args);
    }

}
