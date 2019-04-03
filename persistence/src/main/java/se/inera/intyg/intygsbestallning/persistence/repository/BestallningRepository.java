package se.inera.intyg.intygsbestallning.persistence.repository;

import com.querydsl.core.types.Predicate;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import se.inera.intyg.intygsbestallning.persistence.entity.BestallningEntity;

@Repository
public interface BestallningRepository extends JpaRepository<BestallningEntity, Long>,
        QuerydslPredicateExecutor<BestallningEntity> {
    /**
     * Finds all bestallning matching predicate.
     *
     * @param predicate the predicate.
     * @param pageable pageable info.
     * @return a page with entities.
     */
    Page<BestallningEntity> findAll(Predicate predicate, Pageable pageable);

    /**
     * Finds at most one bestallning matching the predicate.
     *
     * @param predicate the predicate.
     * @return the optional with entity.
     */
    Optional<BestallningEntity> findOne(Predicate predicate);

}
