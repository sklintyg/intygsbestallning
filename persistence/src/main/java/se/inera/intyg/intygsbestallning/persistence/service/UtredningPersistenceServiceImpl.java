package se.inera.intyg.intygsbestallning.persistence.service;

import org.springframework.stereotype.Service;
import se.inera.intyg.intygsbestallning.common.Utredning;
import se.inera.intyg.intygsbestallning.persistence.entity.UtredningEntity;
import se.inera.intyg.intygsbestallning.persistence.repository.UtredningRepository;

@Service
public class UtredningPersistenceServiceImpl implements UtredningPersistenceService {

    private final UtredningRepository utredningRepository;

    public UtredningPersistenceServiceImpl(UtredningRepository utredningRepository) {
        this.utredningRepository = utredningRepository;
    }

    @Override
    public Utredning saveNewUtredning(Utredning utredning) {

        var utredningEntity = UtredningEntity.Factory.toEntity(utredning);

        return UtredningEntity.Factory.toDomain(utredningRepository.save(utredningEntity));
    }
}
