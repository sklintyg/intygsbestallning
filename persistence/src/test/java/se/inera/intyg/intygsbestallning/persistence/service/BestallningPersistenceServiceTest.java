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
package se.inera.intyg.intygsbestallning.persistence.service;


import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import se.inera.intyg.intygsbestallning.common.domain.Bestallning;
import se.inera.intyg.intygsbestallning.common.domain.Handlaggare;
import se.inera.intyg.intygsbestallning.common.domain.Invanare;
import se.inera.intyg.intygsbestallning.common.domain.Vardenhet;
import se.inera.intyg.intygsbestallning.common.dto.ListBestallningDirection;
import se.inera.intyg.intygsbestallning.common.dto.ListBestallningSortColumn;
import se.inera.intyg.intygsbestallning.common.dto.ListBestallningarQuery;
import se.inera.intyg.intygsbestallning.common.exception.IbErrorCodeEnum;
import se.inera.intyg.intygsbestallning.common.exception.IbServiceException;
import se.inera.intyg.intygsbestallning.common.resolver.BestallningStatusResolver;
import se.inera.intyg.intygsbestallning.persistence.TestContext;
import se.inera.intyg.intygsbestallning.persistence.TestSupport;
import se.inera.intyg.intygsbestallning.persistence.entity.BestallningEntity;
import se.inera.intyg.intygsbestallning.persistence.repository.BestallningRepository;
import se.inera.intyg.schemas.contract.Personnummer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@TestContext
public class BestallningPersistenceServiceTest extends TestSupport {

    @Autowired
    BestallningRepository bestallningRepository;

    @Autowired
    BestallningPersistenceService bestallningPersistenceService;

    @Autowired
    BestallningStatusResolver bestallningStatusResolver;

    // must be multiple of 5
    private final int pageSize = 5;
    private final int total = 3 * pageSize;

    @BeforeEach
    public void before() {
        bestallningRepository.deleteAll();
        randomizer()
                .objects(Bestallning.class, total)
                .forEach(bestallningPersistenceService::saveNewBestallning);
    }

    @Test
    void createBestallningarTest() {
        var list = bestallningRepository.findAll();
        assertEquals(total, list.size());
        list.forEach(b -> {
            assertNotNull(b.getInvanare());
            assertNotNull(b.getVardenhet());
            assertFalse(b.getNotifieringar().isEmpty());
        });
    }

    @Test
    void textSearchTest() {
        var list = bestallningRepository.findAll();

        var entity = list.get(0);

        var query = search(entity, entity.getInvanare().getPersonId());
        var result = bestallningPersistenceService.listBestallningar(query);
        assertFalse(result.getSecond().isEmpty());

        query = search(entity, entity.getStatus().getBeskrivning().toLowerCase());
        result = bestallningPersistenceService.listBestallningar(query);
        assertFalse(result.getSecond().isEmpty());

        query = search(entity, entity.getAnkomstDatum().format(DateTimeFormatter.ISO_DATE));
        result = bestallningPersistenceService.listBestallningar(query);
        assertFalse(result.getSecond().isEmpty());
    }

    @Test
    void pageTest() {
        var query = Mockito.mock(ListBestallningarQuery.class);
        Mockito.when(query.getPageIndex()).thenReturn(0);
        Mockito.when(query.getLimit()).thenReturn(pageSize);
        Mockito.when(query.getSortColumn()).thenReturn(ListBestallningSortColumn.ID);
        Mockito.when(query.getSortDirection()).thenReturn(ListBestallningDirection.ASC);

        // page 0
        var result = bestallningPersistenceService.listBestallningar(query);
        assertEquals(pageSize, result.getSecond().size());
        assertEquals(0, result.getFirst().getPageIndex());
        assertEquals(pageSize, result.getFirst().getNumberOfElements());

        // page 1
        Mockito.when(query.getPageIndex()).thenReturn(1);
        result = bestallningPersistenceService.listBestallningar(query);
        assertEquals(pageSize, result.getSecond().size());
        assertEquals(1, result.getFirst().getPageIndex());
        assertEquals(total / pageSize, result.getFirst().getTotalPages());
        assertEquals(total, result.getFirst().getTotalElements());
    }

    @Test
    void sortTest() {
        var query = Mockito.mock(ListBestallningarQuery.class);
        Mockito.when(query.getPageIndex()).thenReturn(0);
        Mockito.when(query.getLimit()).thenReturn(pageSize);

        // bestallning id asc
        Mockito.when(query.getSortColumn()).thenReturn(ListBestallningSortColumn.ID);
        Mockito.when(query.getSortDirection()).thenReturn(ListBestallningDirection.ASC);
        var id1 = bestallningPersistenceService.listBestallningar(query)
                .getSecond()
                .stream()
                .map(Bestallning::getId)
                .collect(Collectors.toList());
        var id2 = Lists.newArrayList(id1);
        id2.sort(Long::compareTo);
        assertEquals(id2, id1);

        // person id desc
        Mockito.when(query.getSortColumn()).thenReturn(ListBestallningSortColumn.INVANARE_PERSON_ID);
        Mockito.when(query.getSortDirection()).thenReturn(ListBestallningDirection.DESC);

        var p1 = bestallningPersistenceService.listBestallningar(query)
                .getSecond()
                .stream()
                .map(b -> b.getInvanare().getPersonId().getPersonnummer())
                .collect(Collectors.toList());
        var p2 = Lists.newArrayList(p1);
        p2.sort(String::compareTo);
        assertEquals(Lists.reverse(p2), p1);
    }

    ListBestallningarQuery search(BestallningEntity entity, String text) {
        return new ListBestallningarQuery(Lists.newArrayList(), entity.getVardenhet().getHsaId(),
                entity.getVardenhet().getOrganisationId(), text,
                0, pageSize, ListBestallningSortColumn.ID, ListBestallningDirection.ASC);
    }

    @Test
    void getBestallningByIdAndHsaIdAndOrgIdTest() {
        var list = bestallningRepository.findAll();

        var entity = list.get(0);

        var domain = bestallningPersistenceService.getBestallningByIdAndHsaIdAndOrgId(entity.getId(),
                entity.getVardenhet().getHsaId(), entity.getVardenhet().getOrganisationId());
        assertTrue(domain.isPresent());

        domain = bestallningPersistenceService.getBestallningByIdAndHsaIdAndOrgId(-1L,
                null, null);
        assertTrue(domain.isEmpty());
    }

    @Test
    void getBestallningByIdAndHsaIdAndOrgIdTestVardenheterHsaIdMismatch() {
        var list = bestallningRepository.findAll();

        var entity = list.get(0);

        assertThatThrownBy(() -> bestallningPersistenceService.getBestallningByIdAndHsaIdAndOrgId(entity.getId(),
                "other-vardenhet", entity.getVardenhet().getOrganisationId()))
                .isExactlyInstanceOf(IbServiceException.class)
                .hasFieldOrPropertyWithValue("errorCode", IbErrorCodeEnum.BESTALLNING_FEL008_ATKOMST_NEKAD);
    }

    @Test
    void getBestallningByIdAndHsaIdAndOrgIdTestChangedOrgId() {
        var list = bestallningRepository.findAll();

        var entity = list.get(0);

        assertThatThrownBy(() -> bestallningPersistenceService.getBestallningByIdAndHsaIdAndOrgId(entity.getId(),
                entity.getVardenhet().getHsaId(), "other-org-nr"))
                .isExactlyInstanceOf(IbServiceException.class)
                .hasFieldOrPropertyWithValue("errorCode", IbErrorCodeEnum.VARDGIVARE_ORGNR_MISMATCH);
    }

    @Test
    void searchByPartialDateTest() {
        var bestallningList = createBestallningar();
        bestallningList.forEach(bestallningPersistenceService::saveNewBestallning);

        var dateString = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDate.now().atStartOfDay()).substring(0, 6); //yyyy-M
        var query = new ListBestallningarQuery(
                Lists.newArrayList(),
                "hsa",
                "orgId",
                dateString,
                0,
                50,
                ListBestallningSortColumn.ID,
                ListBestallningDirection.ASC
        );

         var result = bestallningPersistenceService.listBestallningar(query);

        assertThat(result.getSecond()).hasSize(2);

    }

    @Test
    void searchByPartialPersonnummerTest() {
        var bestallningList = createBestallningar();
        bestallningList.forEach(bestallningPersistenceService::saveNewBestallning);

        var partialPersonnummerString = "191212"; //19121212-1212
        var query = new ListBestallningarQuery(
                Lists.newArrayList(),
                "hsa",
                "orgId",
                partialPersonnummerString,
                0,
                50,
                ListBestallningSortColumn.ID,
                ListBestallningDirection.ASC
        );

        var result = bestallningPersistenceService.listBestallningar(query);

        assertThat(result.getSecond()).hasSize(1);
    }

    @Test
    void searchByPartialBestallningTypTest() {
        var bestallningList = createBestallningar();
        bestallningList.forEach(bestallningPersistenceService::saveNewBestallning);

        var partialPersonnummerString = "AF00"; //AF00213
        var query = new ListBestallningarQuery(
                Lists.newArrayList(),
                "hsa",
                "orgId",
                partialPersonnummerString,
                0,
                50,
                ListBestallningSortColumn.ID,
                ListBestallningDirection.ASC
        );

        var result = bestallningPersistenceService.listBestallningar(query);

        assertThat(result.getSecond()).hasSize(1);
    }

    @Test
    void searchByPartialStatusTest() {
        var bestallningList = createBestallningar();
        bestallningList.forEach(bestallningPersistenceService::saveNewBestallning);

        var partialStatusString = "oläs"; //OLÄST
        var query = new ListBestallningarQuery(
                Lists.newArrayList(),
                "hsa",
                "orgId",
                partialStatusString,
                0,
                50,
                ListBestallningSortColumn.ID,
                ListBestallningDirection.ASC
        );

        var result = bestallningPersistenceService.listBestallningar(query);

        assertThat(result.getSecond()).hasSize(2);
    }

    private List<Bestallning> createBestallningar() {

        var b1 = Bestallning.Factory.newBestallning(buildInvanare("19121212-1212"), "syfte", "insats", buildHandlaggare(), "F1.0_AF00213", "AF00213", "detta är beskrivningen", buildVardenhet(), "ref");
        var b2 = Bestallning.Factory.newBestallning(buildInvanare("20121212-1212"), "syfte", "insats", buildHandlaggare(), "TESTTYP", "TESTINTYGTYP", "detta är beskrivningen", buildVardenhet(), "ref");

        bestallningStatusResolver.setStatus(b1);
        bestallningStatusResolver.setStatus(b2);

        return List.of(b1, b2);

    }

    private Vardenhet buildVardenhet() {
        return Vardenhet.Factory.newVardenhet(
                "hsa", "vgHsa", "orgId", "namn", "epost@mail.se");
    }

    private Handlaggare buildHandlaggare() {
        return Handlaggare.Factory.newHandlaggare(
                "Handlaggaren", "073-123", "myndigheten", "kontor", "kostnadsställe", "adress", "12345", "Staden");
    }

    private Invanare buildInvanare(String personnummer) {
        return Invanare.Factory.newInvanare(
                Personnummer.createPersonnummer(personnummer).get(),"Läge");
    }
}
