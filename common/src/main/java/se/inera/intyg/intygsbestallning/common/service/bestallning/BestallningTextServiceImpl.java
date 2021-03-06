/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.inera.intyg.intygsbestallning.common.service.bestallning;

import static java.lang.invoke.MethodHandles.lookup;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.kotlin.KotlinModule;
import com.google.common.collect.MoreCollectors;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Service;
import se.inera.intyg.intygsbestallning.common.domain.Bestallning;
import se.inera.intyg.intygsbestallning.common.property.BestallningProperties;
import se.inera.intyg.intygsbestallning.common.text.bestallning.BestallningTexter;

@Service
public class BestallningTextServiceImpl implements BestallningTextService {

    private static final Logger LOG = LoggerFactory.getLogger(lookup().lookupClass());
    private static final String ACTION = "Initiate Intygsbestallning Bestallning Text Resources";
    protected static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_DATE;


    private List<BestallningTexter> bestallningTexterList = List.of();
    private BestallningProperties bestallningProperties;
    private ResourceLoader resourceLoader;
    private XmlMapper xmlMapper;

    public BestallningTextServiceImpl(BestallningProperties bestallningProperties, ResourceLoader resourceLoader) {
        this.bestallningProperties = bestallningProperties;
        this.resourceLoader = resourceLoader;
    }



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
        return getTexter(bestallning.getTyp())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Bestallning text resources for bestallning is not supported for Type: "
                                + bestallning.getTyp()));
    }

    @Override
    public Optional<BestallningTexter> getBestallningTexter(String typ) {
        return getTexter(typ);
    }

    private Optional<BestallningTexter> getTexter(String intygTyp) {
        return bestallningTexterList.stream()
                .filter(texter -> texter.getTyp().equals(intygTyp))
                .filter(texter -> LocalDate.parse(texter.getGiltigFrom(), FORMATTER).isBefore(LocalDate.now()))
                .collect(MoreCollectors.toOptional());
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
