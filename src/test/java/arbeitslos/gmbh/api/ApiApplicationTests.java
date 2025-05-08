package arbeitslos.gmbh.api;

import arbeitslos.gmbh.api.model.EmploymentStatus;
import arbeitslos.gmbh.api.model.UnemployedEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.NoSuchElementException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WithMockUser
class ApiApplicationTests {
    @Autowired
    private WebTestClient rest;
    private final String subUri = "/api/v1/unemployed";
    @LocalServerPort
    private int port;
    private final String host = "http://localhost:";

    @Test
    void shouldReturnAllUnemployedEntities() {
        var response = rest
                .get()
                .uri(subUri)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UnemployedEntity.class)
                .returnResult();

        assertEquals(HttpStatus.OK, response.getStatus());
        assertNotNull(response.getResponseBody());
        assertFalse(response.getResponseBody().isEmpty());
        assertEquals("user", response.getResponseBody().getFirst().getFirstName());
    }

    @Test
    void shouldReturnOkAndFindByUnemployedEntityByValidId() {
        var response = rest
                .get()
                .uri("{}/0196a2c6-3072-7902-873f-36a8cec90577", subUri)
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
                .uri("{}/0196a2c6-3072-7902-873f-bba8cec90577", subUri)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().isEmpty();
    }

    @Test
    void shouldReturnBadRequestOnInvalidId() {
        rest
                .get()
                .uri("{}/0196a2c6-3072-873f-bba8cec90577", subUri)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.error").isEqualTo("Bad Request")
                .returnResult();

    }

    @Test
    @DirtiesContext
    void shouldCreateNewEntityAndReturnUriOfCreatedEntity() {


        var newEntity = UnemployedEntity.builder()
                .firstName("test")
                .lastName("user")
                .email("t@t.t")
                .password("test")
                .employmentStatus(EmploymentStatus.UNEMPLOYED)
                .build();

        var response = rest
                .post()
                .uri(subUri)
                .bodyValue(newEntity)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UnemployedEntity.class)
                .returnResult();

        assertNotNull(response.getResponseBody());

        assertEquals("test", response.getResponseBody().getFirstName());

        assertEquals(EmploymentStatus.UNEMPLOYED, response.getResponseBody().getEmploymentStatus());

        var uri = response.getResponseHeaders().get("Location").getFirst();

        assertNotNull(uri);

        assertEquals(host + port + subUri + '/' + response.getResponseBody().getId(), uri);
    }

}
