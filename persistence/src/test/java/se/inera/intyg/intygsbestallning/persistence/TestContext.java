package se.inera.intyg.intygsbestallning.persistence;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.transaction.Transactional;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import se.inera.intyg.intygsbestallning.common.CommonConfig;

@SpringJUnitConfig(value = { PersistenceConfig.class, CommonConfig.class })
@ActiveProfiles("test")
@EnableAutoConfiguration
@TestPropertySource("classpath:test.properties")
@Transactional
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface TestContext {
}
