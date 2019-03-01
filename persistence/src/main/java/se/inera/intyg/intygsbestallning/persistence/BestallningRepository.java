package se.inera.intyg.intygsbestallning.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BestallningRepository extends JpaRepository<Bestallning, Long> {
}
