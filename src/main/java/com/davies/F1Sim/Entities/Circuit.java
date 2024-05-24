package com.davies.F1Sim.Entities;

import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
public class Circuit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long circuitId;

    String name;

    @OneToMany(mappedBy = "circuit")
    List<Question> questions;

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
}