package questions;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import player.ActionPlayer;

public class QuestionPool {
    private static JsonObject gameTexts;
    private static Random rand = new Random();
    // Lista de preguntas para cada nivel
    private static List<Question> traineeQuestions = new ArrayList<>();
    private static List<Question> juniorQuestions = new ArrayList<>();
    private static List<Question> seniorQuestions = new ArrayList<>();

    static {
        try {
            String content = new String(Files.readAllBytes(Paths.get("src/resources/game_texts.json")), StandardCharsets.UTF_8);
            Gson gson = new Gson();
            gameTexts = gson.fromJson(content, JsonObject.class);
            parseQuestions();
        } catch(Exception e) {
            System.out.println("Error cargando game_texts.json en QuestionPool: " + e.getMessage());
        }
    }

    // Parsea las preguntas del JSON y las guarda en listas
    private static void parseQuestions() {
        JsonObject dialogues = gameTexts.getAsJsonObject("dialogues");
        traineeQuestions = parseLevelQuestions(dialogues.getAsJsonObject("trainee"));
        juniorQuestions = parseLevelQuestions(dialogues.getAsJsonObject("junior"));
        seniorQuestions = parseLevelQuestions(dialogues.getAsJsonObject("senior"));
    }

    private static List<Question> parseLevelQuestions(JsonObject levelObj) {
        List<Question> questions = new ArrayList<>();
        if(levelObj == null) return questions;
        JsonArray questionsArray = levelObj.getAsJsonArray("questions");
        for (int i = 0; i < questionsArray.size(); i++) {
            JsonObject qObj = questionsArray.get(i).getAsJsonObject();
            String questionText = qObj.get("question").getAsString();
            JsonArray answersArray = qObj.getAsJsonArray("answers");
            String[] answers = new String[answersArray.size()];
            for (int j = 0; j < answersArray.size(); j++) {
                answers[j] = answersArray.get(j).getAsString();
            }
            int correct = qObj.get("correct").getAsInt();
            JsonArray feedbackArray = qObj.getAsJsonArray("feedback");
            String[] feedback = new String[feedbackArray.size()];
            for (int j = 0; j < feedbackArray.size(); j++) {
                feedback[j] = feedbackArray.get(j).getAsString();
            }
            questions.add(new Question(questionText, answers, correct, feedback));
        }
        return questions;
    }

    // Método para obtener una pregunta aleatoria según el nivel
    public static boolean askQuestionForLevel(String level) {
        List<Question> pool;
        switch(level.toLowerCase()) {
            case "trainee":
                pool = traineeQuestions;
                break;
            case "junior":
                pool = juniorQuestions;
                break;
            case "senior":
                pool = seniorQuestions;
                break;
            default:
                System.out.println("Nivel desconocido: " + level);
                return true;
        }
        if(pool.isEmpty()){
            System.out.println("No se encontraron preguntas para el nivel " + level);
            return true;
        }
        int index = rand.nextInt(pool.size());
        Question q = pool.get(index);
        return ActionPlayer.askQuestion(q.getQuestion(), q.getAnswers(), q.getCorrect(), q.getFeedback());
    }
}
