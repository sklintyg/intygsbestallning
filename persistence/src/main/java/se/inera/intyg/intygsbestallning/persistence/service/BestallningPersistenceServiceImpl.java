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

import com.google.common.primitives.Longs;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import se.inera.intyg.intygsbestallning.common.domain.Bestallning;
import se.inera.intyg.intygsbestallning.common.domain.BestallningStatus;
import se.inera.intyg.intygsbestallning.common.dto.CountBestallningarQuery;
import se.inera.intyg.intygsbestallning.common.dto.ListBestallningDirection;
import se.inera.intyg.intygsbestallning.common.dto.ListBestallningarBasedOnStatusQuery;
import se.inera.intyg.intygsbestallning.common.dto.ListBestallningarQuery;
import se.inera.intyg.intygsbestallning.common.dto.ListBestallningarResult;
import se.inera.intyg.intygsbestallning.common.dto.PageDto;
import se.inera.intyg.intygsbestallning.common.exception.IbErrorCodeEnum;
import se.inera.intyg.intygsbestallning.common.exception.IbServiceException;
import se.inera.intyg.intygsbestallning.persistence.entity.BestallningEntity;
import se.inera.intyg.intygsbestallning.persistence.entity.InvanareEntity;
import se.inera.intyg.intygsbestallning.persistence.entity.QBestallningEntity;
import se.inera.intyg.intygsbestallning.persistence.entity.VardenhetEntity;
import se.inera.intyg.intygsbestallning.persistence.repository.BestallningRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class BestallningPersistenceServiceImpl implements BestallningPersistenceService {

    private BestallningRepository bestallningRepository;

    public BestallningPersistenceServiceImpl(
            BestallningRepository bestallningRepository) {
        this.bestallningRepository = bestallningRepository;
    }

    @Override
    public Bestallning saveNewBestallning(Bestallning bestallning) {

        var invanareEntity = InvanareEntity.Factory.toEntity(bestallning.getInvanare());

        var vardenhetEntity = VardenhetEntity.Factory.toEntity(bestallning.getVardenhet());

        var bestallningEntity = BestallningEntity.Factory.toEntity(bestallning, invanareEntity, vardenhetEntity);

        return BestallningEntity.Factory.toDomain(bestallningRepository.save(bestallningEntity));
    }

    @Override
    public Bestallning updateBestallning(Bestallning bestallning) {
        var bestallningEntity = BestallningEntity.Factory.toEntity(bestallning);
        return BestallningEntity.Factory.toDomain(bestallningRepository.save(bestallningEntity));
    }

    @Override
    public void deleteBestallning(Bestallning bestallning) {
        var bestallningEntity = BestallningEntity.Factory.toEntity(bestallning);
        bestallningRepository.delete(bestallningEntity);
    }

    public long countBestallningar(CountBestallningarQuery query) {

        var pb = new BooleanBuilder();
        var qe = QBestallningEntity.bestallningEntity;
        if (!query.getStatusar().isEmpty()) {
            pb.and(qe.status.in(query.getStatusar()));
        }
        pb.and(qe.vardenhet.hsaId.eq(query.getHsaId()));
        pb.and(qe.vardenhet.organisationId.eq(query.getOrgNrVardgivare()));

        return bestallningRepository.count(pb);
    }

    @Override
    public Pair<PageDto, List<Bestallning>> listBestallningar(ListBestallningarQuery query) {

        var pb = new BooleanBuilder();
        var qe = QBestallningEntity.bestallningEntity;
        if (!query.getStatusar().isEmpty()) {
            pb.and(qe.status.in(query.getStatusar()));
        }
        if (Objects.nonNull(query.getHsaId())) {
            pb.and(qe.vardenhet.hsaId.eq(query.getHsaId()));
        }
        if (Objects.nonNull(query.getOrgNrVardgivare())) {
            pb.and(qe.vardenhet.organisationId.eq(query.getOrgNrVardgivare()));
        }

        var textSearch = query.getTextSearch();
        if (Objects.nonNull(textSearch)) {
            pb.and(textPredicate(textSearch));
        }

        Sort sortRequest;
        if (query.getSortDirection() == ListBestallningDirection.ASC) {
            sortRequest = Sort.by(query.getSortColumn().getKolumn()).ascending();
        } else {
            sortRequest = Sort.by(query.getSortColumn().getKolumn()).descending();
        }

        var pageResult = bestallningRepository.findAll(pb.getValue(), PageRequest.of(query.getPageIndex(), query.getLimit(), sortRequest));

        List<Bestallning> bestallningar = pageResult.get().map(BestallningEntity.Factory::toDomain).collect(Collectors.toList());
        PageDto pageDto = new PageDto(
                pageResult.getNumber(),
                pageResult.getNumber() * pageResult.getSize() + 1,
                pageResult.getNumber() * pageResult.getSize() + pageResult.getNumberOfElements(),
                pageResult.getTotalPages(),
                pageResult.getNumberOfElements(),
                pageResult.getTotalElements());


        return Pair.of(pageDto, bestallningar);
    }

    @Override
    public Optional<Bestallning> getBestallningByIdAndHsaIdAndOrgId(Long id, String hsaId, String orgNrVardgivare) {
        var pb = new BooleanBuilder();
        var qe = QBestallningEntity.bestallningEntity;

        pb.and(qe.id.eq(id));

        var bestallning = bestallningRepository.findOne(pb.getValue())
                .map(BestallningEntity.Factory::toDomain);

        if (bestallning.isPresent()) {
            if (!bestallning.get().getVardenhet().getOrganisationId().equals(orgNrVardgivare)) {
                throw new IbServiceException(IbErrorCodeEnum.VARDGIVARE_ORGNR_MISMATCH,
                        "Bestallning vardgivare organisationId doesn't match user orgNrVardgivare");
            }
            if (!bestallning.get().getVardenhet().getHsaId().equals(hsaId)) {
                throw new IbServiceException(IbErrorCodeEnum.UNAUTHORIZED, "Bestallning vardenhet hsaid doesn't match user hsaid");
            }
        }

        return bestallning;
    }

    @Override
    public Optional<Bestallning> getBestallningById(Long id) {
        var pb = new BooleanBuilder();
        var qe = QBestallningEntity.bestallningEntity;
        pb.and(qe.id.eq(id));

        return bestallningRepository.findOne(pb.getValue())
                .map(BestallningEntity.Factory::toDomain);
    }

    @Override
    public List<Bestallning> listBestallningarBasedOnStatus(ListBestallningarBasedOnStatusQuery query) {
        var pb = new BooleanBuilder();
        var qe = QBestallningEntity.bestallningEntity;
        if (!query.getStatusar().isEmpty()) {
            pb.and(qe.status.in(query.getStatusar()));
        }
        var result = bestallningRepository.findAll(pb.getValue());
        return StreamSupport.stream(result.spliterator(), false)
                .map(BestallningEntity.Factory::toDomain)
                .collect(Collectors.toList());
    }

    private Predicate textPredicate(String text) {

        var qe = QBestallningEntity.bestallningEntity;
        var pb = new BooleanBuilder();

        var id = Longs.tryParse(text);
        if (Objects.nonNull(id)) {
            pb.or(qe.id.eq(id));
        }

        var status = Stream.of(BestallningStatus.values())
                .filter(v -> v.getBeskrivning().equalsIgnoreCase(text))
                .findFirst();
        if (status.isPresent()) {
            pb.or(qe.statusString.equalsIgnoreCase(status.get().getBeskrivning()));
        } else {
            pb.or(qe.statusString.containsIgnoreCase(text));
        }

        return pb
                .or(qe.intygTyp.containsIgnoreCase(text))
                .or(qe.invanare.personId.containsIgnoreCase(text))
                .or(qe.ankomstDatumString.containsIgnoreCase(text))
                .getValue();
    }

}
