package arbeitslos.gmbh.api.service;

import arbeitslos.gmbh.api.model.EmploymentStatus;
import arbeitslos.gmbh.api.model.UnemployedEntity;
import arbeitslos.gmbh.api.repository.UnemployedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @Override
    public Mono<UnemployedEntity> save(UnemployedEntity entity) {
        return _repository.save(UnemployedEntity.builder()
                .email(entity.getEmail())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .password(entity.getPassword())
                .employmentStatus(entity.getEmploymentStatus())
                .build()
        );
    }

}
