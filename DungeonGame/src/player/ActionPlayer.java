package player;

import java.util.Scanner;

public class ActionPlayer {
    private static Scanner scanner = new Scanner(System.in);

    // Realiza una pregunta al jugador y devuelve true si responde correctamente
    public static boolean askQuestion(String question, String[] answers, int correctIndex, String[] feedback) {
        System.out.println("\nPregunta: " + question);
        for(int i = 0; i < answers.length; i++){
            System.out.println((i + 1) + ". " + answers[i]);
        }
        System.out.print("Ingresa el número de tu respuesta: ");
        String input = scanner.nextLine();
        int response;
        try {
            response = Integer.parseInt(input) - 1;
        } catch(NumberFormatException e) {
            System.out.println("Entrada no válida.");
            return false;
        }
        if(response == correctIndex) {
            System.out.println("¡Correcto! ✅ " + feedback[response]);
            return true;
        } else {
            System.out.println("¡Incorrecto! ❌ " + feedback[response]);
            return false;
        }
    }
}
