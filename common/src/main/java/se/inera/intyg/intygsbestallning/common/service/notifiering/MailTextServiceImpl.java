package se.inera.intyg.intygsbestallning.common.service.notifiering;

import static java.lang.invoke.MethodHandles.lookup;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
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
import se.inera.intyg.intygsbestallning.common.domain.NotifieringTyp;
import se.inera.intyg.intygsbestallning.common.property.MailProperties;
import se.inera.intyg.intygsbestallning.common.text.mail.MailTexter;

@Service
public class MailTextServiceImpl implements MailTextService {

    private static final Logger LOG = LoggerFactory.getLogger(lookup().lookupClass());
    private static final String ACTION = "Initiate Intygsbestallning Mail Text Resources";

    private List<MailTexter> mailTexterList = Lists.newArrayList();

    private MailProperties mailProperties;

    public MailTextServiceImpl(MailProperties mailProperties) {
        this.mailProperties = mailProperties;
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

        var result = Try.run(this::loadResources);
        if (result.isFailure()) {
            LOG.error("Failure: " + ACTION);
            result.getCause().printStackTrace();
        } else {
            LOG.info("Done: " + ACTION);
        }
    }

    private void loadResources() throws IOException {
        var xmlMapper = new XmlMapper();
        var filePaths = Files.walk(Paths.get(mailProperties.getTextResourcePath()))
                .filter(Files::isRegularFile)
                .collect(Collectors.toList());

        var tempList = Lists.<MailTexter>newArrayList();
        for (var filePath : filePaths) {
            var file = filePath.toFile();
            var mailContent = xmlMapper.readValue(file, MailTexter.class);
            tempList.add(mailContent);
        }
        mailTexterList.addAll(tempList);
    }
}
