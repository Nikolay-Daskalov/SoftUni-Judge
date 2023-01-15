package com.trading212.judge.model.dto.task;

import com.trading212.judge.model.dto.submission.AnswerCases;

import java.util.List;

public record TaskAnswersDTO(List<AnswerCases> cases) {
}
