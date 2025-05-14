package arbeitslos.gmbh.api.controller;

import arbeitslos.gmbh.api.model.UnemployedEntity;
import arbeitslos.gmbh.api.service.UnemployedService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/unemployed")
@CrossOrigin(
        allowedHeaders = "*",
        origins = "*"
)
@Slf4j
public class UnemployedController {
    private final UnemployedService _service;

    @GetMapping
    public ResponseEntity<Flux<UnemployedEntity>> findAll() {
        var entities = _service.findAll();
        var count = entities.count();
        return ResponseEntity.ok().eTag("" + count).body(entities);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<UnemployedEntity>> findById(@PathVariable UUID id) {
        return _service.findById(id)
                .map(entity -> ResponseEntity
                        .ok()
                        .eTag("\"" + id + "\"")
                        .body(entity))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/search{email}")
    public Mono<ResponseEntity<UnemployedEntity>> findById(@RequestParam String email) {
        return _service.findByEmail(email)
                .map(entity -> ResponseEntity
                        .ok()
                        .eTag("\"" + email + "\"")
                        .body(entity))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<UnemployedEntity>> save(@Valid @RequestBody UnemployedEntity entity, UriComponentsBuilder uriBuilder) {
        var result = _service.save(entity);
        return result.map(created -> {
            if (created != null) {
                var uri = uriBuilder
                        .path("/api/v1/unemployed/{id}")
                        .buildAndExpand(created.getId())
                        .toUri();
                return ResponseEntity.created(uri).body(created);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(entity);
        });

    }
}
