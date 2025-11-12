package edu.dosw.rideci.POSEIDON_SEARCH_AND_BOOKING;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
    "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration"
})
class PoseidonSearchAndBookingApplicationTests {

	@Test
	void contextLoads() {
	}

}
