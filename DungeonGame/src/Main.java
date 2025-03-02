import java.util.HashSet;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;
import java.util.Map;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Main {

    // Estados del juego
    static String currentLevel = "Trainee"; // "Trainee", "Junior", "Senior"
    static String currentRoom = "Sala Principal";
    static int currentRow, currentCol; // posición en la sala (matriz 3x3)

    // Inventario del jugador (se muestra solo al usar V)
    static Set<String> inventory = new HashSet<>();
    static String playerName;

    // Salud del jugador (5 puntos = 5 corazones)
    static int health = 5;

    // Registro de salas cuyo contenido ya fue recogido: clave = level + "-" + room.
    static Set<String> collectedRooms = new HashSet<>();
    // Contenido de cada sala, si tiene (vacío = sin contenido)
    static Map<String, String> roomContent = new HashMap<>();

    // Variable para almacenar la última puerta bloqueada (para usar "X")
    static DoorInfo lastLockedDoor = null;

    // Variable para almacenar los textos del juego cargados desde JSON
    static JsonObject gameTexts;

    // Clase para definir la información de una puerta
    static class DoorInfo {
        String targetRoom;
        int targetRow, targetCol;
        String requiredKey; // Si es null, no requiere llave
        String nextLevel;   // Si no es null, la transición cambia de nivel

        public DoorInfo(String targetRoom, int targetRow, int targetCol, String requiredKey, String nextLevel) {
            this.targetRoom = targetRoom;
            this.targetRow = targetRow;
            this.targetCol = targetCol;
            this.requiredKey = requiredKey;
            this.nextLevel = nextLevel;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in, "UTF-8");

        // Cargar textos del juego desde JSON
        loadGameTexts();

        // Introducción y selección de género y nombre
        System.out.println("¡Bienvenido al Dungeon Game!");
        System.out.println("\n1) Héroe  \n2) Heroína");
        System.out.print("Selecciona tu género: ");
        String genderChoice = scanner.nextLine();
        String gender = genderChoice.equals("1") ? "Héroe" : "Heroína";
        System.out.print("Introduce tu nombre: ");
        playerName = scanner.nextLine();
        // Mostrar intro usando el mensaje del JSON (reemplazando {name})
        String introMessage;
        if(gender.equals("Héroe")) {
            introMessage = gameTexts.getAsJsonObject("intro").get("hero").getAsString();
        } else {
            introMessage = gameTexts.getAsJsonObject("intro").get("heroine").getAsString();
        }
        introMessage = introMessage.replace("{name}", playerName);
        System.out.println(introMessage);

        // Inicializar contenido de salas para cada nivel
        initRoomContent();

        // Establecer la sala inicial: siempre "Sala Principal" en la posición central (1,1)
        currentRoom = "Sala Principal";
        currentRow = 1;
        currentCol = 1;

        // Mostrar comandos (únicamente al inicio)
        System.out.println("🎮 Comandos:");
        System.out.println("  A: Izquierda, W: Arriba, D: Derecha, S: Abajo");
        System.out.println("  F: Acción (recoger item/interactuar con puerta/encontrar enemigo)");
        System.out.println("  V: Ver Inventario, X: Usar objeto (desbloquear puerta)");

        boolean gameOver = false;
        while(!gameOver) {
            // Mostrar estado actual
            System.out.println("\nNivel: " + currentLevel + " | Sala: " + currentRoom);
            printRoom();
            System.out.println("Vida: " + getHealthDisplay());
            // El inventario se muestra solo al presionar "V"
            System.out.print("Ingresa un comando: ");
            String command = scanner.nextLine().toUpperCase();

            switch(command) {
                case "A": case "W": case "S": case "D":
                    gameOver = movePlayer(command);
                    break;
                case "F":
                    performAction();
                    break;
                case "V":
                    System.out.println("Inventario: " + inventory);
                    break;
                case "X":
                    // Intentar desbloquear puerta bloqueada
                    if(lastLockedDoor != null) {
                        if(lastLockedDoor.requiredKey.contains(",")) {
                            String[] keys = lastLockedDoor.requiredKey.split(",");
                            boolean hasAll = true;
                            for(String k : keys) {
                                if(!inventory.contains(k.trim())) {
                                    hasAll = false;
                                    break;
                                }
                            }
                            if(hasAll) {
                                System.out.println("¡Has usado tus llaves (" + lastLockedDoor.requiredKey + ") para desbloquear la puerta!");
                                transitionDoor(lastLockedDoor);
                                lastLockedDoor = null;
                            } else {
                                System.out.println("No tienes todos los ítems necesarios para desbloquear la puerta.");
                            }
                        } else {
                            if(inventory.contains(lastLockedDoor.requiredKey)) {
                                System.out.println("¡Has usado " + lastLockedDoor.requiredKey + " para desbloquear la puerta!");
                                transitionDoor(lastLockedDoor);
                                lastLockedDoor = null;
                            } else {
                                System.out.println("No tienes el item necesario para desbloquear la puerta.");
                            }
                        }
                    } else {
                        System.out.println("No hay ninguna puerta bloqueada a desbloquear.");
                    }
                    break;
                default:
                    System.out.println("Comando no reconocido.");
            }
        }

        scanner.close();
    }

    // Carga el archivo JSON de textos (ubicado en src/resources/game_texts.json)
    private static void loadGameTexts() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("src/resources/game_texts.json")), StandardCharsets.UTF_8);
            Gson gson = new Gson();
            gameTexts = gson.fromJson(content, JsonObject.class);
        } catch (Exception e) {
            System.out.println("Error cargando game_texts.json: " + e.getMessage());
            System.exit(1);
        }
    }

    // Retorna una cadena de corazones según la salud actual
    private static String getHealthDisplay() {
        StringBuilder hearts = new StringBuilder();
        for (int i = 0; i < health; i++) {
            hearts.append("❤️");
        }
        return hearts.toString();
    }

    // Imprime la matriz 3x3 con "1" en la posición actual y "0" en el resto.
    private static void printRoom() {
        for (int i = 0; i < 3; i++) {
            System.out.print("[ ");
            for (int j = 0; j < 3; j++) {
                if(i == currentRow && j == currentCol)
                    System.out.print("1 ");
                else
                    System.out.print("0 ");
            }
            System.out.println("]");
        }
    }

    // Intenta mover al jugador. Si el movimiento es dentro de la sala (índices 0–2), se actualiza.
    // Si se intenta salir de la sala, se consulta si existe una puerta.
    private static boolean movePlayer(String dir) {
        int newRow = currentRow, newCol = currentCol;
        switch(dir) {
            case "W": newRow--; break;
            case "S": newRow++; break;
            case "A": newCol--; break;
            case "D": newCol++; break;
        }
        // Movimiento dentro de la sala (índices 0 a 2)
        if(newRow >= 0 && newRow < 3 && newCol >= 0 && newCol < 3) {
            currentRow = newRow;
            currentCol = newCol;
            return false;
        } else {
            // Movimiento fuera de la sala: se consulta la puerta
            DoorInfo door = getDoorInfo(currentLevel, currentRoom, dir, currentRow, currentCol);
            if(door == null) {
                System.out.println("No puedes moverte en esa dirección.");
                return false;
            } else {
                // Si la puerta requiere llave...
                if(door.requiredKey != null) {
                    if(door.requiredKey.contains(",")) {
                        // Verificar cada llave requerida
                        String[] keys = door.requiredKey.split(",");
                        boolean hasAll = true;
                        for(String k : keys) {
                            if(!inventory.contains(k.trim())) {
                                hasAll = false;
                                System.out.println("La puerta final está bloqueada. Necesitas " + door.requiredKey + " para abrirla.");
                                break;
                            }
                        }
                        if(!hasAll) return false;
                        lastLockedDoor = door;
                        System.out.println("La puerta final está bloqueada. Usa 'X' para usar tus llaves (" + door.requiredKey + ") y desbloquearla.");
                        return false;
                    } else {
                        if(!inventory.contains(door.requiredKey)) {
                            System.out.println("La puerta está bloqueada. Necesitas " + door.requiredKey + " para abrirla.");
                            return false;
                        }
                        lastLockedDoor = door;
                        System.out.println("La puerta está bloqueada. Usa 'X' para usar tu llave " + door.requiredKey + " y desbloquearla.");
                        return false;
                    }
                }
                // Si la puerta no requiere llave, se realiza la transición automáticamente.
                transitionDoor(door);
                return false;
            }
        }
    }

    // Realiza la transición de puerta: si hay cambio de nivel o solo de sala.
    private static void transitionDoor(DoorInfo door) {
        if(door.targetRoom.equals("Salida Final")) {
            // Mostrar mensaje final personalizado (reemplazando {name})
            String finalMessage = gameTexts.getAsJsonObject("dialogues")
                    .getAsJsonObject("final")
                    .get("message").getAsString();
            finalMessage = finalMessage.replace("{name}", playerName);
            System.out.println(finalMessage);
            System.exit(0);
        }
        if(door.nextLevel != null) {
            System.out.println("¡Has transitado al siguiente nivel: " + door.nextLevel + "!");
            currentLevel = door.nextLevel;
            currentRoom = "Sala Principal";
            currentRow = 1;
            currentCol = 1;
        } else {
            currentRoom = door.targetRoom;
            currentRow = door.targetRow;
            currentCol = door.targetCol;
        }
    }

    // Ejecuta la acción "F": recoge contenido o interactúa en la sala actual.
    private static void performAction() {
        String key = currentLevel + "-" + currentRoom;
        // En la Sala Principal del nivel Senior, si se tiene "llave del conocimiento", se finaliza el juego.
        if(currentLevel.equals("Senior") && currentRoom.equals("Sala Principal") && currentRow == 1 && currentCol == 1) {
            if(inventory.contains("llave del conocimiento")) {
                String finalMessage = gameTexts.getAsJsonObject("dialogues")
                        .getAsJsonObject("final")
                        .get("message").getAsString();
                finalMessage = finalMessage.replace("{name}", playerName);
                System.out.println(finalMessage);
                System.exit(0);
            } else {
                System.out.println("La puerta del conocimiento está bloqueada. Necesitas la 'llave del conocimiento'.");
            }
            return;
        }
        // Si hay contenido asignado a la sala, se recoge (si no fue recogido previamente)
        if(roomContent.containsKey(key)) {
            if(!collectedRooms.contains(key)) {
                String content = roomContent.get(key);
                if(content != null && !content.isEmpty()) {
                    System.out.println("¡Has encontrado " + content + "!");
                    inventory.add(content);
                    collectedRooms.add(key);
                } else {
                    double chance = Math.random();
                    if(chance < 0.3) {
                        enemyEncounter();
                    } else {
                        System.out.println("Esta sala no tiene contenido especial.");
                    }
                }
            } else {
                double chance = Math.random();
                if(chance < 0.3) {
                    enemyEncounter();
                } else {
                    System.out.println("Esta sala ya fue explorada.");
                }
            }
        } else {
            System.out.println("No hay nada especial aquí.");
        }
    }

    // Simula un encuentro con un enemigo (Bug 👾) utilizando el pool de preguntas del JSON según el nivel.
    private static void enemyEncounter() {
        System.out.println("¡Te has encontrado con un enemigo: Bug 👾!");
        boolean correct = questions.QuestionPool.askQuestionForLevel(currentLevel);
        if (!correct) {
            health--;
            System.out.println("Respuesta incorrecta. El Bug 👾 te ha atacado. ¡Has perdido 1 punto de vida!");
            if(health <= 0) {
                System.out.println("¡Has perdido toda tu vida! GAME OVER.");
                System.exit(0);
            }
        } else {
            System.out.println("¡Respuesta correcta! Has vencido al Bug 👾! ✅");
        }
    }

    // Define la información de las puertas según el nivel, sala, dirección y posición actual.
    private static DoorInfo getDoorInfo(String level, String room, String direction, int row, int col) {
        // --- MAPA TRAINEE ---
        if(level.equals("Trainee")) {
            if(room.equals("Sala Principal") && direction.equals("D") && row == 1 && col == 2) {
                return new DoorInfo("Habitacion1", 1, 0, null, null);
            }
            if(room.equals("Habitacion1") && direction.equals("A") && row == 1 && col == 0) {
                return new DoorInfo("Sala Principal", 1, 2, null, null);
            }
            if(room.equals("Habitacion1") && direction.equals("W") && row == 0 && col == 1) {
                return new DoorInfo("Habitacion Secreta", 2, 1, "llave secreta", null);
            }
            if(room.equals("Habitacion Secreta") && direction.equals("S") && row == 2 && col == 1) {
                return new DoorInfo("Habitacion1", 0, 1, null, null);
            }
            if(room.equals("Habitacion Secreta") && direction.equals("W") && row == 0 && col == 1) {
                return new DoorInfo("Sala Principal", 1, 1, null, "Junior");
            }
        }
        // --- MAPA JUNIOR ---
        if(level.equals("Junior")) {
            if(room.equals("Sala Principal") && direction.equals("D") && row == 1 && col == 2) {
                return new DoorInfo("Habitacion1", 1, 0, null, null);
            }
            if(room.equals("Habitacion1") && direction.equals("A") && row == 1 && col == 0) {
                return new DoorInfo("Sala Principal", 1, 2, null, null);
            }
            if(room.equals("Habitacion1") && direction.equals("D") && row == 1 && col == 2) {
                return new DoorInfo("Habitacion2", 1, 0, null, null);
            }
            if(room.equals("Habitacion2") && direction.equals("A") && row == 1 && col == 0) {
                return new DoorInfo("Habitacion1", 1, 2, null, null);
            }
            if(room.equals("Sala Principal") && direction.equals("W") && row == 0 && col == 1) {
                return new DoorInfo("Habitacion Secreta", 2, 1, "Tesoro verdadero", null);
            }
            if(room.equals("Habitacion Secreta") && direction.equals("S") && row == 2 && col == 1) {
                return new DoorInfo("Sala Principal", 0, 1, null, null);
            }
            if(room.equals("Habitacion Secreta") && direction.equals("W") && row == 0 && col == 1) {
                return new DoorInfo("Sala Principal", 1, 1, null, "Senior");
            }
        }
        // --- MAPA SENIOR ---
        if(level.equals("Senior")) {
            if(room.equals("Campos Elíseos") && direction.equals("W") && row == 0 && col == 1) {
                return new DoorInfo("Salida Final", 2, 1, "tesoro del conocimiento,llave del conocimiento", null);
            }
            if(room.equals("Sala Principal") && direction.equals("A") && row == 1 && col == 0) {
                return new DoorInfo("Sala Exterior", 1, 2, null, null);
            }
            if(room.equals("Sala Exterior") && direction.equals("D") && row == 1 && col == 2) {
                return new DoorInfo("Sala Principal", 1, 0, null, null);
            }
            if(room.equals("Sala Exterior") && direction.equals("A") && row == 1 && col == 0) {
                return new DoorInfo("Campos Elíseos", 1, 2, null, null);
            }
            if(room.equals("Campos Elíseos") && direction.equals("D") && row == 1 && col == 2) {
                return new DoorInfo("Sala Exterior", 1, 0, null, null);
            }
            if(room.equals("Sala Principal") && direction.equals("D") && row == 1 && col == 2) {
                return new DoorInfo("Habitacion1", 1, 0, "llave secreta de snippet", null);
            }
            if(room.equals("Habitacion1") && direction.equals("A") && row == 1 && col == 0) {
                return new DoorInfo("Sala Principal", 1, 2, null, null);
            }
            if(room.equals("Habitacion1") && direction.equals("D") && row == 1 && col == 2) {
                return new DoorInfo("Habitacion Secreta", 1, 0, null, null);
            }
            if(room.equals("Habitacion Secreta") && direction.equals("A") && row == 1 && col == 0) {
                return new DoorInfo("Habitacion1", 1, 2, null, null);
            }
            if(room.equals("Habitacion Secreta") && direction.equals("W") && row == 0 && col == 1) {
                return new DoorInfo("Habitacion2", 2, 1, null, null);
            }
            if(room.equals("Habitacion2") && direction.equals("S") && row == 2 && col == 1) {
                return new DoorInfo("Habitacion Secreta", 0, 1, null, null);
            }
            if(room.equals("Habitacion Secreta") && direction.equals("S") && row == 2 && col == 1) {
                return new DoorInfo("Habitacion3", 0, 1, null, null);
            }
            // Bidireccional: Desde Habitacion3: si estás en (0,1) y presionas W, regresa a Habitacion Secreta.
            if(room.equals("Habitacion3") && direction.equals("W") && row == 0 && col == 1) {
                return new DoorInfo("Habitacion Secreta", 2, 1, null, null);
            }
        }

        return null;
    }

    // Inicializa el contenido de cada sala (clave: level + "-" + room)
    private static void initRoomContent() {
        // MAPA TRAINEE
        roomContent.put("Trainee-Sala Principal", "");
        roomContent.put("Trainee-Habitacion1", "llave secreta");
        roomContent.put("Trainee-Habitacion Secreta", "tesoro del conocimiento");
        // MAPA JUNIOR
        roomContent.put("Junior-Sala Principal", "");
        roomContent.put("Junior-Habitacion1", "Tesoro Falso");
        roomContent.put("Junior-Habitacion2", "Tesoro verdadero");
        roomContent.put("Junior-Habitacion Secreta", "llave secreta");
        // MAPA SENIOR
        roomContent.put("Senior-Sala Principal", "");
        roomContent.put("Senior-Sala Exterior", "");
        roomContent.put("Senior-Campos Elíseos", "llave secreta de snippet");
        roomContent.put("Senior-Habitacion1", "");
        roomContent.put("Senior-Habitacion Secreta", "llave");
        roomContent.put("Senior-Habitacion2", "");
        roomContent.put("Senior-Habitacion3", "llave del conocimiento");
    }
}
