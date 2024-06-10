package com.davies.F1Sim.Entities;

import jakarta.persistence.*;

import javax.annotation.processing.Generated;

@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long questionId;
    String question;
    String options;
    String correctOption;

    @ManyToOne
    @JoinColumn(name = "circuit_id")
    Circuit circuit;

    @ManyToOne
    @JoinColumn(name = "customcircuit_id")
    Customcircuit customcircuit;

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Circuit getCircuit() {
        return circuit;
    }

    public void setCircuit(Circuit circuit) {
        this.circuit = circuit;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getCorrectOption() {
        return correctOption;
    }

    public void setCorrectOption(String correctOption) {
        this.correctOption = correctOption;
    }
}
