package arbeitslos.gmbh.api.controller;

import arbeitslos.gmbh.api.model.UnemployedEntity;
import arbeitslos.gmbh.api.service.UnemployedService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
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
public class UnemployedController {
    private final UnemployedService _service;

    @GetMapping
    public Flux<UnemployedEntity> findAll() {
        return _service.findAll();
    }

    @GetMapping("/{id}")
    public Mono<UnemployedEntity> findById(@PathVariable UUID id) {
        return _service.findById(id);
    }

}
