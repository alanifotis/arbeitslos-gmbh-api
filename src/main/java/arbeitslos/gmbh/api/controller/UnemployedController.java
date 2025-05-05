package arbeitslos.gmbh.api.controller;

import arbeitslos.gmbh.api.model.UnemployedEntity;
import arbeitslos.gmbh.api.service.UnemployedService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("/unemployed")
public class UnemployedController {
    private final UnemployedService _service;

    @GetMapping
    public Flux<UnemployedEntity> findAll() {
        return _service.findAll();
    }
}
