package rooms;

import player.Player;

public class EmptyRoom implements Room {
    @Override
    public void enter(Player player) {
        System.out.println("Sala vacía. No hay nada que hacer aquí.");
    }
}
