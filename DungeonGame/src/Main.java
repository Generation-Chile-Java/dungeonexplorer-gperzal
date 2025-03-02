import java.util.Scanner;
import play.DungeonGame;

public class Main {
    public static void main(String[] args) {
        // Inicializar scanner con UTF-8
        Scanner scanner = new Scanner(System.in, "UTF-8");

        // Inicializar y ejecutar el juego
        DungeonGame game = new DungeonGame();
        game.initialize(scanner);
        game.run();

        // Cerrar el scanner al finalizar
        scanner.close();
    }
}