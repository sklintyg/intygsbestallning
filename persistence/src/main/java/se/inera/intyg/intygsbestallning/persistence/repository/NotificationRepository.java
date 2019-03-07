package se.inera.intyg.intygsbestallning.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import se.inera.intyg.intygsbestallning.persistence.entity.SkickadNotifieringEntity;

import java.util.Optional;

@Transactional(value = "transactionManager", readOnly = false)
public interface NotificationRepository extends JpaRepository<SkickadNotifieringEntity, String> {

    Optional<SkickadNotifieringEntity> findById(long id);

}
