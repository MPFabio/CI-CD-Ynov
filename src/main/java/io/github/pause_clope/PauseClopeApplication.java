package io.github.pause_clope;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication()
// Disable auto-configuration security for the main class
public class PauseClopeApplication {

    public static void main(String[] args) {
        SpringApplication.run(PauseClopeApplication.class, args);
    }
}
