package cs.ttt;

public interface MoveListener {
    /**
     * Called when a player moves.
     * also called when listener added
     */
    public void moved(BoardPanel bp);
}
