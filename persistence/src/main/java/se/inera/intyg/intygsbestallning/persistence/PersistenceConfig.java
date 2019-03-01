package se.inera.intyg.intygsbestallning.persistence;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = { "se.inera.intyg.intygsbestallning.persistence" })
@EnableJpaRepositories(basePackages = { "se.inera.intyg.intygsbestallning.persistence" })
public class PersistenceConfig {

}
