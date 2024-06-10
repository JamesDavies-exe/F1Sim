package com.davies.F1Sim.Controllers;

import com.davies.F1Sim.DTO.ClasificationDTO;
import com.davies.F1Sim.DTO.QuestionDTO;
import com.davies.F1Sim.DTO.ScoreDTO;
import com.davies.F1Sim.Entities.Question;
import com.davies.F1Sim.Entities.Score;
import com.davies.F1Sim.Entities.User;
import com.davies.F1Sim.Exceptions.ScoreExistsException;
import com.davies.F1Sim.Services.QuestionService;
import com.davies.F1Sim.Services.ScoreService;
import com.davies.F1Sim.Services.TokenService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class QuizController {
    @Autowired
    QuestionService questionService;
    @Autowired
    ScoreService scoreService;
    @Autowired
    TokenService tokenService;

    @GetMapping("/getQuestions/{id}")
    @CrossOrigin
    public List<QuestionDTO> getQuestions (@PathVariable int id){
        return questionService.getQuestions(id);
    }

    @PostMapping("/saveScore")
    @CrossOrigin
    public List<ScoreDTO> saveScore(@RequestHeader("Authorization") String token, @RequestBody ClasificationDTO info, HttpServletResponse response){
        User user = tokenService.getUserFromToken(token);
        try {
            return scoreService.saveScore(user, info.score(), (long) info.circuitId());
        } catch (ScoreExistsException e){
            response.setStatus(409);
        }
        return null;
    }
}