package com.davies.F1Sim.Repos;

import com.davies.F1Sim.Entities.Circuit;
import com.davies.F1Sim.Entities.Score;
import com.davies.F1Sim.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreRepo extends JpaRepository<Score, Long> {
    List<Score> findByCircuitOrderByPointsDesc(Circuit circuit);

    Score findByCircuitAndUser(Circuit circuit, User user);
}
