package se.inera.intyg.intygsbestallning.persistence.repository;

import com.google.common.base.Enums;
import com.google.common.primitives.Longs;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import io.vavr.control.Try;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import se.inera.intyg.intygsbestallning.common.domain.BestallningStatus;
import se.inera.intyg.intygsbestallning.persistence.entity.BestallningEntity;
import se.inera.intyg.intygsbestallning.persistence.entity.QBestallningEntity;

@Repository
public interface BestallningRepository extends JpaRepository<BestallningEntity, Long>,
        QuerydslPredicateExecutor<BestallningEntity>,
        QuerydslBinderCustomizer<QBestallningEntity> {

    @Query(value =  "select b from BestallningEntity b " +
                    "join b.invanare i " +
                    "join b.vardenhet v " +
                    "where (b.status in :statusar) " +
                    "and (:textSearch is null " +
                    "or (:textSearch is not null and (b.intygTyp like %:textSearch%) " +
                        "or (i.personId like %:textSearch% " +
                        "or upper(concat(trim(i.fornamn), ' ' , trim(i.efternamn))) like %:textSearch%) " +
                        "or upper(concat(trim(i.fornamn), ' ' , trim(i.mellannamn), ' ', trim(i.efternamn))) like %:textSearch% " +
                        "or (:id is not null and b.id = :id) " +
                        "or (:status is not null and b.status = :status) " +
                        "or (:ankomstDatumFrom is not null and (b.ankomstDatum between :ankomstDatumFrom and :ankomstDatumTo))))")
    Page<BestallningEntity> findByQuery(
            @Param("statusar") List<BestallningStatus> statusar,
            @Param("textSearch") String textSearch,
            @Param("id") Long id,
            @Param("status") BestallningStatus bestallningStatus,
            @Param("ankomstDatumFrom") LocalDateTime ankomstDatumFrom,
            @Param("ankomstDatumTo") LocalDateTime ankomstDatumTo,
            Pageable pageable);

    @Override
    default void customize(QuerydslBindings bindings, QBestallningEntity entity) {
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        bindings.bind(entity.textSearch).first((path, value) -> {
            var id = Longs.tryParse(value);
            if (Objects.nonNull(id)) {
                return entity.id.eq(id);
            }
            var status = Enums.getIfPresent(BestallningStatus.class, value);
            if (status.isPresent()) {
                return entity.status.eq(status.get());
            }
            var type = entity.intygTyp;
            if (Objects.nonNull(type)) {
                return entity.intygTyp.eq(type);
            }
            var date = Try.of(() -> LocalDate.parse(value, formatter));
            if (date.isSuccess()) {
                 var from = date.get().atStartOfDay();
                 var to = from.plusDays(1);
                 return entity.ankomstDatum.between(from, to);
            }
            return new BooleanBuilder(entity.invanare.fornamn.containsIgnoreCase(value))
                    .or(entity.invanare.mellannamn.containsIgnoreCase(value))
                    .or(entity.invanare.efternamn.containsIgnoreCase(value))
                    .getValue();

        });
    }

    Page<BestallningEntity> findAll(Predicate predicate, Pageable pageable);
}
