package rooms;

import player.Player;

public class LockedRoom implements Room {
    private String roomName;
    private String requiredKey;

    public LockedRoom(String roomName, String requiredKey) {
        this.roomName = roomName;
        this.requiredKey = requiredKey;
    }

    @Override
    public void enter(Player player) {
        if (player.getInventory().contains(requiredKey)) {
            System.out.println("Has desbloqueado " + roomName + " usando " + requiredKey + ". ¡Puedes continuar!");
        } else {
            System.out.println(roomName + " está bloqueada. Necesitas " + requiredKey + " para entrar.");
        }
    }

}
