/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cs.ttt;

import java.util.Scanner;

/**
 *
 * @author s506571
 */
public class Main {
    public static void main(String argv[]) {
        Board gameBoard = new Board();
        Board.Symbol currentPlayer = Board.Symbol.X;
        Scanner s = new Scanner(System.in);
        while (!gameBoard.isTerminal()) {
            gameBoard.print();
            System.out.println();
            System.out.println("Scores:");
            for (int i : gameBoard.getMovePositions()) {
                Board consider = gameBoard.move(i, currentPlayer);
                System.out.println(i + ": " + consider.minimax(currentPlayer.other()));
            }
            System.out.println("It is " + currentPlayer.getSymbol() + "'s turn");
            System.out.println("We are " + (currentPlayer.max() ? "maximizing" : "minimizing"));
            int move = s.nextInt();
            gameBoard = gameBoard.move(move, currentPlayer);
            currentPlayer = currentPlayer.other();
        }
    }
}
