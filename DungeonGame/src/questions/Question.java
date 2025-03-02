package questions;

public class Question {
    private String question;
    private String[] answers;
    private int correct; // Índice de la respuesta correcta
    private String[] feedback;

    public Question(String question, String[] answers, int correct, String[] feedback) {
        this.question = question;
        this.answers = answers;
        this.correct = correct;
        this.feedback = feedback;
    }

    public String getQuestion() {
        return question;
    }

    public String[] getAnswers() {
        return answers;
    }

    public int getCorrect() {
        return correct;
    }

    public String[] getFeedback() {
        return feedback;
    }
}
