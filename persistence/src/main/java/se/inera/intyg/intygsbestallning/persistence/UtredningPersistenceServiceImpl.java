package se.inera.intyg.intygsbestallning.persistence;

import org.springframework.stereotype.Service;
import se.inera.intyg.intygsbestallning.common.Utredning;

@Service
public class UtredningPersistenceServiceImpl implements UtredningPersistenceService {

    private final UtredningRepository utredningRepository;

    public UtredningPersistenceServiceImpl(UtredningRepository utredningRepository) {
        this.utredningRepository = utredningRepository;
    }

    @Override
    public Utredning saveNewUtredning(Utredning utredning) {

        var utredningEntity = UtredningEntityKt.toEntity(utredning);

        return UtredningEntityKt.toDomain(utredningRepository.save(utredningEntity));
    }
}
