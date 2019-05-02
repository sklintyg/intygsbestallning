/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
                Bestallning.Factory.newBestallning(buildInvanare("19121212-1212"), "syfte", "insatser", buildHandlaggare(), "AF00213", 1.0, buildVardenhet(), "ref"),
                Bestallning.Factory.newBestallning(buildInvanare("20121212-1212"), "syfte", "insatser", buildHandlaggare(), "AF00213", 1.0, buildVardenhet(), "ref"),
                Bestallning.Factory.newBestallning(buildInvanare("19121212-1212"), "syfte", "insatser", buildHandlaggare(), "AF00213", 1.0, buildVardenhet(), "ref"),
                Bestallning.Factory.newBestallning(buildInvanare("20121212-1212"), "syfte", "insatser", buildHandlaggare(), "AF00213", 1.0, buildVardenhet(), "ref"),
                Bestallning.Factory.newBestallning(buildInvanare("19121212-1212"), "syfte", "insatser", buildHandlaggare(), "AF00213", 1.0, buildVardenhet(), "ref"),
                Bestallning.Factory.newBestallning(buildInvanare("19121212-1212"), "syfte", "insatser", buildHandlaggare(), "AF00213", 1.0, buildVardenhet(), "ref"),
                Bestallning.Factory.newBestallning(buildInvanare("19121212-1212"), "syfte", "insatser", buildHandlaggare(), "AF00213", 1.0, buildVardenhet(), "ref")
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
                false,
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
                Personnummer.createPersonnummer(personnummer).get(),"Läge");
    }
}
