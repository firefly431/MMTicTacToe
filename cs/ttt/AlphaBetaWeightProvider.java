package cs.ttt;

/**
 * Implementation of WeightProvider using the alphaBeta method of the board
 */
class AlphaBetaWeightProvider extends WeightProvider {
    public int calculateWeight(Board board, Board.Symbol consider) {
        return board.alphaBeta(consider);
    }
}
