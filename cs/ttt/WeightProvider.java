/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cs.ttt;

/**
 *
 * @author s506571
 */
public interface WeightProvider {
    public int[] calculateWeights(Board board, Board.Symbol symbol);
}
