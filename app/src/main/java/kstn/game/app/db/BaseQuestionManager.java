package kstn.game.app.db;

import kstn.game.logic.db.Question;
import kstn.game.logic.db.QuestionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BaseQuestionManager implements QuestionManager {
    private final Random random = new Random();
    private final List<Integer> usedQuestions = new ArrayList<>();

    public BaseQuestionManager() {}

    protected List<Integer> getAllIdsByCategories(List<String> categories) {
        return null;
    }

    protected Question getQuestionById(int id) {
        return null;
    }

    @Override
    public Question getRandomQuestion(List<String> categories) {
        List<Integer> idList = getAllIdsByCategories(categories);
        int size = idList.size();
        int id;
        while (true) {
            int index = random.nextInt(size);
            id = idList.get(index);
            if (usedQuestions.contains(new Integer(id)))
                continue;
            break;
        }
        usedQuestions.add(id);
        return getQuestionById(id);
    }
}
