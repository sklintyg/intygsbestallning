package se.inera.intyg.intygsbestallning.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import se.inera.intyg.intygsbestallning.common.domain.BestallningStatus;
import se.inera.intyg.intygsbestallning.persistence.entity.BestallningEntity;

@Repository
public interface BestallningRepository extends JpaRepository<BestallningEntity, Long> {

    @Query(value =  "select b from BestallningEntity b " +
                    "join b.invanare i " +
                    "join b.vardenhet v " +
                    "join b.handelser h " +
                    "join b.notifieringar n " +
                    "where b.status in :statusar")
    Page<BestallningEntity> findByQuery(@Param("statusar") List<BestallningStatus> statusar, Pageable pageable);
}
