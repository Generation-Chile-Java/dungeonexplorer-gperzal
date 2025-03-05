package play;

import rooms.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomManager {
    // Map de salas: clave = level + "-" + room, valor = lista de eventos en esa sala
    private Map<String, List<Room>> roomMap = new HashMap<>();

    public void initRoomContent() {
        // MAPA TRAINEE
        List<Room> salaPrincipalTrainee = new ArrayList<>();
        salaPrincipalTrainee.add(new EmptyRoom("Sala Principal"));
        roomMap.put("Trainee-Sala Principal", salaPrincipalTrainee);

        List<Room> habitacion1Trainee = new ArrayList<>();
        habitacion1Trainee.add(new KeyRoom("llave secreta"));
        habitacion1Trainee.add(new TreasureRoom());  // Tesoro que puede servir de escudo
        habitacion1Trainee.add(new EnemyRoom());
        roomMap.put("Trainee-Habitacion1", habitacion1Trainee);

        List<Room> habitacionSecretaTrainee = new ArrayList<>();
        habitacionSecretaTrainee.add(new KeyRoom("tesoro del conocimiento"));
        roomMap.put("Trainee-Habitacion Secreta", habitacionSecretaTrainee);

        // MAPA JUNIOR
        List<Room> salaPrincipalJunior = new ArrayList<>();
        salaPrincipalJunior.add(new EmptyRoom("Sala Principal"));
        roomMap.put("Junior-Sala Principal", salaPrincipalJunior);

        List<Room> habitacion1Junior = new ArrayList<>();
        habitacion1Junior.add(new KeyRoom("Tesoro Falso"));
        habitacion1Junior.add(new EnemyRoom());
        roomMap.put("Junior-Habitacion1", habitacion1Junior);

        List<Room> habitacion2Junior = new ArrayList<>();
        habitacion2Junior.add(new KeyRoom("Tesoro verdadero"));
        habitacion2Junior.add(new TreasureRoom());
        roomMap.put("Junior-Habitacion2", habitacion2Junior);

        List<Room> habitacionSecretaJunior = new ArrayList<>();
        habitacionSecretaJunior.add(new KeyRoom("llave secreta"));
        roomMap.put("Junior-Habitacion Secreta", habitacionSecretaJunior);

       /* // MAPA SENIOR
        List<Room> salaPrincipalSenior = new ArrayList<>();
        salaPrincipalSenior.add(new EmptyRoom("Sala Principal"));
        roomMap.put("Senior-Sala Principal", salaPrincipalSenior);

        List<Room> salaExteriorSenior = new ArrayList<>();
        salaExteriorSenior.add(new EmptyRoom("Sala Exterior"));
        roomMap.put("Senior-Sala Exterior", salaExteriorSenior);

        List<Room> camposElyseosSenior = new ArrayList<>();
        camposElyseosSenior.add(new KeyRoom("llave secreta de snippet"));
        roomMap.put("Senior-Campos Elíseos", camposElyseosSenior);

        List<Room> habitacion1Senior = new ArrayList<>();
        habitacion1Senior.add(new EmptyRoom("Habitacion1"));
        roomMap.put("Senior-Habitacion1", habitacion1Senior);

        List<Room> habitacionSecretaSenior = new ArrayList<>();
        habitacionSecretaSenior.add(new KeyRoom("llave"));
        roomMap.put("Senior-Habitacion Secreta", habitacionSecretaSenior);

        List<Room> habitacion2Senior = new ArrayList<>();
        habitacion2Senior.add(new TreasureRoom()); // Tesoro que puede servir de escudo o recuperar vida
        roomMap.put("Senior-Habitacion2", habitacion2Senior);

        List<Room> habitacion3Senior = new ArrayList<>();
        habitacion3Senior.add(new KeyRoom("llave del conocimiento"));
        roomMap.put("Senior-Habitacion3", habitacion3Senior);*/
        // MAPA SENIOR
        List<Room> salaPrincipalSenior = new ArrayList<>();
        salaPrincipalSenior.add(new EmptyRoom("Sala Principal"));
        roomMap.put("Senior-Sala Principal", salaPrincipalSenior);

        List<Room> salaExteriorSenior = new ArrayList<>();
        salaExteriorSenior.add(new EmptyRoom("Sala Exterior"));
        roomMap.put("Senior-Sala Exterior", salaExteriorSenior);

        List<Room> camposElyseosSenior = new ArrayList<>();
        camposElyseosSenior.add(new KeyRoom("llave secreta de snippet"));
        roomMap.put("Senior-Campos Elíseos", camposElyseosSenior);

        List<Room> habitacion1Senior = new ArrayList<>();
        habitacion1Senior.add(new LockedRoom("Habitacion1", "llave secreta de snippet"));
        roomMap.put("Senior-Habitacion1", habitacion1Senior);

        List<Room> habitacionSecretaSenior = new ArrayList<>();
        habitacionSecretaSenior.add(new KeyRoom("llave"));
        roomMap.put("Senior-Habitacion Secreta", habitacionSecretaSenior);

        List<Room> habitacion2Senior = new ArrayList<>();
        habitacion2Senior.add(new TreasureRoom());
        roomMap.put("Senior-Habitacion2", habitacion2Senior);

        List<Room> habitacion3Senior = new ArrayList<>();
        habitacion3Senior.add(new KeyRoom("llave del conocimiento"));
        roomMap.put("Senior-Habitacion3", habitacion3Senior);


    }

    public boolean hasRoom(String key) {
        return roomMap.containsKey(key);
    }

    public List<Room> getRooms(String key) {
        return roomMap.get(key);
    }

    // Método para obtener la información de la puerta (la parte de transición entre salas)
    // Este método se puede mantener similar, pues sigue devolviendo un DoorInfo
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
