package se.inera.intyg.intygsbestallning.web.service.bestallning;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.intygsbestallning.common.domain.Bestallning;
import se.inera.intyg.intygsbestallning.common.domain.BestallningStatus;
import se.inera.intyg.intygsbestallning.common.domain.BestallningSvar;
import se.inera.intyg.intygsbestallning.common.domain.Handlaggare;
import se.inera.intyg.intygsbestallning.common.domain.Invanare;
import se.inera.intyg.intygsbestallning.common.domain.Vardenhet;
import se.inera.intyg.intygsbestallning.common.dto.AvvisaBestallningRequest;
import se.inera.intyg.intygsbestallning.common.dto.RaderaBestallningRequest;
import se.inera.intyg.intygsbestallning.common.dto.SimpleBestallningRequest;
import se.inera.intyg.intygsbestallning.common.resolver.BestallningStatusResolver;
import se.inera.intyg.intygsbestallning.integration.client.RespondToOrderService;
import se.inera.intyg.intygsbestallning.persistence.service.BestallningPersistenceService;
import se.inera.intyg.intygsbestallning.web.pdl.LogEvent;
import se.inera.intyg.intygsbestallning.web.service.pdl.LogService;
import se.inera.intyg.schemas.contract.Personnummer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static se.inera.intyg.schemas.contract.Personnummer.createPersonnummer;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class RaderaBestallningServiceImplTest {
    private static final Long BESTALLNING_ID = 1L;
    private static final LocalDateTime ANKOMST_DATUM = LocalDateTime.now().minusDays(2L);
    private static final LocalDateTime AVSLUT_DATUM = LocalDateTime.now().plusDays(1L);

    @Mock
    private BestallningPersistenceService bestallningPersistenceService;

    @Mock
    private BestallningStatusResolver bestallningStatusResolver;

    @Mock
    private RespondToOrderService respondToOrderService;

    @Mock
    private LogService pdlLogService;

    @InjectMocks
    private RaderaBestallningServiceImpl raderaBestallningService;

    @BeforeEach
    void setup() {
        Mockito.when(bestallningPersistenceService.getBestallningByIdAndHsaIdAndOrgId(anyLong(), anyString(), anyString())).thenReturn(buildBestallning());
    }

    @Test
    void testRaderaBestallning() {
        RaderaBestallningRequest request = new RaderaBestallningRequest("1", "hsaId", "orgNr", BestallningSvar.RADERAT, "Kommentar");
        raderaBestallningService.raderaBestallning(request);

        verify(bestallningPersistenceService, times(1)).deleteBestallning(any(Bestallning.class));
        verify(pdlLogService, times(0)).log(any(Bestallning.class), Mockito.eq(LogEvent.BESTALLNING_AVVISAS));
        verify(respondToOrderService, times(1)).sendRespondToOrder(any(RaderaBestallningRequest.class));

    }

    private Invanare buildInvanare() {
        Personnummer personnummer = createPersonnummer("191212121212").get();
        return new Invanare(1L, personnummer, "Tolvan", "Mellan", "Tolvansson", "", false);
    }

    private Optional<Bestallning> buildBestallning() {
        return Optional.of(new Bestallning(
                BESTALLNING_ID,
                "typ",
                1.0,
                ANKOMST_DATUM,
                AVSLUT_DATUM,
                "",
                "",
                BestallningStatus.LAST,
                buildInvanare(),
                buildHandlaggare(),
                buildVardenhet(),
                "referens",
                Lists.newArrayList(),
                Lists.newArrayList()));
    }

    private Vardenhet buildVardenhet() {
        return new Vardenhet(1L, "hsa-id", "", "", "","", "");
    }

    private Handlaggare buildHandlaggare() {
        return new Handlaggare(1L, "", "", "", "", "", "", "", "", "");
    }
}
