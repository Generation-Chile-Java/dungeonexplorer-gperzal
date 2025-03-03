package levels;

import rooms.Room;
import rooms.EmptyRoom;
import rooms.KeyRoom;
import rooms.TransitionRoom;

public class TraineeLevel implements Level {
    private Room[][] map;

    public TraineeLevel() {
        // Creamos un mapa de 4 filas x 3 columnas para el nivel Trainee.
        map = new Room[4][3];
        // Asignamos las salas según el diseño:
        // Sala Principal en (3,1)
        map[3][1] = new EmptyRoom("Sala Principal");
        // Habitacion1 a la derecha: (3,2) que contiene "Secreta la llave"
        map[3][2] = new KeyRoom("Secreta la llave");
        // Habitacion Secreta arriba de Habitacion1: (2,2) que contiene "tesoro del conocimiento"
        map[2][2] = new KeyRoom("tesoro del conocimiento");
        // Desde Habitacion Secreta, hacia arriba (1,2) se coloca la puerta de transición al siguiente nivel.
        map[1][2] = new TransitionRoom("Desarrollador Junior");
        // El resto de celdas se dejan como null (no accesibles).
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
        return "Trainee";
    }
}
