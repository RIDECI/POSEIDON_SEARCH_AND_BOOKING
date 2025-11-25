package edu.dosw.rideci;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class PoseidonSearchAndBookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(PoseidonSearchAndBookingApplication.class, args);
	}

}
