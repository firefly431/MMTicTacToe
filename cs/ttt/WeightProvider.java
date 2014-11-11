/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cs.ttt;

/**
 *
 * @author s506571
 */
public abstract class WeightProvider {
    public int[] calculateWeights(Board board, Board.Symbol symbol) {
        int[] ret = new int[9];
        if (!board.isTerminal())
            for (int i = 0; i < 9; i++) {
                if (board.getBoard()[i] == Board.Symbol.NONE)
                    ret[i] = calculateWeight(board.move(i, symbol), symbol.other());
            }
        return ret;
    }
    // consider is the Symbol of the next move (the opposite of the current)
    public abstract int calculateWeight(Board board, Board.Symbol consider);
}
