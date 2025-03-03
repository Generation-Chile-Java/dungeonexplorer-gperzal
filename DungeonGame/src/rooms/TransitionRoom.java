package rooms;

import player.Player;

public class TransitionRoom implements Room {
    private String nextLevel;

    public TransitionRoom(String nextLevel) {
        this.nextLevel = nextLevel;
    }

    @Override
    public void enter(Player player) {
        System.out.println("¡Has activado el portal a " + nextLevel + "!");
        // Aquí se podría establecer un flag para la transición. La lógica de cambio de nivel se gestionará en Main.java.
    }

    public String getNextLevel() {
        return nextLevel;
    }
}
