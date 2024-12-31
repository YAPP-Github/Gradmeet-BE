package com.dobby.backend

import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class DobbyBackendApplicationTests {

	private val clientRegistrationRepository = Mockito.mock(ClientRegistrationRepository::class.java)

	@Test
	fun contextLoads() {
	}

}
