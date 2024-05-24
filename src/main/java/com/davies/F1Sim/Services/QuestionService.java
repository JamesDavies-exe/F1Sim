package com.davies.F1Sim.Services;

import com.davies.F1Sim.Entities.Circuit;
import com.davies.F1Sim.Entities.Question;
import com.davies.F1Sim.Repos.CircuitRepo;
import com.davies.F1Sim.Repos.QuestionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {
    @Autowired
    QuestionRepo questionRepo;

    @Autowired
    CircuitRepo circuitRepo;

    public List<Question> getAllQuestions() {
        return questionRepo.findAll();
    }

    public List<Question> getQuestions(int id) {
        Circuit circuit = circuitRepo.findByCircuitId((long) id);
        System.out.println(circuit.getQuestions());
        return circuit.getQuestions();
    }
}
