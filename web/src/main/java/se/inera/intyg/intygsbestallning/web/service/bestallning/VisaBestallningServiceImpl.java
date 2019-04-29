package se.inera.intyg.intygsbestallning.web.service.bestallning;

import static se.inera.intyg.intygsbestallning.common.dto.BestallningMetadataTyp.MAIL_VIDAREBEFORDRA;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import se.inera.intyg.intygsbestallning.common.domain.BestallningStatus;
import se.inera.intyg.intygsbestallning.common.domain.Handelse;
import se.inera.intyg.intygsbestallning.common.dto.BestallningInvanareDto;
import se.inera.intyg.intygsbestallning.common.dto.BestallningMetaData;
import se.inera.intyg.intygsbestallning.common.dto.VisaBestallningDto;
import se.inera.intyg.intygsbestallning.common.dto.VisaBestallningScope;
import se.inera.intyg.intygsbestallning.common.exception.IbErrorCodeEnum;
import se.inera.intyg.intygsbestallning.common.exception.IbServiceException;
import se.inera.intyg.intygsbestallning.common.resolver.BestallningStatusResolver;
import se.inera.intyg.intygsbestallning.common.service.bestallning.BestallningTextService;
import se.inera.intyg.intygsbestallning.common.service.notifiering.NotifieringSendService;
import se.inera.intyg.intygsbestallning.common.text.bestallning.BestallningTexter;
import se.inera.intyg.intygsbestallning.integration.pu.PatientService;
import se.inera.intyg.intygsbestallning.persistence.service.BestallningPersistenceService;
import se.inera.intyg.intygsbestallning.web.pdl.LogEvent;
import se.inera.intyg.intygsbestallning.web.service.pdl.LogService;

@Service
@Transactional
public class VisaBestallningServiceImpl implements VisaBestallningService {

    private BestallningPersistenceService bestallningPersistenceService;
    private BestallningStatusResolver bestallningStatusResolver;
    private BestallningTextService bestallningTextService;
    private PatientService patientService;
    private NotifieringSendService notifieringSendService;
    private LogService pdlLogService;

    public VisaBestallningServiceImpl(
            BestallningPersistenceService bestallningPersistenceService,
            BestallningStatusResolver bestallningStatusResolver,
            BestallningTextService bestallningTextService,
            PatientService patientService,
            NotifieringSendService notifieringSendService,
            LogService pdlLogService) {
        this.bestallningPersistenceService = bestallningPersistenceService;
        this.bestallningStatusResolver = bestallningStatusResolver;
        this.bestallningTextService = bestallningTextService;
        this.patientService = patientService;
        this.notifieringSendService = notifieringSendService;
        this.pdlLogService = pdlLogService;
    }

    @Override
    public VisaBestallningDto getBestallningByIdAndHsaIdAndOrgId(Long id, String hsaId, String orgNrVardgivare) {

        var bestallning = bestallningPersistenceService.getBestallningByIdAndHsaIdAndOrgId(id, hsaId, orgNrVardgivare);

        if (bestallning.isEmpty()) {
            throw new IbServiceException(IbErrorCodeEnum.NOT_FOUND, MessageFormat.format("Bestallning with id '{0}' was not found", id));
        }

        if (bestallning.get().getStatus() == BestallningStatus.OLAST) {
            bestallning.get().getHandelser().add(Handelse.Factory.las());
            bestallningStatusResolver.setStatus(bestallning.get());
            bestallningPersistenceService.updateBestallning(bestallning.get());
        }

        var bestallningTexter = bestallningTextService.getBestallningTexter(bestallning.get());

        var personSvar = patientService.lookupPersonnummerFromPU(bestallning.get().getInvanare().getPersonId());

        if (personSvar.isEmpty()) {
            throw new IbServiceException(IbErrorCodeEnum.PU_ERROR, "Person was not found in PU");
        }

        pdlLogService.log(bestallning.get(), LogEvent.BESTALLNING_OPPNAS_OCH_LASES);

        var invanareDto = BestallningInvanareDto.Factory.toDto(
                bestallning.get().getInvanare(),
                personSvar.get().getFornamn(),
                personSvar.get().getMellannamn(),
                personSvar.get().getEfternamn(),
                personSvar.get().isSekretessmarkering());

        var metaDataList = List.of(
                new BestallningMetaData(MAIL_VIDAREBEFORDRA, notifieringSendService.vidarebefordrad(bestallning.get()))
        );

        return VisaBestallningDto.Factory.toDto(
                bestallning.get(),
                invanareDto,
                getBildUrl(bestallningTexter),
                metaDataList,
                bestallningTexter,
                VisaBestallningScope.ALL);
    }

    private String getBildUrl(BestallningTexter bestallningTexter) {
        return "/images/" + bestallningTexter.getBild();
    }
}
