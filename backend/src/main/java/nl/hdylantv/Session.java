package nl.hdylantv;

import jakarta.ws.rs.core.Response;

import java.util.Map;
import java.util.UUID;

public class Session {

    UUID sessionId;

    private Quiz currentQuiz;

    private int quizzesDone = 0;
    private float averageGrade = 0;

    public Session() {
        this.sessionId = UUID.randomUUID();
    }

    public void generateNewQuiz(QuizConfig config) {
        if (this.currentQuiz == null) {
            this.currentQuiz = new Quiz(config);
        }
        if (this.currentQuiz.isDone()) {
            this.currentQuiz = new Quiz(config);
        }
    }

    public String getCorrectAnswer() {
        return this.currentQuiz.getCurrentQuestionAnswer();
    }

    public boolean isCurrentQuestionAnswered() {
        return this.currentQuiz.isCurrentQuestionAnswered();
    }

    public void submitAnswer(String answerGiven) {
        this.currentQuiz.submitAnswer(answerGiven);
    }

    public Response createQuestionResponse() {
        if (this.currentQuiz.isDone()) {
            return Response.ok()
                .entity(Map.of(
                    "session", this.sessionId,
                    "total_questions", this.currentQuiz.questionList.size(),
                    "questions_correct", this.currentQuiz.getScore()
                ))
                .build();
        }

        Question question = this.currentQuiz.getNextQuestion();

        return Response.ok()
            .entity(Map.of(
                "session", this.sessionId,
                "question", question.getQuestion(),
                "answers", question.getPossibleAnswers(),
                "questions_correct", this.currentQuiz.currentQuizScore,
                "question_number", this.currentQuiz.currentQuestionIdx + 1,
                "total_questions", this.currentQuiz.questionList.size()
            ))
            .build();
    }
}
