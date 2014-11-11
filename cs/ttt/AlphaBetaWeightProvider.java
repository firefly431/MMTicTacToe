/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cs.ttt;

/**
 *
 * @author s506571
 */
class AlphaBetaWeightProvider extends WeightProvider {
    public int calculateWeight(Board board, Board.Symbol consider) {
        return board.alphaBeta(consider);
    }
}
