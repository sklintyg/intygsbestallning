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

import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.intygsbestallning.common.domain.Bestallning;
import se.inera.intyg.intygsbestallning.common.domain.BestallningEvent;
import se.inera.intyg.intygsbestallning.common.domain.BestallningStatus;
import se.inera.intyg.intygsbestallning.common.domain.BestallningSvar;
import se.inera.intyg.intygsbestallning.common.domain.Handelse;
import se.inera.intyg.intygsbestallning.common.domain.Handlaggare;
import se.inera.intyg.intygsbestallning.common.domain.Invanare;
import se.inera.intyg.intygsbestallning.common.domain.Vardenhet;
import se.inera.intyg.intygsbestallning.common.dto.RaderaBestallningRequest;
import se.inera.intyg.intygsbestallning.common.resolver.BestallningStatusResolver;
import se.inera.intyg.intygsbestallning.integration.client.RespondToOrderService;
import se.inera.intyg.intygsbestallning.persistence.service.BestallningPersistenceService;
import se.inera.intyg.intygsbestallning.web.pdl.LogEvent;
import se.inera.intyg.intygsbestallning.web.service.pdl.LogService;
import se.inera.intyg.schemas.contract.Personnummer;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static se.inera.intyg.schemas.contract.Personnummer.createPersonnummer;

@ExtendWith(MockitoExtension.class)
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

    private Optional<Bestallning> bestallning;

    @BeforeEach
    void setup() {
        bestallning = buildBestallning();
        Mockito.when(bestallningPersistenceService.getBestallningByIdAndHsaIdAndOrgId(anyLong(), anyString(), anyString())).thenReturn(bestallning);
    }

    @Test
    void testRaderaBestallning() {
        RaderaBestallningRequest request = new RaderaBestallningRequest("user1","1", "hsaId", "orgNr", BestallningSvar.RADERAT, "Kommentar");
        raderaBestallningService.raderaBestallning(request);

        verify(bestallningPersistenceService, times(1)).deleteBestallning(any(Bestallning.class));
        verify(pdlLogService, times(0)).log(any(Bestallning.class), Mockito.eq(LogEvent.BESTALLNING_AVVISAS));
        verify(respondToOrderService, times(1)).sendRespondToOrder(any(RaderaBestallningRequest.class));

        assertEquals(1, bestallning.get().getHandelser().size());
        Handelse handelse = bestallning.get().getHandelser().get(0);
        assertEquals(BestallningEvent.AVVISA_RADERA, handelse.getEvent());
        assertEquals("user1", handelse.getAnvandare());
        assertEquals("Kommentar", handelse.getKommentar());

    }

    private Invanare buildInvanare() {
        Personnummer personnummer = createPersonnummer("191212121212").get();
        return new Invanare(personnummer, "");
    }

    private Optional<Bestallning> buildBestallning() {
        return Optional.of(new Bestallning(
                BESTALLNING_ID,
                "typ",
                "AF00213",
                "detta är beskrivningen",
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
        return new Vardenhet("hsa-id", "", "", "","");
    }

    private Handlaggare buildHandlaggare() {
        return new Handlaggare(1L, "", "", "", "", "", "", "", "", "");
    }
}
