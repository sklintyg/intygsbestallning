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

package se.inera.intyg.intygsbestallning.common.resolver;

import java.lang.invoke.MethodHandles;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import javax.annotation.Nonnull;
import com.sun.mail.util.BASE64EncoderStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import se.inera.intyg.intygsbestallning.common.domain.Bestallning;
import se.inera.intyg.intygsbestallning.common.domain.BestallningStatus;
import se.inera.intyg.intygsbestallning.common.domain.Handelse;
import se.inera.intyg.intygsbestallning.common.exception.IbErrorCodeEnum;
import se.inera.intyg.intygsbestallning.common.exception.IbServiceException;

@Component
public class BestallningStatusResolverImpl implements BestallningStatusResolver {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void setStatus(Bestallning bestallning) {

        if (bestallning == null) {
            throw new IllegalArgumentException("bestallning may not be null");
        }

        if (bestallning.getStatus() == null) {
            throw new IllegalArgumentException("status may not be null");
        }

        var nuvarandeStatus = bestallning.getStatus();

        var handelser = bestallning.getHandelser();

        if (handelser == null || handelser.isEmpty()) {
            throw new IllegalArgumentException("handelseList may not be null or empty");
        }

        var senasteHandelse = Collections.max(handelser, Comparator.comparing(Handelse::getSkapad));

        switch (senasteHandelse.getEvent()) {
            case SKAPA:
                if (nuvarandeStatus != BestallningStatus.UNDEFINED) {
                    throw new IbServiceException(IbErrorCodeEnum.GENERAL_FEL01_SPARA,
                            "Can not update state from UNDEFINED unless latest handelse is of typ SKAPA");
                }
                bestallning.setStatus(BestallningStatus.OLAST);
                break;
            case LAS:
                if (nuvarandeStatus != BestallningStatus.OLAST) {
                    throw new IbServiceException(IbErrorCodeEnum.GENERAL_FEL01_SPARA,
                            "Can not update state from OLAST unless latest handelse is of typ LAST");
                }
                bestallning.setStatus(BestallningStatus.LAST);
                break;
            case AVVISA_RADERA:
                if (nuvarandeStatus != BestallningStatus.LAST) {
                    throw new IbServiceException(IbErrorCodeEnum.GENERAL_FEL01_SPARA,
                            "Can not update state from LAST to AVVISAD_RADERAD unless latest handelse is of typ LAST");
                }
                // Do not set a new status here, the bestallning will actually be removed from the database
                break;
            case AVVISA:
                if (nuvarandeStatus != BestallningStatus.LAST) {
                    throw new IbServiceException(IbErrorCodeEnum.GENERAL_FEL01_SPARA,
                            "Can not update state from LAST to AVVISA unless latest handelse is of typ AVVISAD");
                }
                bestallning.setStatus(BestallningStatus.AVVISAD);
                break;
            case ACCEPTERA:
                if (nuvarandeStatus != BestallningStatus.LAST) {
                    throw new IbServiceException(IbErrorCodeEnum.GENERAL_FEL01_SPARA,
                            "Can not update state from LAST to ACCEPTERA unless latest handelse is of typ ACCEPTERAD");
                }
                bestallning.setStatus(BestallningStatus.ACCEPTERAD);
                break;
            case KLARMARKERA:
                if (nuvarandeStatus != BestallningStatus.ACCEPTERAD) {
                    throw new IbServiceException(IbErrorCodeEnum.GENERAL_FEL01_SPARA,
                            "Can not update state from ACCEPTEDAD to KLAR unless latest handelse is of typ KLARMARKERA");
                }
                bestallning.setStatus(BestallningStatus.KLAR);
                break;
            default:
                throw new IbServiceException(IbErrorCodeEnum.GENERAL_FEL01_SPARA, "Not a valid bestallningEvent: " + senasteHandelse);
        }
    }
}
