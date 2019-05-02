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

package se.inera.intyg.intygsbestallning.web.controller.testability;

import com.querydsl.core.BooleanBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.inera.intyg.intygsbestallning.common.domain.Bestallning;
import se.inera.intyg.intygsbestallning.persistence.entity.BestallningEntity;
import se.inera.intyg.intygsbestallning.persistence.entity.InvanareEntity;
import se.inera.intyg.intygsbestallning.persistence.entity.QBestallningEntity;
import se.inera.intyg.intygsbestallning.persistence.entity.VardenhetEntity;
import se.inera.intyg.intygsbestallning.persistence.repository.BestallningRepository;
import se.inera.intyg.intygsbestallning.web.service.util.EntityTxMapper;

import javax.ws.rs.core.Response;

@RestController
@RequestMapping(BestallningResource.API_TEST_BESTALLNINGAR)
@Profile({"dev", "testability-api"})
public class BestallningResource {

    public static final String API_TEST_BESTALLNINGAR = "/api/test/bestallningar";

    private BestallningRepository bestallningRepository;
    private EntityTxMapper entityTxMapper;

    public BestallningResource(BestallningRepository bestallningRepository, EntityTxMapper entityTxMapper) {
        this.bestallningRepository = bestallningRepository;
        this.entityTxMapper = entityTxMapper;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Response createBestallning(@RequestBody Bestallning bestallning) {

        var invanareEntity = InvanareEntity.Factory.toEntity(bestallning.getInvanare());
        var vardenhetEntity = VardenhetEntity.Factory.toEntity(bestallning.getVardenhet());
        var bestallningEntity = BestallningEntity.Factory.toEntity(bestallning, invanareEntity, vardenhetEntity);

        return entityTxMapper.jsonResponse(() -> bestallningRepository.save(bestallningEntity));
    }

    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response deleteBestallning(@PathVariable("id") Long id) {
        return entityTxMapper.jsonResponse(() -> {

            var pb = new BooleanBuilder();
            var qe = QBestallningEntity.bestallningEntity;
            pb.and(qe.id.eq(id));

            var bestallning = bestallningRepository.findOne(pb).orElseThrow(
                () -> new IllegalArgumentException("Bestallning with id '" + id + "' does not exist."));

            bestallningRepository.delete(bestallning);

            return EntityTxMapper.OK;
        });
    }
}
