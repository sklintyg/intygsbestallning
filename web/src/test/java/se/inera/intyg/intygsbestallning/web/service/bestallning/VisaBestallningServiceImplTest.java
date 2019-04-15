package se.inera.intyg.intygsbestallning.web.service.bestallning;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.kotlin.KotlinModule;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ReflectionUtils;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import se.inera.intyg.infra.integration.pu.model.Person;
import se.inera.intyg.intygsbestallning.common.domain.Bestallning;
import se.inera.intyg.intygsbestallning.common.domain.BestallningStatus;
import se.inera.intyg.intygsbestallning.common.domain.Handlaggare;
import se.inera.intyg.intygsbestallning.common.domain.Invanare;
import se.inera.intyg.intygsbestallning.common.domain.Vardenhet;
import se.inera.intyg.intygsbestallning.common.property.BestallningProperties;
import se.inera.intyg.intygsbestallning.common.resolver.BestallningStatusResolver;
import se.inera.intyg.intygsbestallning.common.service.bestallning.BestallningTextService;
import se.inera.intyg.intygsbestallning.common.service.notifiering.NotifieringSendService;
import se.inera.intyg.intygsbestallning.common.text.bestallning.BestallningTexter;
import se.inera.intyg.intygsbestallning.integration.pu.PatientService;
import se.inera.intyg.intygsbestallning.persistence.service.BestallningPersistenceService;
import se.inera.intyg.intygsbestallning.web.pdl.LogEvent;
import se.inera.intyg.intygsbestallning.web.service.pdl.LogService;
import se.inera.intyg.schemas.contract.Personnummer;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class VisaBestallningServiceImplTest {
    private static final String ORG_ID = "org_id";
    private static final Long ID_OLAST = 1L;
    private static final Long ID_LAST = 2L;
    private static final String HSA_ID = "hsa_id";
    private static final LocalDateTime ANKOMST_DATUM = LocalDateTime.now().minusDays(2L);
    private static final LocalDateTime AVSLUT_DATUM = LocalDateTime.now().plusDays(1L);
    private static BestallningTexter bestallningTexter;

    @Mock
    private BestallningPersistenceService bestallningPersistenceService;

    @Mock
    private BestallningStatusResolver bestallningStatusResolver;

    @Mock
    private LogService pdlLogService;

    @Mock
    private PatientService patientService;

    @Mock
    private BestallningTextService bestallningTextService;

    @Mock
    private BestallningProperties bestallningProperties;

    @Mock
    private NotifieringSendService notifieringSendService;

    @InjectMocks
    private VisaBestallningServiceImpl visaBestallningService;

    @BeforeAll
    static void parseBestallningTexter() throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.registerModule(new KotlinModule());
        bestallningTexter = xmlMapper.readValue(
                new ClassPathResource("/TEST_BESTALLNING_TEXT.xml").getFile(),
                BestallningTexter.class);
    }

    @BeforeEach
    void setup() throws NoSuchFieldException {

        var person = new Person(Personnummer.createPersonnummer("19121212-1212").get(),
                false, false, "Tolvan", "Tolvenius", "Tolvansson", "postaddress", "postnummer", "postOrt");

        ReflectionUtils.setField(BestallningProperties.class.getField("host"), bestallningProperties, "host-url");
        when(bestallningTextService.getBestallningTexter(any(Bestallning.class))).thenReturn(bestallningTexter);
        when(patientService.lookupPersonnummerFromPU(any(Personnummer.class))).thenReturn(Optional.of(person));
        when(notifieringSendService.vidarebefordrad(any(Bestallning.class))).thenReturn("bra mail-text");
    }

    @Test
    void testVisaBestallningWithOlastStatusChangesStatus() {
        when(bestallningPersistenceService.getBestallningByIdAndHsaIdAndOrgId(eq(ID_OLAST), anyString(), anyString()))
                .thenReturn(buildBestallning(ID_OLAST, BestallningStatus.OLAST));

        visaBestallningService.getBestallningByIdAndHsaIdAndOrgId(ID_OLAST, HSA_ID, ORG_ID);

        verify(bestallningStatusResolver, times(1)).setStatus(any(Bestallning.class));
        verify(pdlLogService, times(1)).log(any(Bestallning.class), eq(LogEvent.BESTALLNING_OPPNAS_OCH_LASES));

    }

    @Test
    void testVisaBestallningWithLastStatus() {
        when(bestallningPersistenceService.getBestallningByIdAndHsaIdAndOrgId(eq(ID_LAST), anyString(), anyString()))
                .thenReturn(buildBestallning(ID_LAST, BestallningStatus.LAST));

        visaBestallningService.getBestallningByIdAndHsaIdAndOrgId(ID_LAST, HSA_ID, ORG_ID);

        verify(pdlLogService, times(1)).log(any(Bestallning.class), eq(LogEvent.BESTALLNING_OPPNAS_OCH_LASES));

    }

    private Optional<Bestallning> buildBestallning(Long id, BestallningStatus status) {
        return Optional.of(new Bestallning(
                id,
                "typ",
                1.0,
                ANKOMST_DATUM,
                AVSLUT_DATUM,
                "",
                "",
                status,
                buildInvanare(),
                buildHandlaggare(),
                buildVardenhet(),
                "referens",
                Lists.newArrayList(),
                Lists.newArrayList())
        );
    }

    private Invanare buildInvanare() {
        return Invanare.Factory.newInvanare(Personnummer.createPersonnummer("19121212-1212").get(), null);
    }

    private Vardenhet buildVardenhet() {
        return Vardenhet.Factory.newVardenhet(
                "hsa",
                "vardGivareHsa",
                "orgId",
                "namn",
                "e@mail.se",
                "svar"
        );
    }

    private Handlaggare buildHandlaggare() {
        return Handlaggare.Factory.newHandlaggare(
                "namn",
                "073-000",
                "e@mail-se",
                "myndigheten",
                "kontor",
                "kostställe",
                "adressen",
                "postnr",
                "stad"
        );
    }

}
