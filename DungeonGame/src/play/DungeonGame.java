package play;

import java.util.Scanner;
import java.util.HashSet;
import java.util.Set;
import com.google.gson.JsonObject;

public class DungeonGame {
    // Estados del juego
    private String currentLevel = "Trainee"; // "Trainee", "Junior", "Senior"
    private String currentRoom = "Sala Principal";
    private int currentRow, currentCol; // posición en la sala (matriz 3x3)

    // Inventario del jugador (se muestra solo al usar V)
    private Set<String> inventory = new HashSet<>();
    private String playerName;

    // Salud del jugador (5 puntos = 5 corazones)
    private int health = 5;

    // Registro de salas cuyo contenido ya fue recogido
    private Set<String> collectedRooms = new HashSet<>();

    // Variable para almacenar la última puerta bloqueada
    private DoorInfo lastLockedDoor = null;

    // Gestores de juego
    private RoomManager roomManager;
    private GameTextManager textManager;

    public DungeonGame() {
        // Inicializar los managers
        roomManager = new RoomManager();
        textManager = new GameTextManager();
    }

    public void initialize(Scanner scanner) {
        // Cargar textos del juego
        textManager.loadGameTexts();

        // Inicializar contenido de salas
        roomManager.initRoomContent();

        // Configuración inicial
        setupInitialState();

        // Configuración del jugador
        setupPlayer(scanner);
    }

    private void setupInitialState() {
        // Establecer la sala inicial en posición central (1,1)
        currentRoom = "Sala Principal";
        currentRow = 1;
        currentCol = 1;
        health = 5;
    }

    private void setupPlayer(Scanner scanner) {
        // Introducción y selección de género y nombre
        System.out.println("¡Bienvenido al Dungeon Game!");
        System.out.println("\n1) Héroe  \n2) Heroína");
        System.out.print("Selecciona tu género: ");
        String genderChoice = scanner.nextLine();
        String gender = genderChoice.equals("1") ? "Héroe" : "Heroína";

        System.out.print("Introduce tu nombre: ");
        playerName = scanner.nextLine();

        // Mostrar intro según género elegido
        String introMessage;
        JsonObject gameTexts = textManager.getGameTexts();
        if(gender.equals("Héroe")) {
            introMessage = gameTexts.getAsJsonObject("intro").get("hero").getAsString();
        } else {
            introMessage = gameTexts.getAsJsonObject("intro").get("heroine").getAsString();
        }
        introMessage = introMessage.replace("{name}", playerName);
        System.out.println(introMessage);

        // Mostrar comandos disponibles
        printCommands();
    }

    private void printCommands() {
        System.out.println("🎮 Comandos:");
        System.out.println("  A: Izquierda, W: Arriba, D: Derecha, S: Abajo");
        System.out.println("  F: Acción (recoger item/interactuar con puerta/encontrar enemigo)");
        System.out.println("  V: Ver Inventario, X: Usar objeto (desbloquear puerta)");
    }

    public void run() {
        Scanner scanner = new Scanner(System.in, "UTF-8");
        boolean gameOver = false;

        while(!gameOver) {
            // Mostrar estado actual
            displayGameState();

            // Solicitar comando
            System.out.print("Ingresa un comando: ");
            String command = scanner.nextLine().toUpperCase();

            // Procesar comando
            gameOver = processCommand(command);
        }
    }

    private void displayGameState() {
        System.out.println("\nNivel: " + currentLevel + " | Sala: " + currentRoom);
        printRoom();
        System.out.println("Vida: " + getHealthDisplay());
    }

    private String getHealthDisplay() {
        StringBuilder hearts = new StringBuilder();
        for (int i = 0; i < health; i++) {
            hearts.append("❤️");
        }
        return hearts.toString();
    }

    private void printRoom() {
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

    private boolean processCommand(String command) {
        switch(command) {
            case "A": case "W": case "S": case "D":
                return movePlayer(command);
            case "F":
                performAction();
                return false;
            case "V":
                System.out.println("Inventario: " + inventory);
                return false;
            case "X":
                return unlockDoor();
            default:
                System.out.println("Comando no reconocido.");
                return false;
        }
    }

    private boolean movePlayer(String dir) {
        int newRow = currentRow, newCol = currentCol;

        // Calcular nueva posición
        switch(dir) {
            case "W": newRow--; break;
            case "S": newRow++; break;
            case "A": newCol--; break;
            case "D": newCol++; break;
        }

        // Verificar si es un movimiento dentro de la sala
        if(newRow >= 0 && newRow < 3 && newCol >= 0 && newCol < 3) {
            currentRow = newRow;
            currentCol = newCol;
            return false;
        } else {
            // Movimiento fuera de la sala: consultar puerta
            return tryDoorTransition(dir);
        }
    }

    private boolean tryDoorTransition(String direction) {
        DoorInfo door = roomManager.getDoorInfo(currentLevel, currentRoom, direction, currentRow, currentCol);

        if(door == null) {
            System.out.println("No puedes moverte en esa dirección.");
            return false;
        }

        // Verificar si la puerta necesita llave
        if(door.requiredKey != null) {
            return handleLockedDoor(door);
        } else {
            // Puerta sin llave, transición automática
            transitionDoor(door);
            return false;
        }
    }

    private boolean handleLockedDoor(DoorInfo door) {
        if(door.requiredKey.contains(",")) {
            // Múltiples llaves requeridas
            String[] keys = door.requiredKey.split(",");
            boolean hasAll = true;

            for(String k : keys) {
                if(!inventory.contains(k.trim())) {
                    hasAll = false;
                    break;
                }
            }

            if(!hasAll) {
                System.out.println("La puerta final está bloqueada. Necesitas " + door.requiredKey + " para abrirla.");
                return false;
            }

            lastLockedDoor = door;
            System.out.println("La puerta final está bloqueada. Usa 'X' para usar tus llaves (" + door.requiredKey + ") y desbloquearla.");
            return false;
        } else {
            // Una sola llave requerida
            if(!inventory.contains(door.requiredKey)) {
                System.out.println("La puerta está bloqueada. Necesitas " + door.requiredKey + " para abrirla.");
                return false;
            }

            lastLockedDoor = door;
            System.out.println("La puerta está bloqueada. Usa 'X' para usar tu llave " + door.requiredKey + " y desbloquearla.");
            return false;
        }
    }

    private boolean unlockDoor() {
        if(lastLockedDoor == null) {
            System.out.println("No hay ninguna puerta bloqueada a desbloquear.");
            return false;
        }

        if(lastLockedDoor.requiredKey.contains(",")) {
            // Múltiples llaves requeridas
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
            // Una sola llave requerida
            if(inventory.contains(lastLockedDoor.requiredKey)) {
                System.out.println("¡Has usado " + lastLockedDoor.requiredKey + " para desbloquear la puerta!");
                transitionDoor(lastLockedDoor);
                lastLockedDoor = null;
            } else {
                System.out.println("No tienes el item necesario para desbloquear la puerta.");
            }
        }

        return false;
    }

    private void transitionDoor(DoorInfo door) {
        if(door.targetRoom.equals("Salida Final")) {
            // Mostrar mensaje final personalizado
            showFinalMessage();
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

    private void showFinalMessage() {
        String finalMessage = textManager.getGameTexts()
                .getAsJsonObject("dialogues")
                .getAsJsonObject("final")
                .get("message").getAsString();
        finalMessage = finalMessage.replace("{name}", playerName);
        System.out.println(finalMessage);
    }

    private void performAction() {
        // Caso especial para el nivel Senior en la Sala Principal
        if(currentLevel.equals("Senior") && currentRoom.equals("Sala Principal") && currentRow == 1 && currentCol == 1) {
            if(inventory.contains("llave del conocimiento")) {
                showFinalMessage();
                System.exit(0);
            } else {
                System.out.println("La puerta del conocimiento está bloqueada. Necesitas la 'llave del conocimiento'.");
            }
            return;
        }

        // Acción normal en las salas
        String key = currentLevel + "-" + currentRoom;
        if(roomManager.hasContent(key)) {
            if(!collectedRooms.contains(key)) {
                collectRoomContent(key);
            } else {
                randomEncounter();
            }
        } else {
            System.out.println("No hay nada especial aquí.");
        }
    }

    private void collectRoomContent(String key) {
        String content = roomManager.getRoomContent(key);

        if(content != null && !content.isEmpty()) {
            System.out.println("¡Has encontrado " + content + "!");
            inventory.add(content);
            collectedRooms.add(key);
        } else {
            randomEncounter();
        }
    }

    private void randomEncounter() {
        double chance = Math.random();
        if(chance < 0.3) {
            enemyEncounter();
        } else {
            System.out.println("Esta sala ya fue explorada.");
        }
    }

    private void enemyEncounter() {
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
}