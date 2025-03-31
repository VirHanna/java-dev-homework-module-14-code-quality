package goitpackage.game;

import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class App {
    private static final char[] BOX = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
    private static final Scanner SCAN = new Scanner(System.in);
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {
        resetBoard();
        LOGGER.info("Enter box number to select. Enjoy!");

        int winner = 0;

        do {
            updateBoard();
            playerMove();
            if (checkWinner('X')) {
                winner = 1;
            } else if (!isBoxAvailable()) {
                winner = 3;
            } else {
                computerMove();
                if (checkWinner('O')) {
                    winner = 2;
                }
            }
        } while (!exitGame(winner));
    }

    private static boolean exitGame(int winner) {
        if (winner != 0) {
            printWinner(winner);
            SCAN.close();
            return true;
        }
        return false;
    }

    private static void resetBoard() {
        for (int i = 0; i < 9; i++) {
            BOX[i] = ' ';
        }
    }

    private static void updateBoard() {
        LOGGER.info(() -> String.format(
                "%n%n %c | %c | %c %n"
                        + "-----------%n %c | %c | %c %n"
                        + "-----------%n %c | %c | %c %n",
                BOX[0], BOX[1], BOX[2],
                BOX[3], BOX[4], BOX[5],
                BOX[6], BOX[7], BOX[8]
        ));
    }

    private static boolean isBoxAvailable() {
        return IntStream.range(0, 9)
                .anyMatch(i -> BOX[i] != 'X' && BOX[i] != 'O');
    }

    private static void computerMove() {
        int rand;
        do {
            rand = ThreadLocalRandom.current().nextInt(0, 9);
        } while (BOX[rand] == 'X' || BOX[rand] == 'O');
        BOX[rand] = 'O';
    }

    private static void playerMove() {
        int input;
        while (true) {
            input = SCAN.nextByte();
            if (input > 0 && input < 10) {
                if (BOX[input - 1] == 'X' || BOX[input - 1] == 'O') {
                    LOGGER.info("That one is already in use. Enter another.");
                } else {
                    BOX[input - 1] = 'X';
                    break;
                }
            } else {
                LOGGER.info("Invalid input. Enter again.");
            }
        }
    }

    private static void printWinner(int winner) {
        switch (winner) {
            case 1 -> LOGGER.info("You won the game!\nCreated by Shreyas Saha. Thanks for playing!");
            case 2 -> LOGGER.info("You lost the game!\nCreated by Shreyas Saha. Thanks for playing!");
            case 3 -> LOGGER.info("It's a draw!\nCreated by Shreyas Saha. Thanks for playing!");
            default -> LOGGER.info("Error");
        }
    }

    private static boolean checkWinner(char player) {
        int[][] winPatterns = {
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8},    // Горизонтальні
                {0, 3, 6}, {1, 4, 7}, {2, 5, 8},    // Вертикальні
                {0, 4, 8}, {2, 4, 6}                // Діагональні
        };

        return Stream.of(winPatterns)
                .anyMatch(pattern -> BOX[pattern[0]] == player &&
                        BOX[pattern[1]] == player &&
                        BOX[pattern[2]] == player);
    }
}
