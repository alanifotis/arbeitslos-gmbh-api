package arbeitslos.gmbh.api;

import arbeitslos.gmbh.api.model.EmploymentStatus;
import arbeitslos.gmbh.api.model.UnemployedEntity;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.netty.http.client.HttpClient;
import io.netty.handler.ssl.SslContextBuilder;


import javax.net.ssl.SSLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WithMockUser
@AutoConfigureWebTestClient
class ApiApplicationTests {
    @Autowired
    private WebTestClient rest;
    @LocalServerPort
    private int port;
    private final String subUri = "/api/v1/unemployed";

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
        assertEquals("test", response.getResponseBody().getFirst().getFirstName());
    }

    @Test
    void shouldReturnOkAndFindByUnemployedEntityByValidId() {
        var response = rest
                .get()
                .uri(subUri + "/1388947d-89c8-434f-b85d-6eba7b20bef5")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UnemployedEntity.class)
                .returnResult();

        assertNotNull(response);
        assertNotNull(response.getResponseBody());
        assertEquals("test", response.getResponseBody().getFirstName());
        assertEquals(EmploymentStatus.FARMING, response.getResponseBody().getEmploymentStatus());
    }

    @Test
    void shouldReturnNotFoundByInexistentId() {
        rest
                .get()
                .uri(subUri + "/0116a2c6-3072-7902-873f-bba8cec90577")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void shouldReturnBadRequestOnInvalidId() {
        rest
                .get()
                .uri(subUri + "/0196a2c6-3072-873f-bba8cec90577")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.title").isEqualTo("JSON Decoding Error")
                .returnResult();

    }

    @Test
    @DirtiesContext
    void shouldCreateNewEntityAndReturnUriOfCreatedEntity() {


        var newEntity = UnemployedEntity.builder()
                .firstName("test")
                .lastName("user")
                .email("test@test.test")
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

        assertEquals(subUri + response.getResponseBody().getId(), uri);
    }

}
