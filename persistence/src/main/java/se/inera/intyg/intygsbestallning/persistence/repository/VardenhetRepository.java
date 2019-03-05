package se.inera.intyg.intygsbestallning.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.inera.intyg.intygsbestallning.persistence.entity.VardenhetEntity;

@Repository
public interface VardenhetRepository extends JpaRepository<VardenhetEntity, Long> {
}
