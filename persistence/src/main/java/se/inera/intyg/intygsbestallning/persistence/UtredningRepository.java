package se.inera.intyg.intygsbestallning.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UtredningRepository extends JpaRepository<UtredningEntity, Long> {
}
