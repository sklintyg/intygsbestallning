package se.inera.intyg.intygsbestallning.web.service.bestallning;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.inera.intyg.schemas.contract.Personnummer.createPersonnummer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import se.inera.intyg.infra.integration.hsa.model.Vardgivare;
import se.inera.intyg.infra.integration.hsa.services.HsaOrganizationsService;
import se.inera.intyg.infra.integration.pu.model.Person;
import se.inera.intyg.intygsbestallning.common.domain.Bestallning;
import se.inera.intyg.intygsbestallning.common.domain.Handlaggare;
import se.inera.intyg.intygsbestallning.common.domain.Invanare;
import se.inera.intyg.intygsbestallning.common.domain.Vardenhet;
import se.inera.intyg.intygsbestallning.common.dto.CreateBestallningRequest;
import se.inera.intyg.intygsbestallning.common.dto.CreateBestallningRequestHandlaggare;
import se.inera.intyg.intygsbestallning.common.dto.CreateBestallningRequestInvanare;
import se.inera.intyg.intygsbestallning.common.dto.CreateBestallningRequestKontor;
import se.inera.intyg.intygsbestallning.common.resolver.BestallningStatusResolver;
import se.inera.intyg.intygsbestallning.common.service.notifiering.NotifieringSendService;
import se.inera.intyg.intygsbestallning.integration.pu.PatientService;
import se.inera.intyg.intygsbestallning.persistence.service.BestallningPersistenceService;
import se.inera.intyg.intygsbestallning.persistence.service.InvanarePersistenceService;
import se.inera.intyg.intygsbestallning.persistence.service.VardenhetPersistenceService;
import se.inera.intyg.schemas.contract.Personnummer;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class CreateBestallningServiceImplTest {

    private static final String HSA_ID = "12345";
    private static final String ORGNUMMER = "orgnummer";

    @Mock
    private BestallningPersistenceService bestallningPersistenceService;

    @Mock
    private InvanarePersistenceService invanarePersistenceService;

    @Mock
    private BestallningStatusResolver bestallningStatusResolver;

    @Mock
    private VardenhetPersistenceService vardenhetPersistenceService;

    @Mock
    private NotifieringSendService notifieringSendService;

    @Mock
    private PatientService patientService;

    @Mock
    private HsaOrganizationsService hsaOrganizationsService;

    @InjectMocks
    private CreateBestallningServiceImpl createBestallningService;

    @BeforeEach
    void setup() {

        var vardgivare = new Vardgivare();
        vardgivare.setId("hsaId");
        vardgivare.setOrgId("orgId");

        when(patientService.lookupPersonnummerFromPU(any(Personnummer.class))).thenReturn(buildPerson());
        when(hsaOrganizationsService.getVardenhet(any(String.class))).thenReturn(buildVardenhetFromHsa());
        when(hsaOrganizationsService.getVardgivareOfVardenhet(any(String.class))).thenReturn(vardgivare);
        when(vardenhetPersistenceService.getVardenhetByHsaId(any(String.class))).thenReturn(buildVardenhet());
        when(bestallningPersistenceService.saveNewBestallning(any(Bestallning.class))).thenReturn(buildBestallning());
    }

    @Test
    void testCreateBestallning() {
        createBestallningService.create(buildBestallningRequest());
        verify(notifieringSendService, times(1)).nyBestallning(any(Bestallning.class));
    }

    private CreateBestallningRequest buildBestallningRequest() {
        return new CreateBestallningRequest(
                new CreateBestallningRequestInvanare(createPersonnummer("191212121212").get(), "Test"),
                new CreateBestallningRequestHandlaggare("", "", "", "",
                        new CreateBestallningRequestKontor("", "", "", "", "")),
                "syfte", "insatser", "intygsTyp", 1.0, "hsa-id", "referens");
    }

    private Optional<Person> buildPerson() {
        Person person = new Person(createPersonnummer("191212121212").get(),
                false, false, "Tolvan", "", "Tolvansson", "", "", "");
        return Optional.of(person);
    }


    private Optional<Vardenhet> buildVardenhet() {
        Vardenhet vardenhet = new Vardenhet(1L, HSA_ID, "vardgivare-hsaId", ORGNUMMER, "namn", "e@post.com", "standard");
        return Optional.of(vardenhet);
    }

    private Invanare buildInvanare() {
        Personnummer personnummer = createPersonnummer("191212121212").get();
        return new Invanare(1L, personnummer, "");
    }

    private Handlaggare buildHandlaggare() {
        return new Handlaggare(1L, "", "", "", "", "", "", "", "", "");
    }

    private Bestallning buildBestallning() {
        return Bestallning.Factory.newBestallning(buildInvanare(), "syfte", "insats", buildHandlaggare(), "intygsTyp", 1.0, buildVardenhet().get(), "ref");
    }

    private se.inera.intyg.infra.integration.hsa.model.Vardenhet buildVardenhetFromHsa() {
        se.inera.intyg.infra.integration.hsa.model.Vardenhet vardenhet =
                new se.inera.intyg.infra.integration.hsa.model.Vardenhet();
        vardenhet.setVardgivareHsaId(HSA_ID);
        vardenhet.setVardgivareOrgnr(ORGNUMMER);
        vardenhet.setId("vardenhetID");
        vardenhet.setNamn("vardenhetNamn");
        vardenhet.setArbetsplatskod("12345");
        vardenhet.setEpost("e@post.com");
        vardenhet.setPostadress("postadress");
        vardenhet.setPostnummer("12345");
        vardenhet.setPostort("postort");
        vardenhet.setTelefonnummer("1234556");
        return vardenhet;
    }

}
