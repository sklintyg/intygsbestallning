package se.inera.intyg.intygsbestallning.persistence.service;

import org.springframework.stereotype.Service;
import se.inera.intyg.intygsbestallning.common.domain.Bestallning;
import se.inera.intyg.intygsbestallning.persistence.entity.BestallningEntity;
import se.inera.intyg.intygsbestallning.persistence.repository.BestallningRepository;

@Service
public class BestallningPersistenceServiceImpl implements BestallningPersistenceService {

    private final BestallningRepository bestallningRepository;

    public BestallningPersistenceServiceImpl(BestallningRepository bestallningRepository) {
        this.bestallningRepository = bestallningRepository;
    }

    @Override
    public Bestallning saveNewBestallning(Bestallning utredning) {

        var utredningEntity = BestallningEntity.Factory.toEntity(utredning);

        return BestallningEntity.Factory.toDomain(bestallningRepository.save(utredningEntity));
    }
}
