package play;

import java.util.Scanner;
import java.util.HashSet;
import java.util.Set;
import com.google.gson.JsonObject;
import rooms.Room;
import player.Player;

public class DungeonGame {
    // Estados del juego
    private String currentLevel = "Trainee"; // "Trainee", "Junior", "Senior"
    private String currentRoom = "Sala Principal";
    private int currentRow, currentCol; // posición en la sala (matriz 3x3)

    // Datos del jugador
    private String playerName;
    private Player player;  // Objeto jugador

    // Salud del jugador (5 puntos = 5 corazones)
    private int health = 5;

    // Registro de salas ya exploradas
    private Set<String> collectedRooms = new HashSet<>();

    // Variable para almacenar la última puerta bloqueada (para transiciones)
    private DoorInfo lastLockedDoor = null;

    // Gestores de juego
    private RoomManager roomManager;
    private GameTextManager textManager;

    public DungeonGame() {
        roomManager = new RoomManager();
        textManager = new GameTextManager();
    }

    public void initialize(Scanner scanner) {
        textManager.loadGameTexts();
        roomManager.initRoomContent();
        setupInitialState();
        setupPlayer(scanner);
    }

    private void setupInitialState() {
        currentRoom = "Sala Principal";
        currentRow = 1;
        currentCol = 1;
        health = 5;
    }

    private void setupPlayer(Scanner scanner) {
        System.out.println("⚔️ ¡Bienvenido al Dungeon Game! 🏰");
        System.out.println("\n1) 🏹 Héroe  \n2) 🎖️ Heroína");
        System.out.print("🔵 Selecciona tu género: ");
        String genderChoice = scanner.nextLine();
        String gender = genderChoice.equals("1") ? "Héroe" : "Heroína";

        System.out.print("💬 Introduce tu nombre: ");
        playerName = scanner.nextLine();

        // Crear el objeto Player
        player = new Player(playerName, gender);

        String introMessage;
        JsonObject gameTexts = textManager.getGameTexts();
        if (gender.equals("Héroe")) {
            introMessage = gameTexts.getAsJsonObject("intro").get("hero").getAsString();
        } else {
            introMessage = gameTexts.getAsJsonObject("intro").get("heroine").getAsString();
        }
        introMessage = introMessage.replace("{name}", playerName);
        System.out.println("\n 📝 " + introMessage);

        printCommands();
    }

    private void printCommands() {
        System.out.println("\n🎮 Comandos:");
        System.out.println("\uD83D\uDD79\uFE0F A: Izquierda, W: Arriba, D: Derecha, S: Abajo");
        System.out.println("\uD83D\uDC49 F: Acción (recoger item/interactuar con puerta/encontrar enemigo)");
        System.out.println("\uD83D\uDCE6 V: Ver Inventario, X: Usar objeto (desbloquear puerta)");
    }

    public void run() {
        Scanner scanner = new Scanner(System.in, "UTF-8");
        boolean gameOver = false;
        while (!gameOver) {
            displayGameState();
            System.out.print("🎮 Ingresa un comando: ");
            String command = scanner.nextLine().toUpperCase();
            gameOver = processCommand(command);
        }
        scanner.close();
    }

    private void displayGameState() {
        System.out.println("\n\uD83D\uDC51 " + player.getGender() + ": " + player.getName() + " | 💻 Nivel: " + currentLevel + " | 🏰 Sala: " + currentRoom);
        printRoom();
        System.out.println("💖 Vida: " + player.getHealthDisplay());
    }


    // Imprime el mapa 3x3 (posición actual del jugador)
    private void printRoom() {
        System.out.println("🗺️ Mapa de Posición de la Zona:");
        for (int i = 0; i < 3; i++) {
            System.out.print("[ ");
            for (int j = 0; j < 3; j++) {
                if (i == currentRow && j == currentCol)
                    System.out.print("👤 ");
                else
                    System.out.print("🌿 ");
            }
            System.out.println("]");
        }
    }

    private boolean processCommand(String command) {
        switch (command) {
            case "A": case "W": case "S": case "D":
                return movePlayer(command);
            case "F":
                performAction();
                return false;
            case "V":
                System.out.println("Inventario: " + player.getInventory());
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
        switch (dir) {
            case "W": newRow--; break;
            case "S": newRow++; break;
            case "A": newCol--; break;
            case "D": newCol++; break;
        }
        if (newRow >= 0 && newRow < 3 && newCol >= 0 && newCol < 3) {
            currentRow = newRow;
            currentCol = newCol;
            return false;
        } else {
            return tryDoorTransition(dir);
        }
    }

    private boolean tryDoorTransition(String direction) {
        DoorInfo door = roomManager.getDoorInfo(currentLevel, currentRoom, direction, currentRow, currentCol);
        if (door == null) {
            System.out.println("No puedes moverte en esa dirección.");
            return false;
        }
        if (door.requiredKey != null) {
            return handleLockedDoor(door);
        } else {
            transitionDoor(door);
            return false;
        }
    }

    private boolean handleLockedDoor(DoorInfo door) {
        if (door.requiredKey.contains(",")) {
            String[] keys = door.requiredKey.split(",");
            boolean hasAll = true;
            for (String k : keys) {
                if (!player.getInventory().contains(k.trim())) {
                    hasAll = false;
                    break;
                }
            }
            if (!hasAll) {
                System.out.println("La puerta final está bloqueada. Necesitas " + door.requiredKey + " para abrirla.");
                return false;
            }
            lastLockedDoor = door;
            System.out.println("La puerta final está bloqueada. Usa 'X' para usar tus llaves (" + door.requiredKey + ") y desbloquearla.");
            return false;
        } else {
            if (!player.getInventory().contains(door.requiredKey)) {
                System.out.println("La puerta está bloqueada. Necesitas " + door.requiredKey + " para abrirla.");
                return false;
            }
            lastLockedDoor = door;
            System.out.println("La puerta está bloqueada. Usa 'X' para usar tu llave " + door.requiredKey + " y desbloquearla.");
            return false;
        }
    }

    private boolean unlockDoor() {
        if (lastLockedDoor == null) {
            System.out.println("No hay ninguna puerta bloqueada a desbloquear.");
            return false;
        }
        if (lastLockedDoor.requiredKey.contains(",")) {
            String[] keys = lastLockedDoor.requiredKey.split(",");
            boolean hasAll = true;
            for (String k : keys) {
                if (!player.getInventory().contains(k.trim())) {
                    hasAll = false;
                    break;
                }
            }
            if (hasAll) {
                System.out.println("¡Has usado tus llaves (" + lastLockedDoor.requiredKey + ") para desbloquear la puerta!");
                transitionDoor(lastLockedDoor);
                lastLockedDoor = null;
            } else {
                System.out.println("No tienes todos los ítems necesarios para desbloquear la puerta.");
            }
        } else {
            if (player.getInventory().contains(lastLockedDoor.requiredKey)) {
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
        if (door.targetRoom.equals("Salida Final")) {
            showFinalMessage();
            System.exit(0);
        }
        if (door.nextLevel != null) {
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
        // Caso especial para el nivel Senior en la Sala Principal (puerta final)
        if (currentLevel.equals("Senior") && currentRoom.equals("Sala Principal") && currentRow == 1 && currentCol == 1) {
            if (player.getInventory().contains("llave del conocimiento")) {
                showFinalMessage();
                System.exit(0);
            } else {
                System.out.println("La puerta del conocimiento está bloqueada. Necesitas la 'llave del conocimiento'.");
            }
            return;
        }

        String key = currentLevel + "-" + currentRoom;
        if (roomManager.hasRoom(key)) {
            java.util.List<Room> events = roomManager.getRooms(key);
            if (events != null && !events.isEmpty()) {
                double roll = Math.random(); // número entre 0 y 1
                Room chosen = null;
                if (roll < 0.05) {
                    // 5% de probabilidad para TreasureRoom
                    for (Room r : events) {
                        if (r instanceof rooms.TreasureRoom) {
                            chosen = r;
                            break;
                        }
                    }
                } else if (roll < 0.65) {
                    // 65% de probabilidad para EnemyRoom
                    for (Room r : events) {
                        if (r instanceof rooms.EnemyRoom) {
                            chosen = r;
                            break;
                        }
                    }
                } else {
                    // 30% de probabilidad para KeyRoom (o similar, para progresar)
                    for (Room r : events) {
                        if (r instanceof rooms.KeyRoom) {
                            chosen = r;
                            break;
                        }
                    }
                }
                // Fallback: si no se encontró un evento del tipo deseado, se selecciona aleatoriamente
                if (chosen == null) {
                    int index = (int) (Math.random() * events.size());
                    chosen = events.get(index);
                }
                chosen.enter(player);
                events.remove(chosen); // Eliminamos el evento para que no se repita
                collectedRooms.add(key);
            } else {
                System.out.println("Esta sala ya fue completamente explorada.");
            }
        } else {
            System.out.println("No hay nada especial aquí.");
        }
    }


}
