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

package se.inera.intyg.intygsbestallning.web.service.bestallning;

import static se.inera.intyg.intygsbestallning.common.dto.BestallningMetadataTyp.MAIL_VIDAREBEFORDRA;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    public Optional<VisaBestallningDto> getBestallningByIdAndHsaIdAndOrgId(Long id, String hsaId, String orgNrVardgivare) {

        var bestallning = bestallningPersistenceService.getBestallningByIdAndHsaIdAndOrgId(id, hsaId, orgNrVardgivare);

        if (bestallning.isEmpty()) {
            return Optional.empty();
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

        return Optional.of(VisaBestallningDto.Factory.toDto(
                bestallning.get(),
                invanareDto,
                getBildUrl(bestallningTexter),
                metaDataList,
                bestallningTexter,
                VisaBestallningScope.ALL));
    }

    private String getBildUrl(BestallningTexter bestallningTexter) {
        return "/images/" + bestallningTexter.getBild();
    }
}
