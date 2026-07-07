package nl.hdylantv;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Quiz {

    ArrayList<Question> questionList;
    int currentQuestionIdx;

    int currentQuizScore = 0;

    public Quiz(QuizConfig config) {
        this.questionList = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode rootNode = mapper.readTree(new TriviaApiRequestSender().fetch(config));

            for (JsonNode item : rootNode.path("results")) {
                String question = item.path("question").asText();
                String correctAnswer = item.path("correct_answer").asText();

                List<String> possibleAnswers = new ArrayList<>();
                possibleAnswers.add(correctAnswer);
                for (JsonNode incorrectAnswer : item.path("incorrect_answers")) {
                    possibleAnswers.add(incorrectAnswer.asText());
                }
                Collections.shuffle(possibleAnswers);

                this.questionList.add(new Question(
                    question,
                    possibleAnswers,
                    correctAnswer,
                    1
                ));
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        this.currentQuestionIdx = 0;
    }

    public Question getCurrentQuestion() {
        if (currentQuestionIdx >= questionList.size()) {
            return null;
        }
        return questionList.get(currentQuestionIdx);
    }

    public int getPoints(String answer) {
        Question currentQuestion = getCurrentQuestion();
        if (currentQuestion.isCorrect(answer)) {
            return currentQuestion.getMaxScore();
        }

        return 0;
    }

    public void submitAnswer(String answer) {
        Question question = getCurrentQuestion();
        if (question.isAnswered) {
            return;
        }
        boolean isCorrect = question.isCorrect(answer);
        if (isCorrect) {
            markCurrentQuestionAsCorrect();
        } else {
            markCurrentQuestionAsWrong();
        }
    }

    public void markCurrentQuestionAsCorrect() {
        getCurrentQuestion().markAsCorrect();
        currentQuizScore += 1;
    }

    public void markCurrentQuestionAsWrong() {
        getCurrentQuestion().markAsWrong();
    }

    public boolean isCurrentQuestionAnswered() {
        return getCurrentQuestion().isAnswered;
    }

    public String getCurrentQuestionAnswer() {
        return getCurrentQuestion().getCorrectAnswer();
    }

    public boolean hasNextQuestion() {
        return currentQuestionIdx + 1 < questionList.size();
    }

    public Question getNextQuestion() {
        Question currentQuestion = getCurrentQuestion();
        if (!currentQuestion.isAnswered) {
            return currentQuestion;
        }
        if (!hasNextQuestion()) {
            return null;
        }

        return questionList.get(++currentQuestionIdx);
    }

    public boolean isDone() {
        if (!getCurrentQuestion().isAnswered) {
            return false;
        }

        return !hasNextQuestion();
    }

    public Integer getScore() {
        if (!isDone()) {
            return null;
        }

        return currentQuizScore;
    }
}
