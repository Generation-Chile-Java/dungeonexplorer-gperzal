package levels;

import rooms.Room;
import rooms.EmptyRoom;
import rooms.KeyRoom;
import rooms.TransitionRoom;

public class JuniorLevel implements Level {
    private Room[][] map;

    public JuniorLevel() {
        // Creamos un mapa de 4 filas x 4 columnas para el nivel Junior.
        map = new Room[4][4];
        // Sala Principal en (3,1)
        map[3][1] = new EmptyRoom("Sala Principal");
        // Habitacion1 a la derecha de Sala Principal: (3,2) con "Tesoro Falso"
        map[3][2] = new KeyRoom("Tesoro Falso");
        // Habitacion2 a la derecha de Habitacion1: (3,3) con "Tesoro verdadero"
        map[3][3] = new KeyRoom("Tesoro verdadero");
        // Habitacion Secreta: desde Sala Principal hacia arriba: (2,1) con "tesoro del conocimiento"
        map[2][1] = new KeyRoom("tesoro del conocimiento");
        // Desde Habitacion Secreta, hacia arriba: (1,1) con transición al siguiente nivel.
        map[1][1] = new TransitionRoom("Desarrollador Senior");
    }

    @Override
    public Room[][] getMap() {
        return map;
    }

    @Override
    public int getInitialRow() {
        return 3;
    }

    @Override
    public int getInitialCol() {
        return 1;
    }

    @Override
    public String getLevelName() {
        return "Junior";
    }
}
