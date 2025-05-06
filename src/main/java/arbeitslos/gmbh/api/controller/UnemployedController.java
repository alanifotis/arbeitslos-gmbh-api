package arbeitslos.gmbh.api.controller;

import arbeitslos.gmbh.api.model.UnemployedEntity;
import arbeitslos.gmbh.api.service.UnemployedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/unemployed")
@CrossOrigin(
        allowedHeaders = "*",
        origins = "*"
)
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

}
