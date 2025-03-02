package enemies;

import player.Player;
import questions.QuestionPool;

public class BugEnemy {
    public static void encounter(Player player) {
        System.out.println("¡Un Bug ha aparecido! 🐞");
        boolean correct = QuestionPool.askQuestionForLevel(player.getLevel());
        if(correct) {
            System.out.println("¡Has derrotado al Bug! ✅");
        } else {
            player.receiveDamage(1);
            System.out.println("El Bug te ha dañado. ❌");
        }
    }
}
