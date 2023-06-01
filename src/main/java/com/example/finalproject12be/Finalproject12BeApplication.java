package com.example.finalproject12be;

import java.util.ArrayList;
import java.util.Arrays;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
@EnableEncryptableProperties
public class Finalproject12BeApplication {

    public static void main(String[] args) {
        SpringApplication.run(Finalproject12BeApplication.class, args);

    }
}
