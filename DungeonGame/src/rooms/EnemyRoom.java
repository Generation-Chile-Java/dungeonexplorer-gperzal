package rooms;

import player.Player;
import questions.QuestionPool;

public class EnemyRoom implements Room {
    @Override
    public void enter(Player player) {
        System.out.println("¡Cuidado! Te has encontrado con un enemigo: Bug 👾");
        // Se utiliza el pool de preguntas para el nivel del jugador.
        boolean correct = QuestionPool.askQuestionForLevel(player.getLevel());

        if (correct) {
            System.out.println("¡Has vencido al Bug! ✅");
        } else {
            // Verificar si el jugador tiene alguno de los ítems que actúan como escudo.
            String[] shields = {"Git", "IntelliJ", "Maven", "JDK", "Spring Boot"};
            boolean shieldUsed = false;
            for (String shield : shields) {
                if (player.getInventory().contains(shield)) {
                    shieldUsed = true;
                    System.out.println("¡Respuesta incorrecta, pero tu escudo (" + shield + ") te protegió! Se ha usado y eliminado de tu inventario.");
                    player.getInventory().remove(shield);
                    break;
                }
            }
            if (!shieldUsed) {
                player.receiveDamage(1);
                System.out.println("El Bug te ha dañado. 💔");
            }
        }
    }
}
