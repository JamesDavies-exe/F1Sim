package com.davies.F1Sim.Entities;

import jakarta.persistence.*;

import java.util.List;
@Entity
public class Customcircuit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long circuitId;

    String name;

    @OneToMany(mappedBy = "customcircuit")
    List<Question> questions;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    public Long getCircuitId() {
        return circuitId;
    }

    public void setCircuitId(Long circuitId) {
        this.circuitId = circuitId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
