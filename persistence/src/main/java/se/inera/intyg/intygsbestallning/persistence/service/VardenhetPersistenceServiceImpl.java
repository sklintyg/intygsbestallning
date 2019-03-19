package se.inera.intyg.intygsbestallning.persistence.service;

import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.Optional;
import se.inera.intyg.intygsbestallning.common.domain.Vardenhet;
import se.inera.intyg.intygsbestallning.persistence.entity.VardenhetEntity;
import se.inera.intyg.intygsbestallning.persistence.repository.VardenhetRepository;

@Service
@Transactional
public class VardenhetPersistenceServiceImpl implements VardenhetPersistenceService {

    private final VardenhetRepository vardenhetRepository;

    public VardenhetPersistenceServiceImpl(VardenhetRepository vardenhetRepository) {
        this.vardenhetRepository = vardenhetRepository;
    }

    @Override
    public Vardenhet saveNewVardenhet(Vardenhet vardenhet) {

        if (vardenhetRepository.existsByHsaId(vardenhet.getHsaId())) {
            throw new IllegalArgumentException("Vardenhet with hsaId: " + vardenhet.getHsaId() + " already exists");
        }

        var vardenhetEntity = VardenhetEntity.Factory.toEntity(vardenhet);
        vardenhetRepository.save(vardenhetEntity);

        return VardenhetEntity.Factory.toDomain(vardenhetEntity);
    }

    @Override
    public Optional<Vardenhet> getVardenhetByHsaId(String hsaId) {
        return vardenhetRepository.findByHsaId(hsaId)
                .map(VardenhetEntity.Factory::toDomain);
    }
}
