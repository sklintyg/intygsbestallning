package se.inera.intyg.intygsbestallning.persistence.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;
import se.inera.intyg.intygsbestallning.common.domain.Bestallning;
import se.inera.intyg.intygsbestallning.common.dto.ListBestallningarQuery;
import se.inera.intyg.intygsbestallning.common.dto.ListBestallningarResult;
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

    @Override
    public ListBestallningarResult listBestallningar(ListBestallningarQuery query) {
        var pageRequest = PageRequest.of(query.getPageIndex(), query.getLimit());

        var pageResult = bestallningRepository.findByQuery(query.getStatusar(), pageRequest);

        var bestallningar = pageResult.get().map(BestallningEntity.Factory::toDomain)
                .collect(Collectors.toList());

        return new ListBestallningarResult(
                bestallningar,
                pageResult.getNumber(),
                pageResult.getTotalPages(),
                pageResult.getNumberOfElements(),
                pageResult.getTotalElements());
    }
}
