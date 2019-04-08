package se.inera.intyg.intygsbestallning.common.service.notifiering;

import static java.lang.invoke.MethodHandles.lookup;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.MoreCollectors;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import se.inera.intyg.intygsbestallning.common.domain.NotifieringTyp;
import se.inera.intyg.intygsbestallning.common.property.MailProperties;
import se.inera.intyg.intygsbestallning.common.text.mail.MailTexter;

@Service
public class MailTextServiceImpl implements MailTextService {

    private static final Logger LOG = LoggerFactory.getLogger(lookup().lookupClass());
    private static final String ACTION = "Initiate Intygsbestallning Mail Text Resources";


    private ResourceLoader resourceLoader;
    private MailProperties mailProperties;

    private XmlMapper xmlMapper = new XmlMapper();
    private List<MailTexter> mailTexterList = Lists.newArrayList();

    public MailTextServiceImpl(MailProperties mailProperties, ResourceLoader resourceLoader) {
        this.mailProperties = mailProperties;
        this.resourceLoader = resourceLoader;
    }


    @Override
    public MailTexter getMailContent(NotifieringTyp typ, String intyg) {

        if (typ == null) {
            throw new IllegalArgumentException("typ may not be null");
        }

        if (intyg == null) {
            throw new IllegalArgumentException("intyg may not be null");
        }

        return mailTexterList.stream()
                .filter(content -> content.getTyp().equals(typ.name()) && content.getIntyg().equals(intyg))
                .collect(MoreCollectors.toOptional())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Mail Content for type: " + typ + " and intyg: " + intyg + " does not exist"));
    }

    @PostConstruct
    private void initTexts() {
        LOG.info("Starting: " + ACTION);

        var result = Try.of(() -> loadResources(mailProperties.getTextResourcePath()));
        if (result.isFailure()) {
            LOG.error("Failure: " + ACTION);
            result.getCause().printStackTrace();
        } else {
            mailTexterList = result.get();
            LOG.info("Done: {}, {}", ACTION, mailTexterList.size());
        }
    }

    private List<MailTexter> loadResources(String location) throws IOException {
        LOG.info("Load mail texts from: {}", location);
        return Stream.of(
                ResourcePatternUtils
                        .getResourcePatternResolver(resourceLoader).getResources(location))
                .map(this::parse)
                .collect(Collectors.toList());
    }

    private MailTexter parse(Resource resource) {
        try {
            return xmlMapper.readValue(resource.getInputStream(), MailTexter.class);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
