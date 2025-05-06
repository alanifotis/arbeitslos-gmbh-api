package arbeitslos.gmbh.api.service;

import arbeitslos.gmbh.api.model.UnemployedEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UnemployedService {
    public Flux<UnemployedEntity> findAll();

    Mono<UnemployedEntity> findById(UUID id);
}
