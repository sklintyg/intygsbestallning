package se.inera.intyg.intygsbestallning.web.service.bestallning;

import org.springframework.stereotype.Service;
import java.util.Optional;
import se.inera.intyg.intygsbestallning.common.domain.BestallningStatus;
import se.inera.intyg.intygsbestallning.common.domain.Handelse;
import se.inera.intyg.intygsbestallning.common.dto.VisaBestallningDto;
import se.inera.intyg.intygsbestallning.common.property.BestallningProperties;
import se.inera.intyg.intygsbestallning.common.resolver.BestallningStatusResolver;
import se.inera.intyg.intygsbestallning.common.service.bestallning.BestallningTextService;
import se.inera.intyg.intygsbestallning.common.text.bestallning.BestallningTexter;
import se.inera.intyg.intygsbestallning.integration.pu.PatientService;
import se.inera.intyg.intygsbestallning.persistence.service.BestallningPersistenceService;
import se.inera.intyg.intygsbestallning.web.pdl.LogEvent;
import se.inera.intyg.intygsbestallning.web.service.pdl.LogService;

@Service
public class VisaBestallningServiceImpl implements VisaBestallningService {

    private BestallningPersistenceService bestallningPersistenceService;
    private BestallningStatusResolver bestallningStatusResolver;
    private BestallningTextService bestallningTextService;
    private PatientService patientService;
    private BestallningProperties bestallningProperties;
    private LogService pdlLogService;

    public VisaBestallningServiceImpl(
            BestallningPersistenceService bestallningPersistenceService,
            BestallningStatusResolver bestallningStatusResolver,
            BestallningTextService bestallningTextService,
            PatientService patientService,
            BestallningProperties bestallningProperties,
            LogService pdlLogService) {
        this.bestallningPersistenceService = bestallningPersistenceService;
        this.bestallningStatusResolver = bestallningStatusResolver;
        this.bestallningTextService = bestallningTextService;
        this.patientService = patientService;
        this.bestallningProperties = bestallningProperties;
        this.pdlLogService = pdlLogService;
    }

    @Override
    public Optional<VisaBestallningDto> getBestallningByIdAndHsaIdAndOrgId(Long id, String hsaId, String orgNrVardgivare) {

        var bestallning = bestallningPersistenceService.getBestallningByIdAndHsaIdAndOrgId(id, hsaId, orgNrVardgivare);

        if (bestallning.isEmpty()) {
            return Optional.empty();
        }

        patientService.updatePersonDetaljer(bestallning.get().getInvanare());

        if (bestallning.get().getStatus() == BestallningStatus.OLAST) {
            bestallning.get().getHandelser().add(Handelse.Factory.las());
            bestallningStatusResolver.setStatus(bestallning.get());
            bestallningPersistenceService.updateBestallning(bestallning.get());
        }

        var bestallningTexter = bestallningTextService.getBestallningTexter(bestallning.get());

        pdlLogService.log(bestallning.get(), LogEvent.BESTALLNING_OPPNAS_OCH_LASES);

        return Optional.of(VisaBestallningDto.Factory.toDto(bestallning.get(), getBildUrl(bestallningTexter), bestallningTexter));
    }

    private String getBildUrl(BestallningTexter bestallningTexter) {
        return bestallningProperties.getHost() + "/images/" + bestallningTexter.getBild();
    }
}
