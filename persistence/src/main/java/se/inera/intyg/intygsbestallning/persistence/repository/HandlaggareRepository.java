package se.inera.intyg.intygsbestallning.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.inera.intyg.intygsbestallning.persistence.entity.HandlaggareEntity;

public interface HandlaggareRepository extends JpaRepository<HandlaggareEntity, Long> {
}
