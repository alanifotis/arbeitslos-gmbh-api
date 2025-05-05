package arbeitslos.gmbh.api.service;

import arbeitslos.gmbh.api.model.UnemployedEntity;
import reactor.core.publisher.Flux;

public interface UnemployedService {
    public Flux<UnemployedEntity> findAll();
}
