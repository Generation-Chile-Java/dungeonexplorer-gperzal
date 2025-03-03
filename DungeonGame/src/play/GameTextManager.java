package play;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class GameTextManager {
    // Variable para almacenar los textos del juego cargados desde JSON
    private JsonObject gameTexts;

    // Carga el archivo JSON de textos
    public void loadGameTexts() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("src/resources/game_texts.json")), StandardCharsets.UTF_8);
            Gson gson = new Gson();
            gameTexts = gson.fromJson(content, JsonObject.class);
        } catch (Exception e) {
            System.out.println("Error cargando game_texts.json: " + e.getMessage());
            System.exit(1);
        }
    }

    // Obtener los textos del juego
    public JsonObject getGameTexts() {
        return gameTexts;
    }
}