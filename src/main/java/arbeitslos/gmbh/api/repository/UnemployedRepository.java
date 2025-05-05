package arbeitslos.gmbh.api.repository;

import arbeitslos.gmbh.api.model.UnemployedEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UnemployedRepository extends R2dbcRepository<UnemployedEntity, UUID> {
    Mono<UnemployedEntity> findByEmail(String email);
}
