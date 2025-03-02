package player;

public class MovePlayer {
    public static int[] processMove(String command, int currentRow, int currentCol, int maxRows, int maxCols, Player player) {
        return player.move(command, currentRow, currentCol, maxRows, maxCols);
    }
}
