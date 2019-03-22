package se.inera.intyg.intygsbestallning.persistence.service;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import se.inera.intyg.intygsbestallning.persistence.PersistenceConfig;
import se.inera.intyg.intygsbestallning.persistence.repository.BestallningRepository;

@SpringJUnitConfig(PersistenceConfig.class)
@EnableAutoConfiguration
public class BestallningPersistenceServiceTest {

}
