package rooms;

import player.Player;
import objects.GameObject;
import java.util.Random;

public class TreasureRoom implements Room, GameObject {
    // Lista de tesoros (herramientas que recuperan vida)
    private String[] treasures = {"Git", "IntelliJ", "Maven", "JDK", "Spring Boot"};

    @Override
    public void enter(Player player) {
        // Al "entrar" en la sala, se interactúa con el objeto
        interact(player);
    }

    @Override
    public void interact(Player player) {
        Random rand = new Random();
        String treasure = treasures[rand.nextInt(treasures.length)];
        System.out.println("¡Has encontrado un tesoro! " + treasure + " 🎁");
        player.addItem(treasure);
    }
}
