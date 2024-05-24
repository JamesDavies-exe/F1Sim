package com.davies.F1Sim.Controllers;

import com.davies.F1Sim.Entities.Question;
import com.davies.F1Sim.Services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class QuizController {
    @Autowired
    QuestionService questionService;

    @GetMapping("/getQuestions/{id}")
    @CrossOrigin
    public List<Question> getQuestions (@PathVariable int id){
        return questionService.getQuestions(id);
    }
}