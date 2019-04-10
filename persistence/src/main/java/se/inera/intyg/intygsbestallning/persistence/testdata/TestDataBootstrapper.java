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
import se.inera.intyg.intygsbestallning.persistence.service.BestallningPersistenceService;
import se.inera.intyg.intygsbestallning.persistence.service.VardenhetPersistenceService;

@Component
@Profile({"dev"})
@Transactional
public class TestDataBootstrapper {

    private static final String MESSAGE_VARDENHETER = "Bootstrapping Vardenheter";
    private static final String MESSAGE_BESTALLNINGAR = "Bootstrapping Beställningar";
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private PersistenceProperties persistenceProperties;
    private VardenhetPersistenceService vardenhetPersistenceService;
    private BestallningPersistenceService bestallningPersistenceService;


    private ObjectMapper objectMapper;

    public TestDataBootstrapper(
            PersistenceProperties persistenceProperties,
            VardenhetPersistenceService vardenhetPersistenceService,
            BestallningPersistenceService bestallningPersistenceService,
            @Qualifier("customObjectMapper") ObjectMapper objectMapper) {
        this.persistenceProperties = persistenceProperties;
        this.vardenhetPersistenceService = vardenhetPersistenceService;
        this.bestallningPersistenceService = bestallningPersistenceService;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    void init() {
        bootstrapVardenheter();
        bootstrapBestallningar();
    }

    private void bootstrapVardenheter() {
        LOG.info("Starting: " + MESSAGE_VARDENHETER);

        var bootstrapResult = Try.run(() -> {
            var vardenhetBootstrapDirectory = persistenceProperties.getVardenhetDirectory();
            var mappadeVardeneheter = loadResources(vardenhetBootstrapDirectory, Vardenhet.class);

            mappadeVardeneheter.forEach(vardenhetPersistenceService::saveNewVardenhet);
        });

        if (bootstrapResult.isFailure()) {
            LOG.error("Failed: " + MESSAGE_VARDENHETER);
            bootstrapResult.getCause().printStackTrace();
        }
        LOG.info("Finished:" + MESSAGE_VARDENHETER);
    }

    private void bootstrapBestallningar() {
        LOG.info("Starting: " + MESSAGE_BESTALLNINGAR);

        var bootstrapResult = Try.run(() -> {
            var bestallningBootstrapDirectory = persistenceProperties.getBestallningDirectory();

            loadResources(bestallningBootstrapDirectory, BootstrapBestallning.class).stream()
                    .map(BootstrapBestallning.Factory::toDomain)
                    .forEach(bestallningPersistenceService::saveNewBestallning);
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
