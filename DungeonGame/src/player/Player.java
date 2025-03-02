package player;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class Player {
    private String name;
    private String gender; // "hero" o "heroine"
    private int health;
    private List<String> inventory;
    private String level; // "Trainee", "Junior", "Senior"

    public Player(String name, String gender) {
        this.name = name;
        this.gender = gender;
        this.health = 5; // 5 corazones equivalen al 100% de vida
        this.inventory = new ArrayList<>();
        this.level = "Trainee";
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public int getHealth() {
        return health;
    }

    public List<String> getInventory() {
        return inventory;
    }

    public String getLevel() {
        return level;
    }

    // Devuelve la representación de la vida usando corazones
    public String getHealthDisplay() {
        StringBuilder hearts = new StringBuilder();
        for (int i = 0; i < health; i++) {
            hearts.append("❤️");
        }
        return hearts.toString();
    }

    // Recibe daño (por respuesta incorrecta, por ejemplo)
    public void receiveDamage(int damage) {
        this.health -= damage;
        if(this.health < 0) {
            this.health = 0;
        }
        System.out.println("¡Has recibido daño! Vida actual: " + getHealthDisplay() + " ❌");
    }

    // Recupera vida (por usar tesoros, por ejemplo)
    public void recoverHealth(int amount) {
        this.health += amount;
        if(this.health > 5) {
            this.health = 5;
        }
        System.out.println("¡Has recuperado vida! Vida actual: " + getHealthDisplay() + " ❤️");
    }

    // Agrega un objeto al inventario
    public void addItem(String item) {
        inventory.add(item);
        System.out.println("¡Has obtenido: " + item + "!");
    }

    // Usa un objeto del inventario (por simplicidad, usa el primer tesoro que encuentre)
    public void useItem() {
        if(inventory.isEmpty()) {
            System.out.println("Tu inventario está vacío.");
            return;
        }
        String item = inventory.get(0);
        if(isTreasure(item)) {
            recoverHealth(1);
            inventory.remove(0);
            System.out.println("Has usado " + item + " para recuperar vida.");
        } else {
            System.out.println("No puedes usar ese objeto ahora.");
        }
    }

    private boolean isTreasure(String item) {
        String[] treasures = {"Git", "IntelliJ", "Maven", "JDK", "Spring Boot"};
        return Arrays.asList(treasures).contains(item);
    }

    // Método para mover al jugador según el comando ingresado
    public int[] move(String command, int currentRow, int currentCol, int maxRows, int maxCols) {
        int newRow = currentRow;
        int newCol = currentCol;
        switch(command) {
            case "W":
                if(currentRow > 0) newRow--;
                else System.out.println("No puedes moverte hacia arriba.");
                break;
            case "S":
                if(currentRow < maxRows - 1) newRow++;
                else System.out.println("No puedes moverte hacia abajo.");
                break;
            case "A":
                if(currentCol > 0) newCol--;
                else System.out.println("No puedes moverte hacia la izquierda.");
                break;
            case "D":
                if(currentCol < maxCols - 1) newCol++;
                else System.out.println("No puedes moverte hacia la derecha.");
                break;
            default:
                System.out.println("Comando de movimiento no reconocido.");
                break;
        }
        return new int[]{newRow, newCol};
    }
}
