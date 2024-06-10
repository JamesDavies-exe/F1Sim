package com.davies.F1Sim.Services;

import com.davies.F1Sim.DTO.CircuitDTO;
import com.davies.F1Sim.Entities.Circuit;
import com.davies.F1Sim.Entities.User;
import com.davies.F1Sim.Repos.CircuitRepo;
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
}
