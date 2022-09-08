package com.sportsbet.fanduel.trading.solution.persistence.repository;

import com.sportsbet.fanduel.trading.solution.persistence.entity.Squad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SquadRepository extends JpaRepository<Squad, Long> {
}
