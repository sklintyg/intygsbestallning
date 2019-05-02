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

package se.inera.intyg.intygsbestallning.persistence.service;

import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import se.inera.intyg.intygsbestallning.common.domain.Invanare;
import se.inera.intyg.intygsbestallning.persistence.entity.InvanareEntity;
import se.inera.intyg.intygsbestallning.persistence.repository.InvanareRepository;
import se.inera.intyg.schemas.contract.Personnummer;

@Service
@Transactional
public class InvanarePersistenceServiceImpl implements InvanarePersistenceService {

    private InvanareRepository invanareRepository;

    public InvanarePersistenceServiceImpl(InvanareRepository invanareRepository) {
        this.invanareRepository = invanareRepository;
    }

    @Override
    public Optional<Invanare> getInvanareByPersonnummer(Personnummer personnummer) {
        return invanareRepository.findByPersonId(personnummer.getPersonnummerWithDash())
                .map(InvanareEntity.Factory::toDomain);
    }
}
