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

package se.inera.intyg.intygsbestallning.integration.pu;

import java.util.Optional;
import io.vavr.control.Try;
import org.springframework.stereotype.Service;
import se.inera.intyg.infra.integration.pu.model.Person;
import se.inera.intyg.infra.integration.pu.model.PersonSvar;
import se.inera.intyg.infra.integration.pu.services.PUService;
import se.inera.intyg.intygsbestallning.common.exception.IbErrorCodeEnum;
import se.inera.intyg.intygsbestallning.common.exception.IbServiceException;
import se.inera.intyg.schemas.contract.Personnummer;

@Service
public class PatientServiceImpl implements PatientService {

    private PUService puService;

    public PatientServiceImpl(PUService puService) {
        this.puService = puService;
    }

    @Override
    public Optional<Person> lookupPersonnummerFromPU(Personnummer personnummer) {
        var personSvar = puLookup(personnummer);

        switch (personSvar.getStatus()) {
            case FOUND:
                return Optional.of(personSvar.getPerson());
            case NOT_FOUND:
                return Optional.empty();
            case ERROR:
            default:
                throw new IbServiceException(IbErrorCodeEnum.PU_ERROR, "Could not get uppslag from PU");
        }
    }

    @Override
    public Optional<Boolean> isSekretessmarkerad(Personnummer personnummer) {
        var personSvar = puLookup(personnummer);

        switch (personSvar.getStatus()) {
            case FOUND:
                return Optional.of(personSvar.getPerson().isSekretessmarkering());
            case NOT_FOUND:
                return Optional.empty();
            case ERROR:
            default:
                throw new IbServiceException(IbErrorCodeEnum.PU_ERROR, "Could not get uppslag from PU");
        }
    }

    private PersonSvar puLookup(Personnummer personnummer) {
        var personSvar = Try.of(() -> puService.getPerson(personnummer));
        if (personSvar.isFailure()) {
            throw new IbServiceException(IbErrorCodeEnum.PU_ERROR, "Could not get uppslag from PU");
        }
        return personSvar.get();
    }
}
