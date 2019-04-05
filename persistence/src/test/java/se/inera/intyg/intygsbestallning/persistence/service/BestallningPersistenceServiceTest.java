package se.inera.intyg.intygsbestallning.persistence.service;


import java.time.format.DateTimeFormatter;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import se.inera.intyg.intygsbestallning.common.domain.Bestallning;
import se.inera.intyg.intygsbestallning.common.dto.ListBestallningDirection;
import se.inera.intyg.intygsbestallning.common.dto.ListBestallningSortColumn;
import se.inera.intyg.intygsbestallning.common.dto.ListBestallningarQuery;
import se.inera.intyg.intygsbestallning.persistence.TestContext;
import se.inera.intyg.intygsbestallning.persistence.TestSupport;
import se.inera.intyg.intygsbestallning.persistence.entity.BestallningEntity;
import se.inera.intyg.intygsbestallning.persistence.repository.BestallningRepository;


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

    // must be multiple of 5
    final int pageSize = 5;
    final int total = 3 * pageSize;

    @BeforeEach
    public void before() {
        bestallningRepository.deleteAll();
        randomizer()
                .objects(Bestallning.class, total)
                .forEach(bestallningPersistenceService::saveNewBestallning);
    }

    @Test
    public void createBestallningarTest() {
        var list = bestallningRepository.findAll();
        assertEquals(total, list.size());
        list.forEach(b -> {
            assertNotNull(b.getInvanare());
            assertNotNull(b.getVardenhet());
            assertFalse(b.getNotifieringar().isEmpty());
        });
    }

    @Test
    public void textSearchTest() {
        var list = bestallningRepository.findAll();

        var entity = list.get(0);

        var query = search(entity, entity.getInvanare().getPersonId());
        var result = bestallningPersistenceService.listBestallningar(query);
        assertFalse(result.getBestallningar().isEmpty());

        query = search(entity, String.valueOf(entity.getStatus().getBeskrivning().toLowerCase()));
        result = bestallningPersistenceService.listBestallningar(query);
        assertFalse(result.getBestallningar().isEmpty());

        query = search(entity, entity.getAnkomstDatum().format(DateTimeFormatter.ISO_DATE));
        result = bestallningPersistenceService.listBestallningar(query);
        assertFalse(result.getBestallningar().isEmpty());
    }

    @Test
    public void pageTest() {
        var query = Mockito.mock(ListBestallningarQuery.class);
        Mockito.when(query.getPageIndex()).thenReturn(0);
        Mockito.when(query.getLimit()).thenReturn(pageSize);
        Mockito.when(query.getSortColumn()).thenReturn(ListBestallningSortColumn.ID);
        Mockito.when(query.getSortDirection()).thenReturn(ListBestallningDirection.ASC);

        // page 0
        var result = bestallningPersistenceService.listBestallningar(query);
        assertEquals(pageSize, result.getBestallningar().size());
        assertEquals(0, result.getPageIndex());
        assertEquals(pageSize, result.getNumberOfElements());

        // page 1
        Mockito.when(query.getPageIndex()).thenReturn(1);
        result = bestallningPersistenceService.listBestallningar(query);
        assertEquals(pageSize, result.getBestallningar().size());
        assertEquals(1, result.getPageIndex());
        assertEquals(total / pageSize, result.getTotalPages());
        assertEquals(total, result.getTotalElements());
    }

    ListBestallningarQuery search(BestallningEntity entity, String text) {
        return new ListBestallningarQuery(Collections.EMPTY_LIST, entity.getVardenhet().getHsaId(),
                entity.getVardenhet().getOrganisationId(), text,
                0, pageSize, ListBestallningSortColumn.ID, ListBestallningDirection.ASC);
    }

    @Test
    public void getBestallningByIdAndHsaIdAndOrgIdTest() {
        var list = bestallningRepository.findAll();

        var entity = list.get(0);

        var domain = bestallningPersistenceService.getBestallningByIdAndHsaIdAndOrgId(entity.getId(),
                entity.getVardenhet().getHsaId(), entity.getVardenhet().getOrganisationId());
        assertTrue(domain.isPresent());

        domain = bestallningPersistenceService.getBestallningByIdAndHsaIdAndOrgId(-1L,
                null, null);
        assertTrue(domain.isEmpty());

        domain = bestallningPersistenceService.getBestallningByIdAndHsaIdAndOrgId(entity.getId(),
                null, null);
        assertTrue(domain.isPresent());
    }

}
