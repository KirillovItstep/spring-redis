package org.itstep;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.URISyntaxException;

@SpringBootApplication
public class Application {

    public static void main(String[] args) throws IOException, URISyntaxException {
        SpringApplication.run(Application.class, args);
    }
}
