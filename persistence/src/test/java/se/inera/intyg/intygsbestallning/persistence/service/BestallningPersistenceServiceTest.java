package se.inera.intyg.intygsbestallning.persistence.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import se.inera.intyg.intygsbestallning.persistence.entity.BestallningEntity;
import se.inera.intyg.intygsbestallning.persistence.repository.BestallningRepository;


import static org.junit.jupiter.api.Assertions.assertEquals;


@TestContext
public class BestallningPersistenceServiceTest extends TestSupport {

    @Autowired
    BestallningRepository bestallningRepository;

    @BeforeEach
    public void before() {
        bestallningRepository.deleteAll();
    }

    @Test
    public void findTest() {
        var e1 = bestallningRepository.save(randomize(BestallningEntity.class));
        var e2 = bestallningRepository.save(randomize(BestallningEntity.class));
        var list = bestallningRepository.findAll();
        assertEquals(2, list.size());
    }



}
