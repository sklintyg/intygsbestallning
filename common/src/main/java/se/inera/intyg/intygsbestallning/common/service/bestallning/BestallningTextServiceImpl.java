package se.inera.intyg.intygsbestallning.common.service.bestallning;

import static java.lang.invoke.MethodHandles.lookup;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.kotlin.KotlinModule;
import com.google.common.collect.Lists;
import com.google.common.collect.MoreCollectors;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import se.inera.intyg.intygsbestallning.common.domain.Bestallning;
import se.inera.intyg.intygsbestallning.common.domain.IntygTyp;
import se.inera.intyg.intygsbestallning.common.property.BestallningProperties;
import se.inera.intyg.intygsbestallning.common.text.bestallning.BestallningTexter;

@Service
public class BestallningTextServiceImpl implements BestallningTextService {

    private static final Logger LOG = LoggerFactory.getLogger(lookup().lookupClass());
    private static final String ACTION = "Initiate Intygsbestallning Bestallning Text Resources";

    private List<BestallningTexter> bestallningTexterList = Lists.newArrayList();

    private BestallningProperties bestallningProperties;

    public BestallningTextServiceImpl(BestallningProperties bestallningProperties) {
        this.bestallningProperties = bestallningProperties;
    }

    @PostConstruct
    public void initTexter() {
        LOG.info("Starting: " + ACTION);
        var result = Try.run(() -> loadResources(bestallningProperties.getTextResourcePath()));
        if (result.isFailure()) {
            LOG.error("Failure: " + ACTION);
            result.getCause().printStackTrace();
        } else {
            LOG.info("Done: " + ACTION);
        }
    }

    @Override
    public BestallningTexter getBestallningTexter(Bestallning bestallning) {
        return getBestallningTexter(bestallning.getIntygTyp(), "1.0"); //TODO: Version

    }

    private BestallningTexter getBestallningTexter(IntygTyp intygTyp, String version) {
        return bestallningTexterList.stream()
                .filter(texter -> texter.getTyp().equals(intygTyp.name()))
                .filter(texter -> texter.getVersion().equals(version))
                .collect(MoreCollectors.toOptional())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Bestallning text resources for bestallning is not supported for Type: " +
                                intygTyp.name() + " and Version: " + version));
    }

    private void loadResources(String path) throws IOException {
        var xmlMapper = new XmlMapper();
        xmlMapper.registerModule(new KotlinModule());

        var filePaths = Files.walk(Paths.get(path))
                .filter(Files::isRegularFile)
                .collect(Collectors.toList());

        for (var filePath : filePaths) {
            var file = filePath.toFile();
            var mailContent = xmlMapper.readValue(file, BestallningTexter.class);
            bestallningTexterList.add(mailContent);
        }
    }
}
