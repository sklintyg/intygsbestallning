package se.inera.intyg.intygsbestallning.persistence.service;

import java.util.Optional;
import se.inera.intyg.intygsbestallning.common.domain.Invanare;
import se.inera.intyg.schemas.contract.Personnummer;

public interface InvanarePersistenceService {
    Invanare saveNewInvanare(Invanare invanare);
    Optional<Invanare> getInvanareByPersonnummer(Personnummer personnummer);
}
