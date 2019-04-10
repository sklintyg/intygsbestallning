package se.inera.intyg.intygsbestallning.web.service.bestallning;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.ReflectionUtils;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import se.inera.intyg.intygsbestallning.common.domain.Bestallning;
import se.inera.intyg.intygsbestallning.common.domain.BestallningStatus;
import se.inera.intyg.intygsbestallning.common.domain.Handlaggare;
import se.inera.intyg.intygsbestallning.common.domain.Invanare;
import se.inera.intyg.intygsbestallning.common.domain.Vardenhet;
import se.inera.intyg.intygsbestallning.common.dto.ListBestallningDirection;
import se.inera.intyg.intygsbestallning.common.dto.ListBestallningSortColumn;
import se.inera.intyg.intygsbestallning.common.dto.ListBestallningarQuery;
import se.inera.intyg.intygsbestallning.common.dto.ListBestallningarResult;
import se.inera.intyg.intygsbestallning.persistence.service.BestallningPersistenceService;
import se.inera.intyg.schemas.contract.Personnummer;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class ListBestallningServiceImplTest {

    @Mock
    private BestallningPersistenceService bestallningPersistenceService;

    @InjectMocks
    private ListBestallningServiceImpl listBestallningService;

    @Test
    void listAktuellaBestallningar() {

        var statusar = List.of(BestallningStatus.OLAST, BestallningStatus.LAST, BestallningStatus.ACCEPTERAD);
        var listBestallningarQuery = buildQuery(statusar);
        var mockedListBestallningarResult = createBestallningarList(statusar);

        when(bestallningPersistenceService.listBestallningar(listBestallningarQuery))
                .thenReturn(mockedListBestallningarResult);

        var actualResult = listBestallningService.listByQuery(listBestallningarQuery);

        verify(bestallningPersistenceService, times(1)).listBestallningar(listBestallningarQuery);
        Assertions.assertThat(actualResult).isEqualTo(mockedListBestallningarResult);
    }

    @Test
    void listKlarmarkeradeBestallningar() {

        var statusar = List.of(BestallningStatus.KLAR);
        var listBestallningarQuery = buildQuery(statusar);
        var mockedListBestallningarResult = createBestallningarList(statusar);

        when(bestallningPersistenceService.listBestallningar(listBestallningarQuery))
                .thenReturn(mockedListBestallningarResult);

        var actualResult = listBestallningService.listByQuery(listBestallningarQuery);

        verify(bestallningPersistenceService, times(1)).listBestallningar(listBestallningarQuery);
        Assertions.assertThat(actualResult).isEqualTo(mockedListBestallningarResult);
    }

    @Test
    void listAvvisadeBestallningar() {

        var statusar = List.of(BestallningStatus.AVVISAD, BestallningStatus.RADERAD);
        var listBestallningarQuery = buildQuery(statusar);
        var mockedListBestallningarResult = createBestallningarList(statusar);

        when(bestallningPersistenceService.listBestallningar(listBestallningarQuery))
                .thenReturn(mockedListBestallningarResult);

        var actualResult = listBestallningService.listByQuery(listBestallningarQuery);

        verify(bestallningPersistenceService, times(1)).listBestallningar(listBestallningarQuery);
        Assertions.assertThat(actualResult).isEqualTo(mockedListBestallningarResult);
    }

    private ListBestallningarQuery buildQuery(List<BestallningStatus> statusar) {
        return new ListBestallningarQuery(
                statusar,
                "hsaId",
                "orgNr",
                null,
                0,
                50,
                ListBestallningSortColumn.ID,
                ListBestallningDirection.ASC);
    }

    private ListBestallningarResult createBestallningarList(List<BestallningStatus> statusar) {
        var bestallningList = List.of(
                Bestallning.Factory.newBestallning(buildInvanare("19121212-1212"), buildHandlaggare(), "AF00213", 1.0, buildVardenhet()),
                Bestallning.Factory.newBestallning(buildInvanare("20121212-1212"), buildHandlaggare(), "AF00213", 1.0, buildVardenhet()),
                Bestallning.Factory.newBestallning(buildInvanare("19121212-1212"), buildHandlaggare(), "AF00213", 1.0, buildVardenhet()),
                Bestallning.Factory.newBestallning(buildInvanare("20121212-1212"), buildHandlaggare(), "AF00213", 1.0, buildVardenhet()),
                Bestallning.Factory.newBestallning(buildInvanare("19121212-1212"), buildHandlaggare(), "AF00213", 1.0, buildVardenhet()),
                Bestallning.Factory.newBestallning(buildInvanare("19121212-1212"), buildHandlaggare(), "AF00213", 1.0, buildVardenhet()),
                Bestallning.Factory.newBestallning(buildInvanare("19121212-1212"), buildHandlaggare(), "AF00213", 1.0, buildVardenhet())
        );

        var randomIndex = new Random().nextInt(statusar.size());
        var longId = new AtomicLong();

        for (var bestallning : bestallningList) {
            Field idField = ReflectionUtils.findField(Bestallning.class, "id");
            ReflectionUtils.makeAccessible(idField);
            ReflectionUtils.setField(idField, bestallning, longId.getAndIncrement());
            bestallning.setStatus(statusar.get(randomIndex));
        }

        return ListBestallningarResult.Factory.toDto(
                bestallningList,
                0,
                0,
                50,
                1,
                bestallningList.size(),
                bestallningList.size(),
                ListBestallningSortColumn.ID,
                ListBestallningDirection.ASC);
    }

    private Vardenhet buildVardenhet() {
        return Vardenhet.Factory.newVardenhet(
                "hsa", "vgHsa", "orgId", "namn", "epost@mail.se", "svar");
    }

    private Handlaggare buildHandlaggare() {
        return Handlaggare.Factory.newHandlaggare(
                "Handlaggaren", "073-123", "e@mail.com", "myndigheten", "kontor", "kostnadsställe", "adress", "12345", "Staden");
    }

    private Invanare buildInvanare(String personnummer) {
        return Invanare.Factory.newInvanare(
                Personnummer.createPersonnummer(personnummer).get(), "Fornamn", "Mellannanmn", "Efternamn", "Läge", false);
    }
}
