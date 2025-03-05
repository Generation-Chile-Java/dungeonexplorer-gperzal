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

    // Devuelve la representación de la vida usando corazones según la salud actual
    public String getHealthDisplay() {
        StringBuilder hearts = new StringBuilder();
        for (int i = 0; i < this.health; i++) {
            hearts.append("❤️");
        }
        return hearts.toString();
    }

    // Recibe daño; se actualiza la salud y se muestra la vida actual
    public void receiveDamage(int damage) {
        this.health -= damage;
        if (this.health < 0) {
            this.health = 0;
        }
        System.out.println("¡Has recibido daño! Vida actual: " + getHealthDisplay() + "💔");
    }

    // Agrega un objeto al inventario
    public void addItem(String item) {
        inventory.add(item);
        System.out.println("¡Has obtenido: " + item + "!");
    }

}
