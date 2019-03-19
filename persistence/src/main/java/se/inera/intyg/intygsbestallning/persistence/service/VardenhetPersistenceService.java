package se.inera.intyg.intygsbestallning.persistence.service;

import java.util.Optional;
import se.inera.intyg.intygsbestallning.common.domain.Vardenhet;

public interface VardenhetPersistenceService {
    Vardenhet saveNewVardenhet(Vardenhet vardenhet);
    Optional<Vardenhet> getVardenhetByHsaId(String hsaId);

}
