package com.davies.F1Sim.Controllers;

import com.davies.F1Sim.DTO.CircuitDTO;
import com.davies.F1Sim.DTO.ScoreDTO;
import com.davies.F1Sim.Entities.Circuit;
import com.davies.F1Sim.Entities.User;
import com.davies.F1Sim.Services.CircuitService;
import com.davies.F1Sim.Services.ScoreService;
import com.davies.F1Sim.Services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CircuitController {

    @Autowired
    CircuitService circuitService;
    @Autowired
    TokenService tokenService;
    @Autowired
    ScoreService scoreService;

    @GetMapping("/getCircuits")
    @CrossOrigin
    public List<CircuitDTO> getCircuits(@RequestHeader("Authorization") String token){
        System.out.println(token);
        User user = tokenService.getUserFromToken(token);
        return circuitService.getAllCircuits(user);
    }
    @GetMapping("/getRanking/{id}")
    @CrossOrigin
    public List<ScoreDTO> getRanking(@PathVariable("id") int id){
        return scoreService.getRanking((long) id);
    }
}
