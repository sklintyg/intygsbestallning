package se.inera.intyg.intygsbestallning.persistence.service;

import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.Optional;
import se.inera.intyg.intygsbestallning.common.domain.Invanare;
import se.inera.intyg.intygsbestallning.persistence.entity.InvanareEntity;
import se.inera.intyg.intygsbestallning.persistence.repository.InvanareRepository;
import se.inera.intyg.schemas.contract.Personnummer;

@Service
@Transactional
public class InvanarePersistenceServiceImpl implements InvanarePersistenceService {

    private InvanareRepository invanareRepository;

    public InvanarePersistenceServiceImpl(InvanareRepository invanareRepository) {
        this.invanareRepository = invanareRepository;
    }

    @Override
    public Invanare saveNewInvanare(Invanare invanare) {
        var invanareEntity = InvanareEntity.Factory.toEntity(invanare);
        invanareRepository.save(invanareEntity);
        return InvanareEntity.Factory.toDomain(invanareEntity);
    }

    @Override
    public Optional<Invanare> getInvanareByPersonnummer(Personnummer personnummer) {
        return invanareRepository.findByPersonId(personnummer.getPersonnummerWithDash())
                .map(InvanareEntity.Factory::toDomain);
    }
}
