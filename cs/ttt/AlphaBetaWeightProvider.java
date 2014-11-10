/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cs.ttt;

/**
 *
 * @author s506571
 */
class AlphaBetaWeightProvider implements WeightProvider {
    public int[] calculateWeights(Board board, Board.Symbol symbol) {
        int[] ret = new int[9];
        for (int i = 0; i < 9; i++) {
            ret[i] = board.alphaBeta(symbol);
        }
        return ret;
    }
}
