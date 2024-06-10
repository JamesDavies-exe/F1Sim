package com.davies.F1Sim.Services;

import com.davies.F1Sim.DTO.ScoreDTO;
import com.davies.F1Sim.Entities.Circuit;
import com.davies.F1Sim.Entities.Score;
import com.davies.F1Sim.Entities.User;
import com.davies.F1Sim.Exceptions.ScoreExistsException;
import com.davies.F1Sim.Repos.CircuitRepo;
import com.davies.F1Sim.Repos.ScoreRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScoreService {
    @Autowired
    private ScoreRepo scoreRepository;
    @Autowired
    private CircuitRepo circuitRepository;

    public List<ScoreDTO> saveScore(User user, int points, Long circuitId) throws ScoreExistsException {
        Circuit circuit = circuitRepository.findById(circuitId).orElseThrow();
        if (scoreRepository.findByCircuitAndUser(circuit, user) != null){
            throw new ScoreExistsException(user.getName() + " ya ha participado en " + circuit.getName());
        }
        Score score = new Score();
        score.setUser(user);
        score.setPoints(points);
        score.setCircuit(circuit);
        scoreRepository.save(score);
        return getRanking(circuitId);
    }

    public List<ScoreDTO> getRanking(Long circuitId) {
        Circuit circuit = circuitRepository.findById(circuitId).orElseThrow();
        List<Score> circuitList =  scoreRepository.findByCircuitOrderByPointsDesc(circuit);
        List<ScoreDTO> scoreDTOList = new ArrayList<>();
        circuitList.forEach(score -> {
            ScoreDTO scoreDTO = new ScoreDTO(score.getUser(), score.getPoints());
            scoreDTOList.add(scoreDTO);
        });
        return scoreDTOList;
    }
}
