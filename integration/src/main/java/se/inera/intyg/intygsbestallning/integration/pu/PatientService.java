package se.inera.intyg.intygsbestallning.integration.pu;

import java.util.Optional;
import se.inera.intyg.infra.integration.pu.model.Person;
import se.inera.intyg.intygsbestallning.common.domain.Invanare;
import se.inera.intyg.schemas.contract.Personnummer;


public interface PatientService {
    Optional<Person> lookupPersonnummerFromPU(Personnummer personnummer);

    Optional<Boolean> isSekretessmarkerad(Personnummer personnummer);
}
