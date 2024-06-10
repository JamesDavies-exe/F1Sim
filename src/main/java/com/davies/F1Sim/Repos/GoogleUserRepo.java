package com.davies.F1Sim.Repos;

import com.davies.F1Sim.Entities.GoogleUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoogleUserRepo extends JpaRepository<GoogleUser, Long> {
}
