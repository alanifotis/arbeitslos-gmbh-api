package arbeitslos.gmbh.api;

import arbeitslos.gmbh.api.model.EmploymentStatus;
import arbeitslos.gmbh.api.model.UnemployedEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WithMockUser
class ApiApplicationTests {
	@Autowired
	WebTestClient rest;

	@Test
	void shouldReturnAllUnemployedEntities() {
		var response = rest
				.get()
				.uri("/api/v1/unemployed")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBodyList(UnemployedEntity.class)
				.returnResult();

		assertEquals(HttpStatus.OK,response.getStatus());
        assertNotNull(response.getResponseBody());
        assertFalse(response.getResponseBody().isEmpty());
		assertEquals("user",response.getResponseBody().getFirst().getFirstName());
	}

	@Test
	void shouldReturnOkAndFindByUnemployedEntityByValidId() {
		var response = rest
				.get()
				.uri("/api/v1/unemployed/0196a2c6-3072-7902-873f-36a8cec90577")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBody(UnemployedEntity.class)
				.returnResult();

		assertNotNull(response);
        assertNotNull(response.getResponseBody());
        assertEquals("user", response.getResponseBody().getFirstName());
		assertEquals(EmploymentStatus.FARMING, response.getResponseBody().getEmploymentStatus());
	}

	@Test
	void shouldReturnNotFoundByInexistentId() {
		 rest
				.get()
				.uri("/api/v1/unemployed/0196a2c6-3072-7902-873f-bba8cec90577")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isNotFound()
				.expectBody().isEmpty();
	}

	@Test
	void shouldReturnBadRequestOnInvalidId() {
		rest
				.get()
				.uri("/api/v1/unemployed/0196a2c6-3072-873f-bba8cec90577")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isBadRequest()
				.expectBody()
				.jsonPath("$.error").isEqualTo("Bad Request")
				.returnResult();

	}

}
