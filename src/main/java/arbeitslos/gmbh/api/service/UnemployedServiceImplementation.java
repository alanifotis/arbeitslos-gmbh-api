package arbeitslos.gmbh.api.service;

import arbeitslos.gmbh.api.model.UnemployedEntity;
import arbeitslos.gmbh.api.repository.UnemployedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class UnemployedServiceImplementation implements UnemployedService{
    private final UnemployedRepository _repository;
    @Override
    public Flux<UnemployedEntity> findAll() {
        return _repository.findAll();
    }
}
