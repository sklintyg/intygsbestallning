package se.inera.intyg.intygsbestallning.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import se.inera.intyg.intygsbestallning.persistence.entity.InvanareEntity;

public interface InvanareRepository extends JpaRepository<InvanareEntity, Long> {
    Optional<InvanareEntity> findByPersonId(String personId);
}
