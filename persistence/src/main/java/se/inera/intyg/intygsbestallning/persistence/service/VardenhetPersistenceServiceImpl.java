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
import se.inera.intyg.intygsbestallning.common.domain.Vardenhet;
import se.inera.intyg.intygsbestallning.persistence.entity.VardenhetEntity;
import se.inera.intyg.intygsbestallning.persistence.repository.VardenhetRepository;

@Service
@Transactional
public class VardenhetPersistenceServiceImpl implements VardenhetPersistenceService {

    private final VardenhetRepository vardenhetRepository;

    public VardenhetPersistenceServiceImpl(VardenhetRepository vardenhetRepository) {
        this.vardenhetRepository = vardenhetRepository;
    }

    @Override
    public Vardenhet saveNewVardenhet(Vardenhet vardenhet) {

        if (vardenhetRepository.existsByHsaId(vardenhet.getHsaId())) {
            throw new IllegalArgumentException("Vardenhet with hsaId: " + vardenhet.getHsaId() + " already exists");
        }

        var vardenhetEntity = VardenhetEntity.Factory.toEntity(vardenhet);
        vardenhetRepository.save(vardenhetEntity);

        return VardenhetEntity.Factory.toDomain(vardenhetEntity);
    }

    @Override
    public Optional<Vardenhet> getVardenhetByHsaId(String hsaId) {
        return vardenhetRepository.findByHsaId(hsaId)
                .map(VardenhetEntity.Factory::toDomain);
    }
}
