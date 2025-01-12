package com.dobby.backend

import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class DobbyBackendApplicationTests {

	@Test
	fun contextLoads() {
	}

	@Configuration
	class MockConfig {
		@Bean
		fun javaMailSender() : JavaMailSender {
			return Mockito.mock(JavaMailSender::class.java)
		}
	}
}
