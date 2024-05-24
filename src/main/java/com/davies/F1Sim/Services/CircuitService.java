package com.davies.F1Sim.Services;

import com.davies.F1Sim.DTO.CircuitDTO;
import com.davies.F1Sim.Entities.Circuit;
import com.davies.F1Sim.Repos.CircuitRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CircuitService {
    @Autowired
    CircuitRepo circuitRepo;
    public List<CircuitDTO> getAllCircuits() {
        List<Circuit> circuits = circuitRepo.findAll();
        List<CircuitDTO> circuitDTOList = new ArrayList<>();
        circuits.forEach(circuit -> {
            CircuitDTO circuitDTO = new CircuitDTO(circuit.getCircuitId(), circuit.getName());
            circuitDTOList.add(circuitDTO);
        });
        return circuitDTOList;
    }
}
