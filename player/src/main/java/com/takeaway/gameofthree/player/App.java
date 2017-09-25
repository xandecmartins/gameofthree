package com.takeaway.gameofthree.player;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages={"com.takeaway.gameofthree"})
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}