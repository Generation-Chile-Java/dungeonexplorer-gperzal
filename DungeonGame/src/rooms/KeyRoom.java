package rooms;

import player.Player;

public class KeyRoom implements Room {
    private String keyName;

    public KeyRoom(String keyName) {
        this.keyName = keyName;
    }

    @Override
    public void enter(Player player) {
        if (!player.getInventory().contains(keyName)) {
            System.out.println("¡Has encontrado " + keyName + "!");
            player.addItem(keyName);
        } else {
            System.out.println("Esta sala ya fue explorada.");
        }
    }
}
