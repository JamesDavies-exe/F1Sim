package com.davies.F1Sim.Services;

import com.davies.F1Sim.DTO.CircuitDTO;
import com.davies.F1Sim.DTO.NewCircuitDTO;
import com.davies.F1Sim.Entities.Circuit;
import com.davies.F1Sim.Entities.Question;
import com.davies.F1Sim.Entities.Score;
import com.davies.F1Sim.Entities.User;
import com.davies.F1Sim.Repos.CircuitRepo;
import com.davies.F1Sim.Repos.QuestionRepo;
import com.davies.F1Sim.Repos.ScoreRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class CircuitService {
    @Autowired
    CircuitRepo circuitRepo;
    @Autowired
    ScoreRepo scoreRepo;
    @Autowired
    QuestionRepo questionRepo;
    public List<CircuitDTO> getAllCircuits(User user) {
        List<Circuit> circuits = circuitRepo.findAll();
        List<CircuitDTO> circuitDTOList = new ArrayList<>();
        circuits.forEach(circuit -> {
            boolean done = scoreRepo.findByCircuitAndUser(circuit, user) != null;
            CircuitDTO circuitDTO = new CircuitDTO(circuit.getCircuitId(), circuit.getName(), done);
            circuitDTOList.add(circuitDTO);
        });
        return circuitDTOList;
    }

    public Circuit getCircuit(int id) {
        return circuitRepo.findById(Long.valueOf(id)).get();
    }

    public void updateCircuit(int id, NewCircuitDTO newInfo) {
        Circuit circuit = circuitRepo.findByCircuitId((long) id);
        circuit.setName(newInfo.title());
        circuit.setQuestions(newInfo.questions());
        circuitRepo.save(circuit);
    }

    public void deleteCircuitById(Long id) {
        Circuit circuit = circuitRepo.findByCircuitId(id);
        if (circuit == null){
            throw new RuntimeException("Este circuito no existe");
        }
        List<Question> questions = circuit.getQuestions();
        List<Score> scores = circuit.getScores();

        scores.forEach(score -> {
            scoreRepo.delete(score);
        });
        questions.forEach(question -> {
            questionRepo.delete(question);
        });
        circuitRepo.delete(circuit);
    }

    public void addCircuit(NewCircuitDTO body) {
        Circuit newCircuit = new Circuit();
        newCircuit.setName(body.title());
        Circuit addedCircuit = circuitRepo.save(newCircuit);
        body.questions().forEach(question -> {
            question.setCircuit(addedCircuit);
            questionRepo.save(question);
        });
    }
}
