package com.davies.F1Sim.Controllers;

import com.davies.F1Sim.DTO.CircuitDTO;
import com.davies.F1Sim.Entities.Circuit;
import com.davies.F1Sim.Services.CircuitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CircuitController {

    @Autowired
    CircuitService circuitService;

    @GetMapping("/getCircuits")
    @CrossOrigin
    public List<CircuitDTO> getCircuits(){
        return circuitService.getAllCircuits();
    }
}
