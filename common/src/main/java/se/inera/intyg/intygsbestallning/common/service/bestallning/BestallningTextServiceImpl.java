package se.inera.intyg.intygsbestallning.common.service.bestallning;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.kotlin.KotlinModule;
import com.google.common.collect.MoreCollectors;
import io.vavr.control.Try;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Service;
import se.inera.intyg.intygsbestallning.common.domain.Bestallning;
import se.inera.intyg.intygsbestallning.common.property.BestallningProperties;
import se.inera.intyg.intygsbestallning.common.text.bestallning.BestallningTexter;


import static java.lang.invoke.MethodHandles.lookup;

@Service
public class BestallningTextServiceImpl implements BestallningTextService {

    private static final Logger LOG = LoggerFactory.getLogger(lookup().lookupClass());
    private static final String ACTION = "Initiate Intygsbestallning Bestallning Text Resources";

    private List<BestallningTexter> bestallningTexterList = Collections.EMPTY_LIST;

    private BestallningProperties bestallningProperties;

    public BestallningTextServiceImpl(BestallningProperties bestallningProperties) {
        this.bestallningProperties = bestallningProperties;
    }

    @Autowired
    ResourceLoader resourceLoader;

    XmlMapper xmlMapper;

    @PostConstruct
    public void initTexter() {
        xmlMapper = new XmlMapper();
        xmlMapper.registerModule(new KotlinModule());
        LOG.info("Starting: " + ACTION);
        var result = Try.of(() -> loadResources(bestallningProperties.getTextResourcePath()));
        if (result.isFailure()) {
            LOG.error("Failure: " + ACTION);
            result.getCause().printStackTrace();
        } else {
            bestallningTexterList = result.get();
            LOG.info("Done: {}, {}", ACTION, bestallningTexterList.size());
        }
    }

    @Override
    public BestallningTexter getBestallningTexter(Bestallning bestallning) {
        return getBestallningTexter(bestallning.getIntygTyp(), bestallning.getIntygVersion())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Bestallning text resources for bestallning is not supported for Type: " +
                                bestallning.getIntygTyp() + " and Version: " + bestallning.getIntygVersion()));
    }

    @Override
    public Double getLatestVersionForBestallningsbartIntyg(String intygTyp) {
        return getLatestVersionForTyp(intygTyp);
    }

    private Optional<BestallningTexter> getBestallningTexter(String intygTyp, Double version) {
        return bestallningTexterList.stream()
                .filter(texter -> texter.getTyp().equals(intygTyp))
                .filter(texter -> texter.getVersion().equals(version.toString()))
                .collect(MoreCollectors.toOptional());
    }

    private Double getLatestVersionForTyp(String intygTyp) {
        return bestallningTexterList.stream()
                .filter(texter -> texter.getTyp().equals(intygTyp))

                .filter(bestallningTexter -> {
                    var date = Try.of(() -> LocalDate.parse(bestallningTexter.getGiltigFrom()));
                    return date.isSuccess() && !date.get().isAfter(LocalDate.now());
                })
                .mapToDouble(texter -> Double.parseDouble(texter.getVersion()))
                .max()
                .orElseThrow(() -> new IllegalArgumentException("Intyg of Type: " + intygTyp + " is not supported"));
    }

    private List<BestallningTexter> loadResources(String location) throws IOException {
        LOG.info("Load bestallning texts from: {}", location);
        return Stream.of(
                ResourcePatternUtils
                        .getResourcePatternResolver(resourceLoader).getResources(location))
                .map(this::parse)
                .collect(Collectors.toList());
    }

    private BestallningTexter parse(Resource resource) {
        try {
            return xmlMapper.readValue(resource.getInputStream(), BestallningTexter.class);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
