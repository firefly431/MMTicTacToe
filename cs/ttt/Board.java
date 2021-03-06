package cs.ttt;

import java.util.ArrayList;

/**
 * Board class.
 */
public class Board {
    // use an enum because there are only 3 states
    public static enum Symbol {
        NONE(0), X(1), O(2);
        Symbol(int val) {
            byteval = val;
        }
        /**
         * Get the other symbol
         */
        public Symbol other() {
            switch (this) {
                case NONE:
                    return NONE;
                case X:
                    return O;
                case O:
                    return X;
                default: // function may not return without this
                    throw new RuntimeException("I do not exist");
            }
        }
        /**
         * Get the score of this (X maxes so it's 1; likewise, O is -1)
         */
        public int getScore() {
            switch (this) {
                case NONE:
                    return 0;
                case X:
                    return 1;
                case O:
                    return -1;
                default:
                    return 0;
            }
        }
        /**
         * Get our symbol
         */
        public String getSymbol() {
            switch (this) {
                case NONE:
                    return " ";
                case X:
                    return "X";
                case O:
                    return "O";
                default:
                    return "";
            }
        }
        // X is max
        public boolean max() {
            return this == X;
        }
        public boolean min() {
            return this == O;
        }
        public int byteval;
        public static final int BITWIDTH = 2;
        // will fit in 2 bits
        // so we can serialize a board easily (good hashCode)
    }

    // 3x3 is 9
    private Symbol[] board;

    public Board() {
        board = new Symbol[9];
        board[0] = board[1] = board[2] =
        board[3] = board[4] = board[5] =
        board[6] = board[7] = board[8] = Symbol.NONE;
    }
    // we can guarantee that hashCode is unique so it's really easy to implement equals()
    @Override
    public int hashCode() {
        int ret = 0;
        // set the bits of ret
        // where the LSB (least significant bit) is part of the first element of board
        for (int i = 0; i < board.length; i++) {
            ret |= board[i].byteval << (i * Symbol.BITWIDTH);
        }
        return ret;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        return obj.hashCode() == hashCode();
    }

    /**
     * Return a copy of this board after moving in a specified position.
     * Does not modify this board
     * @param i The position at which to move
     * @param what The symbol to place at i
     * @return a new Board where return.board[i] == what
     */
    public Board move(int i, Symbol what) {
        Board b = new Board();
        System.arraycopy(board, 0, b.board, 0, board.length);
        b.board[i] = what;
        return b;
    }

    /**
     * Returns the score of this board not considering depth
     * @return 1 if X wins, -1 in O wins, and 0 if no one wins.
     */
    public int getScore() {
        // check horizontal
        for (int i = 0; i < 9; i += 3)
            if (board[i] == board[i + 1] && board[i + 1] == board[i + 2])
                if (board[i] != Symbol.NONE)
                    return board[i].getScore();
        // check vertical
        for (int i = 0; i < 3; i++)
            if (board[i] == board[i + 3] && board[i + 3] == board[i + 6])
                if (board[i] != Symbol.NONE)
                    return board[i].getScore();
        // check diagonal
        if (board[0] == board[4] && board[4] == board[8])
            if (board[0] != Symbol.NONE)
                return board[0].getScore();
        if (board[2] == board[4] && board[4] == board[6])
            if (board[2] != Symbol.NONE)
                return board[2].getScore();
        return 0;
    }

    /**
     * Get the score for this board at a specific depth of solving
     * (Depth is number of moves in)
     */
    public int getScore(int depth) {
        int score = getScore();
        score *= 10;
        if (score > 0)
            score -= depth;
        if (score < 0)
            score += depth;
        return score;
    }

    /**
     * Is the board filled with things other than NONE
     */
    public boolean isFilled() {
        for (Symbol s : board)
            if (s == Symbol.NONE)
                return false;
        return true;
    }
    
    /**
     * Is this a terminal node of the game tree; is the game over
     */
    public boolean isTerminal() {
        return isFilled() || getScore() != 0;
    }

    // get possible moves
    // may return a non-empty list if terminal
    public ArrayList<Board> getMoves(Symbol which) {
        ArrayList<Board> ret = new ArrayList<Board>();
        for (int i : getMovePositions())
            ret.add(move(i, which));
        return ret;
    }

    // get places to move (filter board where square == NONE)
    public ArrayList<Integer> getMovePositions() {
        ArrayList<Integer> ret = new ArrayList<Integer>();
        for (int i = 0; i < board.length; i++) {
            if (board[i] == Symbol.NONE) {
                ret.add(i);
            }
        }
        return ret;
    }
    public Symbol[] getBoard() {
        return board;
    }
    
    public int alphaBeta(Symbol player) {
        return alphaBeta(player, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
    }

    /**
     * Alpha-beta pruning. Returns the score of this board.
     * Results are guaranteed to be the same as minimax.
     *
     * To better understand this algorithm, see the Wikipedia article
     */
    public int alphaBeta(Symbol player, int alpha, int beta, int depth) {
        if (isTerminal()) { // simple
            return getScore(depth);
        }
        if (player.max()) { // if we're maxing
            for (Board b : getMoves(player)) { // consider all the moves
                int score = b.alphaBeta(player.other(), alpha, beta, depth + 1);
                if (score > alpha)
                    alpha = score; // better move
                if (alpha >= beta)
                    return alpha; // cut off
            }
            return alpha; // best move
        } else if (player.min()) {
            for (Board b : getMoves(player)) {
                int score = b.alphaBeta(player.other(), alpha, beta, depth + 1);
                if (score < beta)
                    beta = score; // better move
                if (alpha >= beta)
                    return beta; // cut off
            }
            return beta; // best move
        } else {
            return 0;
        }
    }
    
    public int minimax(Symbol player) {
        return minimax(player, 0);
    }

    /**
     * Minimax algorithm.
     * Results are the same as with alpha-beta pruning, but slightly slower
     */
    public int minimax(Symbol player, int depth) {
        if (isTerminal()) {
            return getScore(depth);
        }
        int bestScore = player.max() ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for (Board b : getMoves(player)) {
            int bscore = b.minimax(player.other(), depth + 1); // get the score of the board
            // if it's a better move
            if (player.max() && bscore > bestScore || player.min() && bscore < bestScore)
                bestScore = bscore; // set the best score
        }
        return bestScore;
    }
    // print this board
    public void print() {
        System.out.println(board[0].getSymbol()
                   + "|" + board[1].getSymbol()
                   + "|" + board[2].getSymbol());
        System.out.println("-----");
        System.out.println(board[3].getSymbol()
                   + "|" + board[4].getSymbol()
                   + "|" + board[5].getSymbol());
        System.out.println("-----");
        System.out.println(board[6].getSymbol()
                   + "|" + board[7].getSymbol()
                   + "|" + board[8].getSymbol());
    }
}
