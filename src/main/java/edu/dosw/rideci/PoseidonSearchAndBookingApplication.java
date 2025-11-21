package edu.dosw.rideci;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
    "edu.dosw.rideci",
    "edu.dosw.rideci.infrastructure"
})
public class PoseidonSearchAndBookingApplication {

    public static void main(String[] args) {
        SpringApplication.run(PoseidonSearchAndBookingApplication.class, args);
    }
}