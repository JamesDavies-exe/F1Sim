package com.davies.F1Sim.DTO;

import com.davies.F1Sim.Entities.Question;

import java.util.List;

public record NewCircuitDTO(String title, List<Question> questions) {
}
