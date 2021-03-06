package cs.ttt;

/**
 * A MoveListener that moves when it's its turn
 */
public class WeightMover implements MoveListener {
    public boolean enabled = true;
    public void moved(BoardPanel bp) {
        if (bp.getBoard().isTerminal() || !enabled) // if game over or disabled
            return;
        if (bp.getSymbol().min()) { // it's our turn
            int[] w = bp.getWeights(); // get the best move
            int minw = Integer.MAX_VALUE;
            int mini = -1;
            for (int i = 0; i < w.length; i++) {
                if (w[i] < minw && bp.getBoard().getBoard()[i] == Board.Symbol.NONE) {
                    mini = i;
                    minw = w[i];
                }
            }
            // move
            bp.setBoardAndSymbol(bp.getBoard().move(mini, Board.Symbol.O), Board.Symbol.X);
        }
    }
}
