package com.simple.blog.backend.infra.config.initializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Initializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(Initializer.class);
    private final DataInitializer dataInitializer;

    public Initializer(DataInitializer dataInitializer) {
        this.dataInitializer = dataInitializer;
    }

    @Override
    public void run(String... args) throws Exception {
        dataInitializer.initializeData();
    }

}
