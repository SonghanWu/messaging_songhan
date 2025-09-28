package com.songhanwu.messaging;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
    "spring.profiles.active=test"
})
class MessagingSystemApplicationTests {

	@Test
	void contextLoads() {
	}

}
