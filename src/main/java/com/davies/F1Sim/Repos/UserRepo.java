package com.davies.F1Sim.Repos;

import com.davies.F1Sim.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    public boolean existsByMail(String mail);

    User findByMailAndPassword(String mail, String password);

    User findByMail(String email);
}
