package se.inera.intyg.intygsbestallning.persistence.service;


import java.util.stream.Collectors;
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
        bestallningRepository.saveAll(
                random.objects(BestallningEntity.class, 2)
                        .collect(Collectors.toList())
        );
        var list = bestallningRepository.findAll();
        assertEquals(2, list.size());
    }


}
