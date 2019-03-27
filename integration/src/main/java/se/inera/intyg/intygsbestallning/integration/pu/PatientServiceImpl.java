package se.inera.intyg.intygsbestallning.integration.pu;

import io.vavr.control.Try;
import org.springframework.stereotype.Service;
import java.util.Optional;
import se.inera.intyg.infra.integration.pu.model.Person;
import se.inera.intyg.infra.integration.pu.model.PersonSvar;
import se.inera.intyg.infra.integration.pu.services.PUService;
import se.inera.intyg.intygsbestallning.common.domain.Invanare;
import se.inera.intyg.schemas.contract.Personnummer;

@Service
public class PatientServiceImpl implements PatientService {

    private PUService puService;

    public PatientServiceImpl(PUService puService) {
        this.puService = puService;
    }

    @Override
    public Optional<Person> lookupPersonnummerFromPU(Personnummer personnummer) {
        var personSvar = puLookup(personnummer);

        switch (personSvar.getStatus()) {
            case FOUND:
                return Optional.of(personSvar.getPerson());
            case NOT_FOUND:
                return Optional.empty();
            case ERROR:
            default:
                throw new RuntimeException("Could not get uppslag from PU"); //TODO: Throw better exception
        }
    }

    @Override
    public void updatePersonDetaljer(Invanare invanare) {
        var personSvar = puLookup(invanare.getPersonId());

        switch (personSvar.getStatus()) {
            case FOUND:
                var person = personSvar.getPerson();
                invanare.setFornamn(person.getFornamn());
                invanare.setEfternamn(person.getEfternamn());
                invanare.setMellannamn(person.getMellannamn());
                break;
            case NOT_FOUND:
                throw new RuntimeException("Could not find person with id: " + invanare.getPersonId() + " from PU"); //TODO: Throw better exception
            case ERROR:
            default:
                throw new RuntimeException("Could not get uppslag from PU"); //TODO: Throw better exception
        }
    }

    @Override
    public Optional<Boolean> isSekretessmarkerad(Personnummer personnummer) {
        var personSvar = puLookup(personnummer);

        switch (personSvar.getStatus()) {
            case FOUND:
                return Optional.of(personSvar.getPerson().isSekretessmarkering());
            case NOT_FOUND:
                return Optional.empty();
            case ERROR:
            default:
                throw new RuntimeException("Could not get uppslag from PU"); //TODO: Throw better exception
        }
    }

    private PersonSvar puLookup(Personnummer personnummer) {
        var personSvar = Try.of(() -> puService.getPerson(personnummer));
        if (personSvar.isFailure()) {
            throw new RuntimeException("Could not get uppslag from PU"); //TODO: Throw better exception
        }
        return personSvar.get();
    }
}
