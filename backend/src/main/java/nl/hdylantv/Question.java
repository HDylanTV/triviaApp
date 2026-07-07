package nl.hdylantv;

import java.util.List;

public class Question {

    private final String question;
    private final String correctAnswer;
    private final List<String> possibleAnswers;

    boolean isAnswered = false;
    boolean answerCorrect;

    private final int maxScore;

    public Question(String question, List<String> possibleAnswers, String correctAnswer, int maxScore) {
        this.question = question;
        this.possibleAnswers = possibleAnswers;
        this.correctAnswer = correctAnswer;
        this.maxScore = maxScore;
    }

    public boolean isCorrect(String answer) {
        return this.correctAnswer.equals(answer);
    }

    public void markAsCorrect() {
        this.isAnswered = true;
        this.answerCorrect = true;
    }

    public void markAsWrong() {
        this.isAnswered = true;
        this.answerCorrect = false;
    }

    public String getQuestion() {
        return this.question;
    }

    public List<String> getPossibleAnswers() {
        return this.possibleAnswers;
    }

    public String getCorrectAnswer() {
        return this.correctAnswer;
    }

    public int getMaxScore() {
        return this.maxScore;
    }

    @Override
    public String toString() {
        return
            "question: " + this.question + "\n" +
            "answers: " + this.possibleAnswers + "\n" +
            "correct: " + this.correctAnswer + "\n" +
            "isAnswered: " + this.isAnswered + "\n" +
            "isCorrect: " + this.answerCorrect + "\n";
    }
}
