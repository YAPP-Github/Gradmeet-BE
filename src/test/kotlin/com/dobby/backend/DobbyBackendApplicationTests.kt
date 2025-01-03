package com.dobby.backend

import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@EnableJpaAuditing
@ActiveProfiles("test")
class DobbyBackendApplicationTests {
	@Test
	fun contextLoads() {
	}

}
