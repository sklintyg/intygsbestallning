package se.inera.intyg.intygsbestallning.web.service.bestallning;

import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.stream.Collectors;
import se.inera.intyg.intygsbestallning.common.domain.BestallningStatus;
import se.inera.intyg.intygsbestallning.common.domain.Handelse;
import se.inera.intyg.intygsbestallning.common.dto.VisaBestallningDto;
import se.inera.intyg.intygsbestallning.common.property.BestallningProperties;
import se.inera.intyg.intygsbestallning.common.resolver.BestallningStatusResolver;
import se.inera.intyg.intygsbestallning.common.service.bestallning.BestallningTextService;
import se.inera.intyg.intygsbestallning.common.text.bestallning.BestallningTexter;
import se.inera.intyg.intygsbestallning.common.text.bestallning.Text;
import se.inera.intyg.intygsbestallning.integration.pu.PatientService;
import se.inera.intyg.intygsbestallning.persistence.service.BestallningPersistenceService;

@Service
public class VisaBestallningServiceImpl implements VisaBestallningService {

    //Forfragan
    private static final String BILD = "BILD";
    private static final String RBK_1 = "RBK_1";
    private static final String ETK_1_1 = "ETK_1.1";
    private static final String ETK_1_2 = "ETK_1.2";
    private static final String TEXT_1_2_1 = "TEXT_1.2.1";
    private static final String ETK_1_3 = "ETK_1.3";
    private static final String TEXT_1_3_1 = "TEXT_1.3.1";
    private static final String RBK_2 = "RBK_2";
    private static final String ETK_2_1 = "ETK_2.1";
    private static final String ETK_2_2 = "ETK_2.2";
    private static final String RBK_3 = "RBK_3";
    private static final String ETK_3_1 = "ETK_3.1";
    private static final String ETK_3_2 = "ETK_3.2";
    private static final String ETK_3_3 = "ETK_3.3";
    private static final String RBK_5 = "RBK_5";
    private static final String ETK_5_1 = "ETK_5.1";
    private static final String ETK_5_2 = "ETK_5.2";

    //Faktureringsunderlag
    private static final String RBK_6 = "RBK_6";
    private static final String ETK_6_1 = "ETK_6.1";
    private static final String TEXT_6_1_1 = "TEXT_6.1.1";
    private static final String ETK_6_2 = "ETK_6.2";
    private static final String ETK_6_3 = "ETK_6.3";
    private static final String ETK_6_4 = "ETK_6.4";
    private static final String TEXT_6_4_1 = "TEXT_6.4.1";
    private static final String ETK_6_5 = "ETK_6.5";
    private static final String TEXT_6_5_1 = "TEXT_6.5.1";

    private BestallningPersistenceService bestallningPersistenceService;
    private BestallningStatusResolver bestallningStatusResolver;
    private BestallningTextService bestallningTextService;
    private PatientService patientService;
    private BestallningProperties bestallningProperties;

    public VisaBestallningServiceImpl(
            BestallningPersistenceService bestallningPersistenceService,
            BestallningStatusResolver bestallningStatusResolver,
            BestallningTextService bestallningTextService,
            PatientService patientService,
            BestallningProperties bestallningProperties) {
        this.bestallningPersistenceService = bestallningPersistenceService;
        this.bestallningStatusResolver = bestallningStatusResolver;
        this.bestallningTextService = bestallningTextService;
        this.patientService = patientService;
        this.bestallningProperties = bestallningProperties;
    }

    @Override
    public Optional<VisaBestallningDto> getBestallningById(Long id) {

        var bestallning = bestallningPersistenceService.getBestallningById(id);

        if (bestallning.isEmpty()) {
            return Optional.empty();
        }

        patientService.updatePersonDetaljer(bestallning.get().getInvanare());

        if (bestallning.get().getStatus() == BestallningStatus.OLAST) {
            bestallning.get().getHandelser().add(Handelse.Factory.las());
            bestallningStatusResolver.setStatus(bestallning.get());
            var updatedBestallning = bestallningPersistenceService.updateBestallning(bestallning.get());
            var bestallningTexter = bestallningTextService.getBestallningTexter(updatedBestallning);
            return Optional.of(VisaBestallningDto.Factory.toDto(updatedBestallning, getBildUrl(bestallningTexter), bestallningTexter));
        } else {
            var bestallningTexter = bestallningTextService.getBestallningTexter(bestallning.get());
            return Optional.of(VisaBestallningDto.Factory.toDto(bestallning.get(), getBildUrl(bestallningTexter), bestallningTexter));
        }
    }

    private String getBildUrl(BestallningTexter bestallningTexter) {
        return bestallningProperties.getHost() + "/images/" + bestallningTexter.getBild();
    }
}
