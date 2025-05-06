package arbeitslos.gmbh.api.service;

import arbeitslos.gmbh.api.model.UnemployedEntity;
import arbeitslos.gmbh.api.repository.UnemployedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UnemployedServiceImplementation implements UnemployedService{
    private final UnemployedRepository _repository;
    @Override
    public Flux<UnemployedEntity> findAll() {
        return _repository.findAll();
    }

    @Override
    public Mono<UnemployedEntity> findById(UUID id) {
        return _repository.findById(id);
    }

}
