package se.inera.intyg.intygsbestallning.common.service.notifiering;

import static com.google.common.collect.Lists.newArrayList;
import static java.lang.invoke.MethodHandles.lookup;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.MoreCollectors;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import se.inera.intyg.intygsbestallning.common.domain.NotifieringTyp;
import se.inera.intyg.intygsbestallning.common.mail.MailContent;
import se.inera.intyg.intygsbestallning.common.property.MailProperties;

@Service
public class MailTextServiceImpl implements MailTextService {

    private static final Logger LOG = LoggerFactory.getLogger(lookup().lookupClass());
    private static final String ACTION = "Initiate Intygsbestallning Mail Text Resources";

    private List<MailContent> mailContentList = newArrayList();

    private MailProperties mailProperties;
    private ResourceLoader resourceLoader;

    public MailTextServiceImpl(MailProperties mailProperties, ResourceLoader resourceLoader) {
        this.mailProperties = mailProperties;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public MailContent getMailContent(NotifieringTyp typ) {

        if (typ == null) {
            throw new IllegalArgumentException("typ may not be null");
        }

        return mailContentList.stream()
                .filter(content -> content.getTyp().equals(typ.name()))
                .collect(MoreCollectors.toOptional())
                .orElseThrow(() -> new IllegalArgumentException("Mail Content for type: " + typ + "does not exist"));
    }

    @PostConstruct
    private void initTexts() throws IOException {
        LOG.info("Starting: " + ACTION);

        var result = Try.run(() -> loadResources(mailProperties.getTextResourcePath()));
        if (result.isFailure()) {
            LOG.error("Failure: " + ACTION);
            result.getCause().printStackTrace();
        } else {
            LOG.info("Done: " + ACTION);
        }
    }

    private void loadResources(String pattern) throws IOException {
        var xmlMapper = new XmlMapper();
        var filePaths = Files.walk(Paths.get(mailProperties.getTextResourcePath()))
                .filter(Files::isRegularFile)
                .collect(Collectors.toList());

        var tempList = Lists.<MailContent>newArrayList();
        for (var filePath : filePaths) {
            var file = filePath.toFile();
            var mailContent = xmlMapper.readValue(file, MailContent.class);
            tempList.add(mailContent);
        }
        mailContentList.addAll(tempList);
    }

//    private String convertToXml(File file) throws FileNotFoundException {
//        //filename is filepath string
//        var bufferedReader = new BufferedReader(new FileReader(file));
//        String line;
//        StringBuilder sb = new StringBuilder();
//
//        while((line=br.readLine())!= null){
//            sb.append(line.trim());
//        }
//    }
}
