import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import player.Player;
import rooms.Room;
import rooms.EmptyRoom;
import rooms.TreasureRoom;
import rooms.EnemyRoom;
import questions.QuestionPool;

public class Main {
    private static final int ROWS = 3; // Matriz 3x3
    private static final int COLS = 3;
    private static Room[][] dungeon = new Room[ROWS][COLS]; // Este arreglo puede usarse para asignar eventos en cada celda
    private static Scanner scanner = new Scanner(System.in);
    private static JsonObject gameTexts; // Textos y diálogos del juego
    private static String currentRoomName = "Sala Principal"; // Nombre de la habitación actual

    public static void main(String[] args) {
        loadGameTexts();
        System.out.println("¡Bienvenido al Dungeon Game!");

        // Selección de género y nombre
        System.out.println("Selecciona tu género: 1) Héroe  2) Heroína");
        String genderChoice = scanner.nextLine();
        String gender = genderChoice.equals("1") ? "hero" : "heroine";
        System.out.print("Introduce tu nombre: ");
        String name = scanner.nextLine();

        // Crear al jugador
        Player player = new Player(name, gender);
        System.out.println(getIntroText(player));

        // Inicializar la mazmorra (puedes seguir usando la lógica de salas para acciones con F)
        initDungeon();

        // Iniciar al jugador en el centro de la matriz (posición [1,1])
        int playerRow = 1;
        int playerCol = 1;
        printPlayerPosition(playerRow, playerCol);

        boolean gameOver = false;
        while (!gameOver) {
            // Mostrar información actual del jugador
            System.out.println("\nTu posición actual es " + currentRoomName);
            printPlayerPosition(playerRow, playerCol);
            System.out.println("Vida: " + player.getHealthDisplay());
            System.out.println("Inventario: " + player.getInventory());
            System.out.println("Comandos:");
            System.out.println("  A: Izquierda, W: Arriba, D: Derecha, S: Abajo");
            System.out.println("  F: Acción (interactuar con la sala: tesoros, puertas, enemigos)");
            System.out.println("  V: Abrir Inventario, X: Usar Objeto");
            System.out.print("Ingresa un comando: ");
            String command = scanner.nextLine().toUpperCase();

            switch (command) {
                case "A":
                case "W":
                case "D":
                case "S":
                    int[] newPos = player.move(command, playerRow, playerCol, ROWS, COLS);
                    // Si se mueve, actualizar la posición y mostrar la nueva matriz
                    if (newPos[0] != playerRow || newPos[1] != playerCol) {
                        playerRow = newPos[0];
                        playerCol = newPos[1];
                        printPlayerPosition(playerRow, playerCol);
                        // Si el jugador llega a un borde, podría haber una puerta
                        if (isAtDoor(playerRow, playerCol)) {
                            System.out.println("¡Has llegado a una puerta en el borde de la sala!");
                            System.out.println("Presiona 'F' para interactuar y cambiar de habitación.");
                        }
                    }
                    break;
                case "F":
                    // Al presionar F se realiza la acción de la sala actual:
                    // Puede ser para buscar tesoros, interactuar con enemigos o cambiar de habitación
                    // Aquí puedes implementar la lógica para que si está en el borde y hay puerta, se cambie de sala.
                    Room currentRoom = dungeon[playerRow][playerCol];
                    currentRoom.enter(player);
                    break;
                case "V":
                    System.out.println("Inventario: " + player.getInventory());
                    break;
                case "X":
                    player.useItem();
                    break;
                default:
                    System.out.println("Comando no reconocido.");
                    break;
            }

            // Verificar condiciones de finalización
            if (player.getHealth() <= 0) {
                System.out.println("Has perdido toda tu vida. ¡Game Over! ❌");
                gameOver = true;
            }

            // Ejemplo: Si llegamos a la puerta de salida (puedes definir una condición especial)
            // Aquí podrías chequear si se cumple alguna condición para cambiar de nivel o salir.
            // Por ejemplo, si el jugador encuentra la llave y se mueve a una celda borde.
        }

        // Mostrar mensaje final del juego
        String finalMessage = gameTexts.getAsJsonObject("dialogues")
                .getAsJsonObject("final")
                .get("message")
                .getAsString();
        System.out.println("\n" + finalMessage);
    }

    // Método para cargar los textos del juego desde el archivo JSON con GSON
    private static void loadGameTexts() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("src/resources/game_texts.json")), StandardCharsets.UTF_8);
            Gson gson = new Gson();
            gameTexts = gson.fromJson(content, JsonObject.class);
        } catch (IOException e) {
            System.out.println("Error cargando game_texts.json: " + e.getMessage());
            System.exit(1);
        }
    }

    // Retorna el texto de introducción adaptado al género y nombre del jugador
    private static String getIntroText(Player player) {
        String gender = player.getGender(); // "hero" o "heroine"
        String introTemplate = gameTexts.getAsJsonObject("intro").get(gender).getAsString();
        return introTemplate.replace("{name}", player.getName());
    }

    // Inicializa la mazmorra asignando aleatoriamente salas (enemigo, tesoro o vacía)
    private static void initDungeon() {
        Random rand = new Random();
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                double prob = rand.nextDouble();
                if (prob < 0.3) {
                    dungeon[i][j] = new EnemyRoom();
                } else if (prob < 0.5) {
                    dungeon[i][j] = new TreasureRoom();
                } else {
                    dungeon[i][j] = new EmptyRoom();
                }
            }
        }
    }

    // Imprime visualmente la matriz con la posición del jugador (1 en la posición actual, 0 en el resto)
    private static void printPlayerPosition(int playerRow, int playerCol) {
        for (int i = 0; i < ROWS; i++) {
            System.out.print("[ ");
            for (int j = 0; j < COLS; j++) {
                if (i == playerRow && j == playerCol) {
                    System.out.print("1 ");
                } else {
                    System.out.print("0 ");
                }
            }
            System.out.println("]");
        }
    }

    // Determina si la posición actual corresponde a un borde (donde puede haber una puerta)
    private static boolean isAtDoor(int row, int col) {
        return (row == 0 || row == ROWS - 1 || col == 0 || col == COLS - 1);
    }
}
