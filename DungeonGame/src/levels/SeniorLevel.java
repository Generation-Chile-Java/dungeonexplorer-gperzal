package levels;

import rooms.Room;
import rooms.EmptyRoom;
import rooms.KeyRoom;
import rooms.LockedRoom;

public class SeniorLevel implements Level {
    private Room[][] map;

    public SeniorLevel() {
        // Creamos un mapa de 5 filas x 5 columnas para el nivel Senior.
        map = new Room[5][5];
        // Sala Principal en (2,2) – puerta del conocimiento bloqueada inicialmente.
        map[2][2] = new EmptyRoom("Sala Principal");
        // Sala Exterior a la izquierda de Sala Principal: (2,1)
        map[2][1] = new EmptyRoom("Sala Exterior");
        // Campos Elíseos a la izquierda de Sala Exterior: (2,0) con "llave secreta de snippet"
        map[2][0] = new KeyRoom("llave secreta de snippet");
        // Habitacion1 a la derecha de Sala Principal: (2,3) bloqueada, requiere "llave secreta de snippet"
        map[2][3] = new LockedRoom("Habitacion1", "llave secreta de snippet");
        // Habitacion Secreta a la derecha de Habitacion1: (2,4) que contiene una llave (por ejemplo, "llave")
        map[2][4] = new KeyRoom("llave");
        // Habitacion2, desde Habitacion Secreta hacia arriba: (1,4) vacía
        map[1][4] = new EmptyRoom("Habitacion2");
        // Habitacion3, desde Habitacion Secreta hacia abajo: (3,4) con "llave del conocimiento"
        map[3][4] = new KeyRoom("llave del conocimiento");
        // Finalmente, en Sala Principal se usará la "llave del conocimiento" para obtener el "Tesoro del Conocimiento"
        // Esta acción se puede implementar en el método enter() de Sala Principal o gestionarse externamente.
    }

    @Override
    public Room[][] getMap() {
        return map;
    }

    @Override
    public int getInitialRow() {
        return 2;
    }

    @Override
    public int getInitialCol() {
        return 2;
    }

    @Override
    public String getLevelName() {
        return "Senior";
    }
}
