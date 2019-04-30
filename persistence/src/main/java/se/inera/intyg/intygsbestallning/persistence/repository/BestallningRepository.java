/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.intyg.intygsbestallning.persistence.repository;

import com.querydsl.core.types.Predicate;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import se.inera.intyg.intygsbestallning.persistence.entity.BestallningEntity;

@Repository
public interface BestallningRepository extends JpaRepository<BestallningEntity, Long>,
        QuerydslPredicateExecutor<BestallningEntity> {
    /**
     * Finds all bestallning matching predicate.
     *
     * @param predicate the predicate.
     * @param pageable pageable info.
     * @return a page with entities.
     */
    Page<BestallningEntity> findAll(Predicate predicate, Pageable pageable);

    /**
     * Finds at most one bestallning matching the predicate.
     *
     * @param predicate the predicate.
     * @return the optional with entity.
     */
    Optional<BestallningEntity> findOne(Predicate predicate);
}
