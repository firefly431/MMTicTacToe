/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cs.ttt;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

/**
 *
 * @author s506571
 */
public class Main {
    public static void main(String argv[]) {
        final BoardPanel bp = new BoardPanel();
        bp.setProvider(new AlphaBetaWeightProvider());
        final JLabel label = new JLabel();
        final WeightMover wm;
        bp.addListener(wm = new WeightMover());
        bp.addListener(new MoveListener() {
            public void moved(BoardPanel bp) {
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
        // menus
        JMenuBar menuBar = new JMenuBar();
        JMenu optionMenu = new JMenu("Options");
        optionMenu.setMnemonic(KeyEvent.VK_O);
        menuBar.add(optionMenu);
        final JCheckBoxMenuItem XGoesFirst = new JCheckBoxMenuItem("X goes first", true);
        optionMenu.add(XGoesFirst);
        final JCheckBoxMenuItem autoMove = new JCheckBoxMenuItem("Computer moves for O", true);
        optionMenu.add(autoMove);
        JMenu viewMenu = new JMenu("View");
        viewMenu.setMnemonic(KeyEvent.VK_V);
        menuBar.add(viewMenu);
        final JCheckBoxMenuItem showWeights = new JCheckBoxMenuItem("Show weights", true);
        viewMenu.add(showWeights);
        // add menu event listeners
        ItemListener listener = new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                boolean state = e.getStateChange() == ItemEvent.SELECTED;
                Object t = e.getItemSelectable();
                if (t == XGoesFirst)
                    bp.setStartSymbol(state ? Board.Symbol.X : Board.Symbol.O);
                if (t == autoMove)
                    wm.enabled = state;
                if (t == showWeights)
                    bp.setWeightsVisible(state);
            }
        };
        XGoesFirst.addItemListener(listener);
        autoMove.addItemListener(listener);
        showWeights.addItemListener(listener);
        // show the window
        mainFrame.setJMenuBar(menuBar);
        mainFrame.setContentPane(cp);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }
}
