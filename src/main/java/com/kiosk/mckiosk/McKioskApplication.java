package com.kiosk.mckiosk;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:beans.xml")
public class McKioskApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(McKioskApplication.class, args);
    }

    public void run(String... args) throws Exception {
    }

}
