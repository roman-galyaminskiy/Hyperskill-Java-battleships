package battleship;

import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        for (int i = 0; i < players.length; i++) {
            players[i] = new Player();
        }

        prepare();
        battle();
        exit();
    }

    private static void prepare() {
        for (var player : players) {
            System.out.printf("Player %d, place your ships on the game field\n\n", activePlayer + 1);
            player.placeShips();
            switchPlayers();
        }
    }

    private static void battle() {
        int[] shot = new int[2];
        int result = 0;

        while (result < 3) {
            for (var player : players) {
//                do {
                    System.out.printf("Player %d, it's your turn:\n\n", activePlayer + 1);

                    shot = player.shoot();

                    if (activePlayer == 0) {
                        result = players[1].checkHit(shot);
                        players[0].lastShotResults((result == 1 || result == 2));

                    } else {
                        result = players[0].checkHit(shot);
                        players[1].lastShotResults((result == 1 || result == 2));
                    }

                    if (result == 3) {
                        return;
                    } else if (result == 1) {
                        System.out.println("You hit a ship!");
                    } else if (result == 2) {
                        System.out.println("You sank a ship!");
//                        break;
                    } else {
                        System.out.println("You missed!");
                    }

//                } while (result > 0 && result < 3);

                switchPlayers();
            }
        }
    }

    private static void exit() {
        System.out.printf("Player %d, you sank the last ship. You won. Congratulations!\n", activePlayer + 1);
    }

    private static void switchPlayers() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nPress Enter and pass the move to another player\n...");
            String line = scanner.nextLine();

            if (line.isEmpty()) {
                if (activePlayer == 0) {
                    activePlayer = 1;
                } else {
                    activePlayer = 0;
                }

                break;
            }
        }

//        System.out.printf("DEBUG: active player index: %d\n", activePlayer);
    }

    private static Player[] players = new Player[2];
    private static int activePlayer = 0; // 0 - player 1, 1 - player 2
}
