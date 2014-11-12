/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cs.ttt;

/**
 *
 * @author s506571
 */
public class WeightMover implements MoveListener {
    public boolean enabled = true;
    public void moved(BoardPanel bp) {
        if (bp.getBoard().isTerminal() || !enabled)
            return;
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
            bp.setBoardAndSymbol(bp.getBoard().move(mini, Board.Symbol.O), Board.Symbol.X);
        }
    }
}
