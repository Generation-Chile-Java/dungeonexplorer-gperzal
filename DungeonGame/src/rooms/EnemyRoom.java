package rooms;

import player.Player;
import questions.QuestionPool;

public class EnemyRoom implements Room {
    @Override
    public void enter(Player player) {
        System.out.println("¡Cuidado! Te has encontrado con un enemigo: Bug 🐞");
        // Se utiliza el pool de preguntas para el nivel del jugador (aquí usamos "Trainee" para el ejemplo)
        boolean correct = QuestionPool.askQuestionForLevel("Trainee");
        if(correct) {
            System.out.println("¡Has vencido al Bug! ✅");
        } else {
            player.receiveDamage(1);
            System.out.println("El Bug te ha dañado. ❌");
        }
    }
}
