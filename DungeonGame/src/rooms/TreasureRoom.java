package rooms;

import player.Player;
import java.util.Random;

public class TreasureRoom implements Room {
    // Lista de tesoros (herramientas que recuperan vida)
    private String[] treasures = {"Git", "IntelliJ", "Maven", "JDK", "Spring Boot"};

    @Override
    public void enter(Player player) {
        Random rand = new Random();
        String treasure = treasures[rand.nextInt(treasures.length)];
        System.out.println("¡Has encontrado un tesoro! " + treasure + " 🎁");
        player.addItem(treasure);
    }
}
