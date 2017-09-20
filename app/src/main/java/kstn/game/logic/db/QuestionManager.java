package kstn.game.logic.db;

import java.util.List;

public interface QuestionManager {
    Question getRandomQuestion(List<String> categories);
}
