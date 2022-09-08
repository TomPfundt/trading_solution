package com.sportsbet.fanduel.trading.solution.persistence.repository;

import com.sportsbet.fanduel.trading.solution.persistence.entity.League;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeagueRepository extends JpaRepository<League, Long> {
}
