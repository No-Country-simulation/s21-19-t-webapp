package com.backend.urbia;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test") // Usar perfil "test"
class UrbiaApplicationTests {


	@Test
	void contextLoads() {
		// Test de carga básica del contexto
	}
}
