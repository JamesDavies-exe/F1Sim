package com.davies.F1Sim.Repos;

import com.davies.F1Sim.Entities.Customcircuit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomCircuitRepo extends JpaRepository<Customcircuit, Long> {
}
