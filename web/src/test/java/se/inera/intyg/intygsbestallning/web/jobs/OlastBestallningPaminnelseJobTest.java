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
package se.inera.intyg.intygsbestallning.web.jobs;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.kotlin.KotlinModule;

import se.inera.intyg.intygsbestallning.common.domain.Bestallning;
import se.inera.intyg.intygsbestallning.common.domain.BestallningStatus;
import se.inera.intyg.intygsbestallning.common.domain.Handlaggare;
import se.inera.intyg.intygsbestallning.common.domain.Invanare;
import se.inera.intyg.intygsbestallning.common.domain.Notifiering;
import se.inera.intyg.intygsbestallning.common.domain.NotifieringTyp;
import se.inera.intyg.intygsbestallning.common.domain.Vardenhet;
import se.inera.intyg.intygsbestallning.common.dto.ListBestallningarBasedOnStatusQuery;
import se.inera.intyg.intygsbestallning.common.service.bestallning.BestallningTextService;
import se.inera.intyg.intygsbestallning.common.service.notifiering.NotifieringSendService;
import se.inera.intyg.intygsbestallning.common.text.bestallning.BestallningTexter;
import se.inera.intyg.intygsbestallning.persistence.entity.BestallningEntity;
import se.inera.intyg.intygsbestallning.persistence.service.BestallningPersistenceService;
import se.inera.intyg.schemas.contract.Personnummer;

@ExtendWith(MockitoExtension.class)
class OlastBestallningPaminnelseJobTest {

    private static BestallningTexter bestallningTexter;

    @Mock
    private NotifieringSendService notifieringSendService;

    @Mock
    private BestallningPersistenceService bestallningPersistenceService;

    @Mock
    private BestallningTextService bestallningTextService;

    @InjectMocks
    private OlastBestallningPaminnelseJob job;

    @BeforeAll
    static void parseBestallningTexter() throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.registerModule(new KotlinModule());
        bestallningTexter = xmlMapper.readValue(
                new ClassPathResource("/TEST_BESTALLNING_TEXT.xml").getFile(),
                BestallningTexter.class);
    }

    @BeforeEach
    void setup() {
        when(bestallningTextService.getBestallningTexter(any(Bestallning.class))).thenReturn(bestallningTexter);
    }

    @Test
    void testOlastBestallingPaminnelse1SentCorrectly() {

        var bestallning = buildBestallning(8L, false);

        when(bestallningPersistenceService.updateBestallning(any(Bestallning.class))).thenReturn(bestallning.get(0));
        when(bestallningPersistenceService.listBestallningarBasedOnStatus(any(ListBestallningarBasedOnStatusQuery.class)))
                .thenReturn(buildBestallning(8L, false));

        job.checkBestallningar();

        verify(bestallningPersistenceService, times(1)).listBestallningarBasedOnStatus(any(ListBestallningarBasedOnStatusQuery.class));
        verify(bestallningPersistenceService, times(2)).updateBestallning(any(Bestallning.class));
        verify(notifieringSendService, times(1)).paminnelse(any(Bestallning.class), eq(NotifieringTyp.NY_BESTALLNING_PAMINNELSE_1));
    }

    @Test
    void testOlastBestallingPaminnelse2SentCorrectly() {

        var bestallning = buildBestallning(15L, true);

        when(bestallningPersistenceService.updateBestallning(any(Bestallning.class))).thenReturn(bestallning.get(0));
        when(bestallningPersistenceService.listBestallningarBasedOnStatus(any(ListBestallningarBasedOnStatusQuery.class)))
                .thenReturn(bestallning);

        job.checkBestallningar();

        verify(bestallningPersistenceService, times(1)).listBestallningarBasedOnStatus(any(ListBestallningarBasedOnStatusQuery.class));
        verify(bestallningPersistenceService, times(2)).updateBestallning(any(Bestallning.class));
        verify(notifieringSendService, times(1)).paminnelse(any(Bestallning.class), eq(NotifieringTyp.NY_BESTALLNING_PAMINNELSE_2));
    }

    @Test
    void testOnlyPaminnelse1SentAfterTwoWeeksSinceNoPriorPaminnelseSent() {
        var bestallning = buildBestallning(15L, false);
        when(bestallningPersistenceService.updateBestallning(any(Bestallning.class))).thenReturn(bestallning.get(0));
        when(bestallningPersistenceService.listBestallningarBasedOnStatus(any(ListBestallningarBasedOnStatusQuery.class)))
                .thenReturn(bestallning);

        job.checkBestallningar();

        verify(bestallningPersistenceService, times(1)).listBestallningarBasedOnStatus(any(ListBestallningarBasedOnStatusQuery.class));
        verify(bestallningPersistenceService, times(2)).updateBestallning(any(Bestallning.class));
        verify(notifieringSendService, times(1)).paminnelse(any(Bestallning.class), eq(NotifieringTyp.NY_BESTALLNING_PAMINNELSE_1));
        verify(notifieringSendService, times(0)).paminnelse(any(Bestallning.class), eq(NotifieringTyp.NY_BESTALLNING_PAMINNELSE_2));
    }

    @Test
    void testBorderValuesForPaminnelse1() {
        var bestallning = buildBestallning(7L, false);

        when(bestallningPersistenceService.listBestallningarBasedOnStatus(any(ListBestallningarBasedOnStatusQuery.class)))
                .thenReturn(bestallning);

        job.checkBestallningar();

        verify(bestallningPersistenceService, times(1)).listBestallningarBasedOnStatus(any(ListBestallningarBasedOnStatusQuery.class));
        verify(bestallningPersistenceService, times(0)).updateBestallning(any(Bestallning.class));
        verify(notifieringSendService, times(0)).paminnelse(any(Bestallning.class), eq(NotifieringTyp.NY_BESTALLNING_PAMINNELSE_1));
    }

    @Test
    void testBorderValuesForPaminnelse2() {

        var bestallning = buildBestallning(14L, true);

        when(bestallningPersistenceService.listBestallningarBasedOnStatus(any(ListBestallningarBasedOnStatusQuery.class)))
                .thenReturn(bestallning);

        job.checkBestallningar();

        verify(bestallningPersistenceService, times(1)).listBestallningarBasedOnStatus(any(ListBestallningarBasedOnStatusQuery.class));
        verify(bestallningPersistenceService, times(0)).updateBestallning(any(Bestallning.class));
        verify(notifieringSendService, times(0)).paminnelse(any(Bestallning.class), eq(NotifieringTyp.NY_BESTALLNING_PAMINNELSE_2));
    }

    private List<Bestallning> buildBestallning(long daysAgo, boolean paminnelse2) {
        List<Notifiering> notifieringar = Lists.newArrayList(Notifiering.Factory.nyBestallning("hsaId"));
        if (paminnelse2) {
            notifieringar.add(Notifiering.Factory.paminnelse("hsaId", NotifieringTyp.NY_BESTALLNING_PAMINNELSE_1));
        }

        return Lists.newArrayList(new Bestallning(1L, "typ", "AF00213",
                "detta är beskrivningen", LocalDate.now().atStartOfDay().minusDays(daysAgo), LocalDateTime.now().plusDays(1L),
                "syfte", "planeradeAktiviteter", BestallningStatus.OLAST,
                Invanare.Factory.newInvanare(Personnummer.createPersonnummer("191212121212").get(), ""),
                Handlaggare.Factory.newHandlaggare("", "", "", "", "", "", "", ""),
                Vardenhet.Factory.newVardenhet("hsaId", "vardgivareHsaId", "orgNr", "vardenhet", "a@b.c"),
                "ärendeReferens",
                null,
                notifieringar));
    }
}
