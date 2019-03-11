package se.inera.intyg.intygsbestallning.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.inera.intyg.intygsbestallning.persistence.entity.UtredningEntity;

@Repository
public interface UtredningRepository extends JpaRepository<UtredningEntity, Long> {
}
