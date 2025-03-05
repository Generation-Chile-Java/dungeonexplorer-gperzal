package rooms;

import player.Player;

public class EmptyRoom implements Room {
    private String name;

    public EmptyRoom(String name) {
        this.name = name;
    }

    @Override
    public void enter(Player player) {
        // En una sala vacía se puede mostrar un mensaje
        System.out.println("Estás en " + name + ". No hay nada especial aquí.");

        // En Sala Principal del nivel Senior, por ejemplo, se podría verificar si se posee la "llave del conocimiento"
        if(name.equals("Sala Principal") && player.getInventory().contains("llave del conocimiento")) {
            System.out.println("Has usado la llave del conocimiento para abrir la puerta del conocimiento y obtener el Tesoro del Conocimiento. ¡FIN DEL JUEGO!");

        }
    }

}
