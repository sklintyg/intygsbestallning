package se.inera.intyg.intygsbestallning.web;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = "se.inera.intyg.intygsbestallning.web")
@PropertySource("classpath:application-test.yaml")
public class WebTestConfig {

}
