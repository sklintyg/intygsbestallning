package se.inera.intyg.intygsbestallning.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.inera.intyg.intygsbestallning.persistence.entity.BestallningEntity;

@Repository
public interface BestallningRepository extends JpaRepository<BestallningEntity, Long> {

}
