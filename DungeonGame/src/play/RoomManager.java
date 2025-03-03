package play;

import java.util.HashMap;
import java.util.Map;

public class RoomManager {
    // Contenido de cada sala, si tiene (vacío = sin contenido)
    private Map<String, String> roomContent = new HashMap<>();

    // Inicializa el contenido de cada sala (clave: level + "-" + room)
    public void initRoomContent() {
        // MAPA TRAINEE
        roomContent.put("Trainee-Sala Principal", "");
        roomContent.put("Trainee-Habitacion1", "llave secreta");
        roomContent.put("Trainee-Habitacion Secreta", "tesoro del conocimiento");

        // MAPA JUNIOR
        roomContent.put("Junior-Sala Principal", "");
        roomContent.put("Junior-Habitacion1", "Tesoro Falso");
        roomContent.put("Junior-Habitacion2", "Tesoro verdadero");
        roomContent.put("Junior-Habitacion Secreta", "llave secreta");

        // MAPA SENIOR
        roomContent.put("Senior-Sala Principal", "");
        roomContent.put("Senior-Sala Exterior", "");
        roomContent.put("Senior-Campos Elíseos", "llave secreta de snippet");
        roomContent.put("Senior-Habitacion1", "");
        roomContent.put("Senior-Habitacion Secreta", "llave");
        roomContent.put("Senior-Habitacion2", "");
        roomContent.put("Senior-Habitacion3", "llave del conocimiento");
    }

    // Verificar si existe contenido para una sala
    public boolean hasContent(String key) {
        return roomContent.containsKey(key);
    }

    // Obtener el contenido de una sala
    public String getRoomContent(String key) {
        return roomContent.get(key);
    }

    // Define la información de las puertas según el nivel, sala, dirección y posición actual.
    public DoorInfo getDoorInfo(String level, String room, String direction, int row, int col) {
        // --- MAPA TRAINEE ---
        if(level.equals("Trainee")) {
            if(room.equals("Sala Principal") && direction.equals("D") && row == 1 && col == 2) {
                return new DoorInfo("Habitacion1", 1, 0, null, null);
            }
            if(room.equals("Habitacion1") && direction.equals("A") && row == 1 && col == 0) {
                return new DoorInfo("Sala Principal", 1, 2, null, null);
            }
            if(room.equals("Habitacion1") && direction.equals("W") && row == 0 && col == 1) {
                return new DoorInfo("Habitacion Secreta", 2, 1, "llave secreta", null);
            }
            if(room.equals("Habitacion Secreta") && direction.equals("S") && row == 2 && col == 1) {
                return new DoorInfo("Habitacion1", 0, 1, null, null);
            }
            if(room.equals("Habitacion Secreta") && direction.equals("W") && row == 0 && col == 1) {
                return new DoorInfo("Sala Principal", 1, 1, null, "Junior");
            }
        }

        // --- MAPA JUNIOR ---
        if(level.equals("Junior")) {
            if(room.equals("Sala Principal") && direction.equals("D") && row == 1 && col == 2) {
                return new DoorInfo("Habitacion1", 1, 0, null, null);
            }
            if(room.equals("Habitacion1") && direction.equals("A") && row == 1 && col == 0) {
                return new DoorInfo("Sala Principal", 1, 2, null, null);
            }
            if(room.equals("Habitacion1") && direction.equals("D") && row == 1 && col == 2) {
                return new DoorInfo("Habitacion2", 1, 0, null, null);
            }
            if(room.equals("Habitacion2") && direction.equals("A") && row == 1 && col == 0) {
                return new DoorInfo("Habitacion1", 1, 2, null, null);
            }
            if(room.equals("Sala Principal") && direction.equals("W") && row == 0 && col == 1) {
                return new DoorInfo("Habitacion Secreta", 2, 1, "Tesoro verdadero", null);
            }
            if(room.equals("Habitacion Secreta") && direction.equals("S") && row == 2 && col == 1) {
                return new DoorInfo("Sala Principal", 0, 1, null, null);
            }
            if(room.equals("Habitacion Secreta") && direction.equals("W") && row == 0 && col == 1) {
                return new DoorInfo("Sala Principal", 1, 1, null, "Senior");
            }
        }

        // --- MAPA SENIOR ---
        if(level.equals("Senior")) {
            if(room.equals("Campos Elíseos") && direction.equals("W") && row == 0 && col == 1) {
                return new DoorInfo("Salida Final", 2, 1, "tesoro del conocimiento,llave del conocimiento", null);
            }
            if(room.equals("Sala Principal") && direction.equals("A") && row == 1 && col == 0) {
                return new DoorInfo("Sala Exterior", 1, 2, null, null);
            }
            if(room.equals("Sala Exterior") && direction.equals("D") && row == 1 && col == 2) {
                return new DoorInfo("Sala Principal", 1, 0, null, null);
            }
            if(room.equals("Sala Exterior") && direction.equals("A") && row == 1 && col == 0) {
                return new DoorInfo("Campos Elíseos", 1, 2, null, null);
            }
            if(room.equals("Campos Elíseos") && direction.equals("D") && row == 1 && col == 2) {
                return new DoorInfo("Sala Exterior", 1, 0, null, null);
            }
            if(room.equals("Sala Principal") && direction.equals("D") && row == 1 && col == 2) {
                return new DoorInfo("Habitacion1", 1, 0, "llave secreta de snippet", null);
            }
            if(room.equals("Habitacion1") && direction.equals("A") && row == 1 && col == 0) {
                return new DoorInfo("Sala Principal", 1, 2, null, null);
            }
            if(room.equals("Habitacion1") && direction.equals("D") && row == 1 && col == 2) {
                return new DoorInfo("Habitacion Secreta", 1, 0, null, null);
            }
            if(room.equals("Habitacion Secreta") && direction.equals("A") && row == 1 && col == 0) {
                return new DoorInfo("Habitacion1", 1, 2, null, null);
            }
            if(room.equals("Habitacion Secreta") && direction.equals("W") && row == 0 && col == 1) {
                return new DoorInfo("Habitacion2", 2, 1, null, null);
            }
            if(room.equals("Habitacion2") && direction.equals("S") && row == 2 && col == 1) {
                return new DoorInfo("Habitacion Secreta", 0, 1, null, null);
            }
            if(room.equals("Habitacion Secreta") && direction.equals("S") && row == 2 && col == 1) {
                return new DoorInfo("Habitacion3", 0, 1, null, null);
            }
            if(room.equals("Habitacion3") && direction.equals("W") && row == 0 && col == 1) {
                return new DoorInfo("Habitacion Secreta", 2, 1, null, null);
            }
        }

        return null;
    }
}