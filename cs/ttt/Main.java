/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cs.ttt;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author s506571
 */
public class Main {
    public static void main(String argv[]) {
        BoardPanel bp = new BoardPanel();
        bp.setSymbol(Board.Symbol.O);
        bp.setProvider(new AlphaBetaWeightProvider());
        final JLabel label = new JLabel();
        bp.setListener(new MoveListener() {
            public void moved(BoardPanel bp) {
                if (bp.getBoard().isTerminal()) {
                    label.setText("Game over. " + (bp.getBoard().getScore() == 0 ? "No one" : (bp.getBoard().getScore() > 0 ? "X" : "O")) + " won.");
                    return;
                }
                if (bp.getSymbol().min()) {
                    int[] w = bp.getWeights();
                    int minw = Integer.MAX_VALUE;
                    int mini = -1;
                    for (int i = 0; i < w.length; i++) {
                        if (w[i] < minw && bp.getBoard().getBoard()[i] == Board.Symbol.NONE) {
                            mini = i;
                            minw = w[i];
                        }
                    }
                    System.out.println("Moving");
                    bp.setBoardAndSymbol(bp.getBoard().move(mini, Board.Symbol.O), Board.Symbol.X);
                }
                label.setText("It is " + bp.getSymbol().toString() + "'s turn");
                if (bp.getBoard().isTerminal()) {
                    label.setText("Game over. " + (bp.getBoard().getScore() == 0 ? "No one" : (bp.getBoard().getScore() > 0 ? "X" : "O")) + " won.");
                    return;
                }
            }
        });
        JFrame mainFrame = new JFrame("Tic-Tac-Toe");
        JPanel cp = new JPanel();
        cp.setLayout(new BorderLayout());
        cp.add(bp, BorderLayout.CENTER);
        cp.add(label, BorderLayout.NORTH);
        mainFrame.setContentPane(cp);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }
}
