package se.inera.intyg.intygsbestallning.persistence.testdata;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import se.inera.intyg.intygsbestallning.common.domain.Vardenhet;
import se.inera.intyg.intygsbestallning.common.property.PersistenceProperties;
import se.inera.intyg.intygsbestallning.common.service.notifiering.NotifieringSendService;
import se.inera.intyg.intygsbestallning.persistence.service.BestallningPersistenceService;

@Component
@Profile({"dev", "init-bootstrap-data"})
@Transactional
public class TestDataBootstrapper {

    private static final String MESSAGE_VARDENHETER = "Bootstrapping Vardenheter";
    private static final String MESSAGE_BESTALLNINGAR = "Bootstrapping Beställningar";
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private PersistenceProperties persistenceProperties;
    private BestallningPersistenceService bestallningPersistenceService;
    private NotifieringSendService notifieringSendService;

    private ObjectMapper objectMapper;

    public TestDataBootstrapper(
            PersistenceProperties persistenceProperties,
            BestallningPersistenceService bestallningPersistenceService,
            NotifieringSendService notifieringSendService, @Qualifier("customObjectMapper") ObjectMapper objectMapper) {
        this.persistenceProperties = persistenceProperties;
        this.bestallningPersistenceService = bestallningPersistenceService;
        this.notifieringSendService = notifieringSendService;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    void init() {
        bootstrapBestallningar();
    }

    private void bootstrapBestallningar() {
        LOG.info("Starting: " + MESSAGE_BESTALLNINGAR);

        var bootstrapResult = Try.run(() -> {
            var bestallningBootstrapDirectory = persistenceProperties.getBestallningDirectory();

            loadResources(bestallningBootstrapDirectory, BootstrapBestallning.class).stream()
                    .map(BootstrapBestallning.Factory::toDomain)
                    .forEach(bestallning -> {
                        var savedBestallning = bestallningPersistenceService.saveNewBestallning(bestallning);
                        notifieringSendService.nyBestallning(savedBestallning);
                        bestallningPersistenceService.updateBestallning(savedBestallning);
                    });
        });

        if (bootstrapResult.isFailure()) {
            LOG.error("Failed: " + MESSAGE_BESTALLNINGAR);
            bootstrapResult.getCause().printStackTrace();
        } else {
            LOG.info("Finished:" + MESSAGE_BESTALLNINGAR);
        }
    }

    private <T> List<T> loadResources(String path, Class<T> clazz) throws IOException {
        var pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
        var resources = pathMatchingResourcePatternResolver.getResources(path + "*.json");

        var mappedList = Lists.<T>newArrayList();
        for (var resource : resources) {
            var mapped = objectMapper.readValue(resource.getInputStream(), clazz);

            if (clazz == BootstrapBestallning.class) {
                var date = randomizeDate();
                ((BootstrapBestallning) mapped).setAnkomstDatum(date);
            }

            mappedList.add(mapped);
        }
        return mappedList;
    }

    private LocalDateTime randomizeDate() {
        var daySpan = 30;
        return LocalDateTime.now().minusDays(new Random().nextInt(daySpan + 1));
    }
}
