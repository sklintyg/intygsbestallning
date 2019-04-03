package se.inera.intyg.intygsbestallning.persistence.service;

import com.google.common.base.Enums;
import com.google.common.primitives.Longs;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import io.vavr.control.Try;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import se.inera.intyg.intygsbestallning.common.domain.Bestallning;
import se.inera.intyg.intygsbestallning.common.domain.BestallningStatus;
import se.inera.intyg.intygsbestallning.common.dto.ListBestallningDirection;
import se.inera.intyg.intygsbestallning.common.dto.ListBestallningarQuery;
import se.inera.intyg.intygsbestallning.common.dto.ListBestallningarResult;
import se.inera.intyg.intygsbestallning.persistence.entity.BestallningEntity;
import se.inera.intyg.intygsbestallning.persistence.entity.InvanareEntity;
import se.inera.intyg.intygsbestallning.persistence.entity.QBestallningEntity;
import se.inera.intyg.intygsbestallning.persistence.entity.VardenhetEntity;
import se.inera.intyg.intygsbestallning.persistence.repository.BestallningRepository;
import se.inera.intyg.intygsbestallning.persistence.repository.InvanareRepository;
import se.inera.intyg.intygsbestallning.persistence.repository.VardenhetRepository;

@Service
@Transactional
public class BestallningPersistenceServiceImpl implements BestallningPersistenceService {

    private BestallningRepository bestallningRepository;
    private InvanareRepository invanareRepository;
    private VardenhetRepository vardenhetRepository;

    public BestallningPersistenceServiceImpl(
            BestallningRepository bestallningRepository,
            InvanareRepository invanareRepository,
            VardenhetRepository vardenhetRepository) {
        this.bestallningRepository = bestallningRepository;
        this.invanareRepository = invanareRepository;
        this.vardenhetRepository = vardenhetRepository;
    }

    @Override
    public Bestallning saveNewBestallning(Bestallning bestallning) {

        var invanareEntity = InvanareEntity.Factory.toEntity(bestallning.getInvanare());
        if (Objects.isNull(invanareEntity.getId())) {
            invanareEntity = invanareRepository.saveAndFlush(invanareEntity);
        }

        var vardenhetEntity = VardenhetEntity.Factory.toEntity(bestallning.getVardenhet());
        if (Objects.isNull(vardenhetEntity.getId())) {
            vardenhetEntity = vardenhetRepository.saveAndFlush(vardenhetEntity);
        }

        var bestallningEntity = BestallningEntity.Factory.toEntity(bestallning, invanareEntity, vardenhetEntity);

        return BestallningEntity.Factory.toDomain(bestallningRepository.save(bestallningEntity));
    }

    @Override
    public Bestallning updateBestallning(Bestallning bestallning) {
        var bestallningEntity = BestallningEntity.Factory.toEntity(bestallning);
        bestallningRepository.save(bestallningEntity);
        return BestallningEntity.Factory.toDomain(bestallningEntity);
    }

    @Override
    public void deleteBestallning(Bestallning bestallning) {
        var bestallningEntity = BestallningEntity.Factory.toEntity(bestallning);
        bestallningRepository.delete(bestallningEntity);
    }

    @Override
    public ListBestallningarResult listBestallningar(ListBestallningarQuery query) {


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
        if (Objects.nonNull(query.getTextSearch())) {
            pb.and(textPredicate(query.getTextSearch()));
        }

        Sort sortRequest;
        if (query.getSortDirection() == ListBestallningDirection.ASC) {
            sortRequest = Sort.by(query.getSortColumn().getKolumn()).ascending();
        } else {
            sortRequest = Sort.by(query.getSortColumn().getKolumn()).descending();
        }

        var pageResult = bestallningRepository.findAll(pb.getValue(), PageRequest.of(query.getPageIndex(), query.getLimit(), sortRequest));

        var bestallningar = pageResult.get().map(BestallningEntity.Factory::toDomain).collect(Collectors.toList());

        return ListBestallningarResult.Factory.toDto(
                bestallningar,
                pageResult.getNumber(),
                pageResult.getNumber() * pageResult.getSize() + 1,
                pageResult.getNumber() * pageResult.getSize() + pageResult.getNumberOfElements(),
                pageResult.getTotalPages(),
                pageResult.getNumberOfElements(),
                pageResult.getTotalElements(),
                query.getSortColumn(),
                query.getSortDirection());
    }

    @Override
    public Optional<Bestallning> getBestallningByIdAndHsaIdAndOrgId(Long id, String hsaId, String orgNrVardgivare) {
        var pb = new BooleanBuilder();
        var qe = QBestallningEntity.bestallningEntity;

        pb.and(qe.id.eq(id));

        if (Objects.nonNull(hsaId)) {
            pb.and(qe.vardenhet.hsaId.eq(hsaId));
        }
        if (Objects.nonNull(orgNrVardgivare)) {
            pb.and(qe.vardenhet.organisationId.eq(orgNrVardgivare));
        }

        return bestallningRepository.findOne(pb.getValue())
                .map(BestallningEntity.Factory::toDomain);
    }

    //
    private Predicate textPredicate(String text) {
        var qe = QBestallningEntity.bestallningEntity;
        var id = Longs.tryParse(text);
        if (Objects.nonNull(id)) {
            return qe.id.eq(id);
        }
        var status = Enums.getIfPresent(BestallningStatus.class, text);
        if (status.isPresent()) {
            return qe.status.eq(status.get());
        }
        var date = Try.of(() -> LocalDate.parse(text, DateTimeFormatter.ISO_LOCAL_DATE));
        if (date.isSuccess()) {
            var from = date.get().atStartOfDay();
            return qe.ankomstDatum.between(from, from.plusDays(1).minus(Duration.ofMillis(1L)));
        }
        return new BooleanBuilder(qe.invanare.fornamn.containsIgnoreCase(text))
                .or(qe.invanare.mellannamn.containsIgnoreCase(text))
                .or(qe.invanare.efternamn.containsIgnoreCase(text))
                .or(qe.invanare.personId.containsIgnoreCase(text))
                .getValue();
    }

}
