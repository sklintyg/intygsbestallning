package se.inera.intyg.intygsbestallning.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.inera.intyg.intygsbestallning.persistence.entity.HandelseEntity;

public interface HandelseRepository extends JpaRepository<HandelseEntity, Long> {
}
