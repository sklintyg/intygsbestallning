package se.inera.intyg.intygsbestallning.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import se.inera.intyg.intygsbestallning.common.domain.BestallningStatus;
import se.inera.intyg.intygsbestallning.common.domain.IntygTyp;
import se.inera.intyg.intygsbestallning.persistence.entity.BestallningEntity;

@Repository
public interface BestallningRepository extends JpaRepository<BestallningEntity, Long> {

    @Query(value =  "select b from BestallningEntity b " +
                    "join b.invanare i " +
                    "join b.vardenhet v " +
                    "join b.handelser h " +
                    "join b.notifieringar n " +
                    "where (b.status in :statusar) " +
                    "and (:textSearch is not null and (i.personId = :textSearch or upper(concat(trim(i.fornamn), ' ' , trim(i.efternamn))) like %:textSearch%) or upper(concat(trim(i.fornamn), ' ' , trim(i.mellannamn), ' ', trim(i.efternamn))) like %:textSearch% " +
                    "or (:id is not null and b.id = :id) " +
                    "or (:intygTyp is not null and b.intygTyp = :intygTyp) " +
                    "or (:ankomstDatumFrom is not null and (b.ankomstDatum between :ankomstDatumFrom and :ankomstDatumTo)))")
    Page<BestallningEntity> findByQuery(
            @Param("statusar") List<BestallningStatus> statusar,
            @Param("textSearch") String textSearch,
            @Param("id") Long id,
            @Param("intygTyp") IntygTyp intygTyp,
            @Param("ankomstDatumFrom") LocalDateTime ankomstDatumFrom,
            @Param("ankomstDatumTo") LocalDateTime ankomstDatumTo,
            Pageable pageable);
}
