package se.inera.intyg.intygsbestallning.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.inera.intyg.intygsbestallning.persistence.entity.UtredningEntity;

public interface UtredningRepository extends JpaRepository<UtredningEntity, Long> {
}
