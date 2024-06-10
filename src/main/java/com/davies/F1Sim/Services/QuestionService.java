package com.davies.F1Sim.Services;

import com.davies.F1Sim.DTO.ClasificationDTO;
import com.davies.F1Sim.DTO.QuestionDTO;
import com.davies.F1Sim.Entities.Circuit;
import com.davies.F1Sim.Entities.Question;
import com.davies.F1Sim.Entities.User;
import com.davies.F1Sim.Repos.CircuitRepo;
import com.davies.F1Sim.Repos.QuestionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    QuestionRepo questionRepo;

    @Autowired
    CircuitRepo circuitRepo;

    @Autowired
    TokenService tokenService;

    public List<Question> getAllQuestions() {
        return questionRepo.findAll();
    }

    public List<QuestionDTO> getQuestions(int id) {
        Circuit circuit = circuitRepo.findByCircuitId((long) id);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        circuit.getQuestions().forEach(question -> {
            QuestionDTO questionDTO = new QuestionDTO(question.getQuestionId(), question.getQuestion(), question.getOptions(),
                    question.getCorrectOption());
            questionDTOList.add(questionDTO);
        });
        return questionDTOList;
    }
}
