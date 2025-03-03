package play;

public class DoorInfo {
    String targetRoom;
    int targetRow, targetCol;
    String requiredKey; // Si es null, no requiere llave
    String nextLevel;   // Si no es null, la transición cambia de nivel

    public DoorInfo(String targetRoom, int targetRow, int targetCol, String requiredKey, String nextLevel) {
        this.targetRoom = targetRoom;
        this.targetRow = targetRow;
        this.targetCol = targetCol;
        this.requiredKey = requiredKey;
        this.nextLevel = nextLevel;
    }
}