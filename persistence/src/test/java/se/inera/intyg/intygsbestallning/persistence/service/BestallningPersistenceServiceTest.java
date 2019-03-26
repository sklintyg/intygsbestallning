package se.inera.intyg.intygsbestallning.persistence.service;


import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import se.inera.intyg.intygsbestallning.common.domain.Bestallning;
import se.inera.intyg.intygsbestallning.persistence.TestContext;
import se.inera.intyg.intygsbestallning.persistence.TestSupport;
import se.inera.intyg.intygsbestallning.persistence.repository.BestallningRepository;


import static org.junit.jupiter.api.Assertions.assertEquals;


@TestContext
public class BestallningPersistenceServiceTest extends TestSupport {

    @Autowired
    BestallningRepository bestallningRepository;

    @Autowired
    BestallningPersistenceService bestallningPersistenceService;

    @BeforeEach
    public void before() {
        bestallningRepository.deleteAll();
    }

    @Disabled
    @Test
    public void createBestallningarTest() {
        final int num  = 5;
        randomizer()
                .objects(Bestallning.class, num)
                .forEach(bestallningPersistenceService::saveNewBestallning);
        var list = bestallningRepository.findAll();
        assertEquals(num, list.size());
    }
}
