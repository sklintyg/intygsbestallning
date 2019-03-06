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
package se.inera.intyg.intygsbestallning.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import se.inera.intyg.intygsbestallning.persistence.NotifieringPreferenceEntity;

import java.util.Optional;

@Transactional(value = "transactionManager", readOnly = false)
public interface NotifieringPreferenceRepository extends JpaRepository<NotifieringPreferenceEntity, String> {

    /**
     * Finds an entity by it's hsaId.
     *
     * @param hsaId
     * @return NotifieringPreference for this hsaId if exists.
     */
    Optional<NotifieringPreferenceEntity> findByHsaId(String hsaId);
}
