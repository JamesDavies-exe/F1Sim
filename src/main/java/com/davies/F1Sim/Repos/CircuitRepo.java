package com.davies.F1Sim.Repos;

import com.davies.F1Sim.Entities.Circuit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CircuitRepo extends JpaRepository<Circuit, Long> {
    Circuit findByCircuitId(Long id);

}
