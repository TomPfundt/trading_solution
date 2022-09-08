package com.sportsbet.fanduel.trading.solution.persistence.repository;

import com.sportsbet.fanduel.trading.solution.persistence.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
}
