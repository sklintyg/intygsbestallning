package se.inera.intyg.intygsbestallning.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.inera.intyg.intygsbestallning.persistence.entity.NotifieringEntity;

@Repository
public interface NotifieringRepository extends JpaRepository<NotifieringEntity, String> {
}
