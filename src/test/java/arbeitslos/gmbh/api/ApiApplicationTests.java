package arbeitslos.gmbh.api;

import arbeitslos.gmbh.api.model.EmploymentStatus;
import arbeitslos.gmbh.api.model.UnemployedEntity;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiApplicationTests {
	@Autowired
	WebTestClient rest;

	@Test
	@WithMockUser
	void shouldReturnAllUnemployedEntities() {
		var response = rest
				.get()
				.uri("/api/v1/unemployed")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBodyList(UnemployedEntity.class)
				.returnResult()
				;

		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK);
        assertNotNull(response.getResponseBody());
        assertFalse(response.getResponseBody().isEmpty());
		assertThat(response.getResponseBody().getFirst().getFirstName()).isEqualTo("user");
	}

	@Test
	@WithMockUser
	void shouldFindByUnemployedEntityByValidId() {
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

}
