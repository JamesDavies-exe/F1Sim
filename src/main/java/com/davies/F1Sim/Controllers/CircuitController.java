package com.davies.F1Sim.Controllers;

import com.davies.F1Sim.DTO.CircuitDTO;
import com.davies.F1Sim.DTO.ScoreDTO;
import com.davies.F1Sim.Entities.Circuit;
import com.davies.F1Sim.Entities.User;
import com.davies.F1Sim.Exceptions.UserExistsException;
import com.davies.F1Sim.Services.CircuitService;
import com.davies.F1Sim.Services.ScoreService;
import com.davies.F1Sim.Services.TokenService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.DescriptorKey;
import java.util.List;
import java.util.Objects;

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
    public List<CircuitDTO> getCircuits(@RequestHeader("Authorization") String token) throws UserExistsException {
        System.out.println(token);
        User user = tokenService.getUserFromToken(token);
        if (user == null){
            throw new UserExistsException("No hay ningun usuario registrado");
        }
        return circuitService.getAllCircuits(user);
    }
    @GetMapping("/getRanking/{id}")
    @CrossOrigin
    public List<ScoreDTO> getRanking(@PathVariable("id") int id){
        return scoreService.getRanking((long) id);
    }

    @GetMapping("/getCircuit/{id}")
    @CrossOrigin
    public Circuit getCircuit(@PathVariable("id") int id){
        return circuitService.getCircuit(id);
    }

    @PutMapping("/updateCircuit/{circuitId}")
    @CrossOrigin
    public void updateCircuit(@RequestHeader("Authorization") String token, @RequestBody String editedTitle, @PathVariable("circuitId") int id, HttpServletResponse response){
        if (tokenService.getUserFromToken(token).getRole().equals("admin")){
            circuitService.updateCircuit(id, editedTitle);
        }else {
            response.setStatus(409);
        }
    }

    @DeleteMapping("/deleteCircuit/{circuitId}")
    @CrossOrigin
    public void deleteCircuit(@RequestHeader("Authorization") String token, @PathVariable("circuitId") Long id, HttpServletResponse response){
        if(token == null){
            response.setStatus(409);
        }
        circuitService.deleteCircuitById(id);
    }


}
