package questions;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import player.ActionPlayer;

public class QuestionPool {
    private static JsonObject gameTexts;
    private static Random rand = new Random();

    static {
        try {
            String content = new String(Files.readAllBytes(Paths.get("src/resources/game_texts.json")), StandardCharsets.UTF_8);
            Gson gson = new Gson();
            gameTexts = gson.fromJson(content, JsonObject.class);
        } catch (Exception e) {
            System.out.println("Error cargando game_texts.json en QuestionPool: " + e.getMessage());
        }
    }

    // Selecciona y lanza una pregunta aleatoria para el nivel dado ("Trainee", "Junior" o "Senior")
    public static boolean askQuestionForLevel(String level) {
        String key = level.toLowerCase(); // Las claves en el JSON son: "trainee", "junior", "senior"
        JsonObject dialogues = gameTexts.getAsJsonObject("dialogues");
        JsonObject levelObj = dialogues.getAsJsonObject(key);
        if (levelObj == null) {
            System.out.println("No se encontraron preguntas para el nivel " + level);
            return true; // Si no hay preguntas, asumimos acierto
        }
        JsonArray questionsArray = levelObj.getAsJsonArray("questions");
        int index = rand.nextInt(questionsArray.size());
        JsonObject questionObj = questionsArray.get(index).getAsJsonObject();

        String question = questionObj.get("question").getAsString();
        JsonArray answersArray = questionObj.getAsJsonArray("answers");
        String[] answers = new String[answersArray.size()];
        for (int i = 0; i < answersArray.size(); i++) {
            answers[i] = answersArray.get(i).getAsString();
        }
        int correct = questionObj.get("correct").getAsInt();
        JsonArray feedbackArray = questionObj.getAsJsonArray("feedback");
        String[] feedback = new String[feedbackArray.size()];
        for (int i = 0; i < feedbackArray.size(); i++) {
            feedback[i] = feedbackArray.get(i).getAsString();
        }

        return ActionPlayer.askQuestion(question, answers, correct, feedback);
    }
}
