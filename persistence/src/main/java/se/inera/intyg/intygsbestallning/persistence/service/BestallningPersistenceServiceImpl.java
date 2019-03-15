package se.inera.intyg.intygsbestallning.persistence.service;

import kotlin.Pair;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.stream.Collectors;
import se.inera.intyg.intygsbestallning.common.domain.Bestallning;
import se.inera.intyg.intygsbestallning.common.domain.IntygTyp;
import se.inera.intyg.intygsbestallning.common.dto.ListBestallningarQuery;
import se.inera.intyg.intygsbestallning.common.dto.ListBestallningarResult;
import se.inera.intyg.intygsbestallning.persistence.entity.BestallningEntity;
import se.inera.intyg.intygsbestallning.persistence.repository.BestallningRepository;

@Service
public class BestallningPersistenceServiceImpl implements BestallningPersistenceService {

    private final BestallningRepository bestallningRepository;

    public BestallningPersistenceServiceImpl(BestallningRepository bestallningRepository) {
        this.bestallningRepository = bestallningRepository;
    }

    @Override
    public Bestallning saveNewBestallning(Bestallning bestallning) {
        var bestallningEntity = BestallningEntity.Factory.toEntity(bestallning);
        return BestallningEntity.Factory.toDomain(bestallningRepository.save(bestallningEntity));
    }

    @Override
    public ListBestallningarResult listBestallningar(ListBestallningarQuery query) {
        var pageRequest = PageRequest.of(query.getPageIndex(), query.getLimit());

        var searchString = query.getTextSearch().toUpperCase();

        var id = getValidLong(query.getTextSearch());

        var intygTyp = getValidIntygTyp(query.getTextSearch());

        var localDateTimeSearch = getValidLocalDatetimeInterval(query.getTextSearch());

        var pageResult = bestallningRepository.findByQuery(
                query.getStatusar(),
                searchString,
                id.orElse(null),
                intygTyp.orElse(null),
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


    private Optional<IntygTyp> getValidIntygTyp(String potentialIntygTypString) {

        if (potentialIntygTypString != null) {

            try {
                return Optional.of(IntygTyp.valueOf(potentialIntygTypString));
            } catch (IllegalArgumentException ex) {
                return Optional.empty();
            }
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
