package se.inera.intyg.intygsbestallning.persistence.service;

import com.google.common.collect.MoreCollectors;
import kotlin.Pair;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import se.inera.intyg.intygsbestallning.common.domain.Bestallning;
import se.inera.intyg.intygsbestallning.common.domain.BestallningStatus;
import se.inera.intyg.intygsbestallning.common.domain.IntygTyp;
import se.inera.intyg.intygsbestallning.common.dto.ListBestallningarQuery;
import se.inera.intyg.intygsbestallning.common.dto.ListBestallningarResult;
import se.inera.intyg.intygsbestallning.persistence.entity.BestallningEntity;
import se.inera.intyg.intygsbestallning.persistence.entity.InvanareEntity;
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

    public BestallningPersistenceServiceImpl(BestallningRepository bestallningRepository,
                                             InvanareRepository invanareRepository,
                                             VardenhetRepository vardenhetRepository) {
        this.bestallningRepository = bestallningRepository;
        this.invanareRepository = invanareRepository;
        this.vardenhetRepository = vardenhetRepository;
    }

    @Override
    public Bestallning saveNewBestallning(Bestallning bestallning) {

        var invanareEntity = InvanareEntity.Factory.toEntity(bestallning.getInvanare());
        if (invanareEntity.getId() == null) {
            invanareRepository.save(invanareEntity);
        }

        var vardenhetEntity = VardenhetEntity.Factory.toEntity(bestallning.getVardenhet());
        if (vardenhetEntity.getId() == null) {
            vardenhetRepository.save(vardenhetEntity);
        }

        var bestallningEntity = BestallningEntity.Factory.toEntity(bestallning, invanareEntity, vardenhetEntity);

        return BestallningEntity.Factory.toDomain(bestallningRepository.save(bestallningEntity));
    }

    @Override
    public ListBestallningarResult listBestallningar(ListBestallningarQuery query) {
        var pageRequest = PageRequest.of(query.getPageIndex(), query.getLimit());

        var searchString = Optional.ofNullable(query.getTextSearch()).map(String::toUpperCase);

        var id = getValidLong(query.getTextSearch());

        var intygTyp = getValidTyp(query.getTextSearch(), IntygTyp.class);

        var status = getValidTyp(query.getTextSearch(), BestallningStatus.class);

        var localDateTimeSearch = getValidLocalDatetimeInterval(query.getTextSearch());

        var pageResult = bestallningRepository.findByQuery(
                query.getStatusar(),
                searchString.orElse(null),
                id.orElse(null),
                (IntygTyp) intygTyp.orElse(null),
                (BestallningStatus) status.orElse(null),
                localDateTimeSearch.map(Pair::getFirst).orElse(null),
                localDateTimeSearch.map(Pair::getSecond).orElse(null),
                pageRequest);

        var bestallningar = pageResult.get().map(BestallningEntity.Factory::toDomain)
                .collect(Collectors.toList());

        return new ListBestallningarResult(
                bestallningar,
                pageResult.getNumber(),
                pageResult.getTotalPages(),
                pageResult.getNumberOfElements(),
                pageResult.getTotalElements());
    }

    private Optional<Long> getValidLong(String potentialLongString) {
        if (potentialLongString != null) {
            try {
                return Optional.of(Long.valueOf(potentialLongString));
            } catch (NumberFormatException ex) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    private Optional<Enum<?>> getValidTyp(String potentialIntygTypString, Class<? extends Enum<?>> enumType) {
        if (potentialIntygTypString != null) {
            return Arrays.stream(enumType.getEnumConstants())
                    .filter(e -> e.name().equalsIgnoreCase(potentialIntygTypString))
                    .collect(MoreCollectors.toOptional());
        }
        return Optional.empty();
    }

    private Optional<Pair<LocalDateTime, LocalDateTime>> getValidLocalDatetimeInterval(String potentialLocalDateString) {
        var dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (potentialLocalDateString != null) {
            try {
                var converted = LocalDate.parse(potentialLocalDateString, dateTimeFormatter);
                var from = converted.atStartOfDay();
                var to = converted.plusDays(1).atStartOfDay();
                return Optional.of(new Pair<>(from, to));
            } catch (DateTimeParseException ex) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }
}
