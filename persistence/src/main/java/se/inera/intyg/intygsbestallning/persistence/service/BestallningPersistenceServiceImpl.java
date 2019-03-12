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
    public Bestallning saveNewBestallning(Bestallning bestallning) {

        var bestallningEntity = BestallningEntity.Factory.toEntity(bestallning);

        return BestallningEntity.Factory.toDomain(bestallningRepository.save(bestallningEntity));
    }
}
