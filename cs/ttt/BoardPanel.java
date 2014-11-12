/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cs.ttt;

import cs.ttt.Board.Symbol;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.*;

public class BoardPanel extends JPanel {
    private Board board;
    private int[] weights;
    private Board.Symbol symbol;
    private WeightProvider provider;
    private ArrayList<MoveListener> listeners;
    private Board.Symbol startSymbol;
    private boolean weightsVisible = true;

    class BoardClickListener extends MouseAdapter {
        @Override
        public void mouseReleased(MouseEvent e) {
            if (BoardPanel.this.getBoard().isTerminal()) {
                // reset the board on click
                BoardPanel.this.setBoard(new Board());
                // set to starting symbol
                BoardPanel.this.setSymbol(BoardPanel.this.getStartSymbol());
            } else {
                int x = e.getX() * 3 / BoardPanel.this.getWidth();
                int y = e.getY() * 3 / BoardPanel.this.getHeight();
                if (x < 0 || x >= 3 || y < 0 || y >= 3)
                    return; // out of bounds
                int i = x + y * 3;
                if (BoardPanel.this.getBoard().getBoard()[i] == Board.Symbol.NONE)
                    BoardPanel.this.setBoardAndSwitch(BoardPanel.this.getBoard().move(x + y * 3, BoardPanel.this.getSymbol()));
            }
            for (MoveListener l : listeners)
                l.moved(BoardPanel.this);
        }
    }
    public BoardPanel() {
        this(new Board());
    }
    public BoardPanel(Board b) {
        this(b, new int[9]);
    }
    public BoardPanel(Board b, int[] weights) {
        setBoard(b);
        setWeights(weights);
        addMouseListener(new BoardClickListener());
        setStartSymbol(Symbol.X);
        setSymbol(getStartSymbol());
        setBackground(Color.WHITE);
        provider = null;
        listeners = new ArrayList<MoveListener>();
    }
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600, 600);
    }
    // must call revalidate and repaint yourself
    protected void calculateWeights() {
        if (provider != null)
            setWeights(getProvider().calculateWeights(getBoard(), getSymbol()));
    }
    public void setBoard(Board b) {
        board = b;
        calculateWeights();
        revalidate();
        repaint();
    }
    public void setBoardAndSwitch(Board b) {
        board = b;
        symbol = symbol.other();
        calculateWeights();
        revalidate();
        repaint();
    }
    public void setBoardAndSymbol(Board b, Board.Symbol s) {
        board = b;
        symbol = s;
        calculateWeights();
        revalidate();
        repaint();
    }
    public Symbol getStartSymbol() {
        return startSymbol;
    }
    public void setStartSymbol(Symbol startSymbol) {
        this.startSymbol = startSymbol;
    }
    public Board getBoard() {
        return board;
    }
    public WeightProvider getProvider() {
        return provider;
    }
    public void setProvider(WeightProvider provider) {
        this.provider = provider;
        if (provider != null)
            setWeights(provider.calculateWeights(getBoard(), getSymbol()));
    }
    public void addListener(MoveListener listener) {
        listeners.add(listener);
        if (listener != null)
            listener.moved(this);
    }
    public boolean removeListener(MoveListener listener) {
        return listeners.remove(listener);
    }
    public int[] getWeights() {
        return weights;
    }
    public void setWeights(int[] weights) {
        if (weights.length != 9) {
            this.weights = new int[9];
            System.arraycopy(weights, 0, this.weights, 0, 9);
        } else {
            this.weights = weights;
        }
        revalidate();
        repaint();
    }
    public int getWeight(int i) {
        return weights[i];
    }
    public void setWeight(int i, int weight) {
        weights[i] = weight;
        revalidate();
        repaint();
    }
    public Symbol getSymbol() {
        return symbol;
    }
    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
        calculateWeights();
        revalidate();
        repaint();
    }
    public void switchSymbol() {
        setSymbol(getSymbol().other());
    }
    public void setWeightsVisible(boolean state) {
        weightsVisible = state;
        revalidate();
        repaint();
    }
    public boolean getWeightsVisible() {
        return weightsVisible;
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (board != null) {
            for (int i = 0; i < 9; i++) {
                int px = i % 3;
                int py = i / 3;
                int pl = getWidth() * px / 3;
                int pr = getWidth() * (px + 1) / 3;
                int pt = getHeight() * py / 3;
                int pb = getHeight() * (py + 1) / 3;
                int opl = pl, opr = pr, opt = pt, opb = pb;
                // padding
                int padding = Math.min(getWidth(), getHeight()) / 60;
                if (getWidth() < 15 || getHeight() < 15)
                    padding = 1;
                if (getWidth() < 10 || getHeight() < 10)
                    padding = 0;
                pt += padding;
                pb -= padding;
                pl += padding;
                pr -= padding;
                if (weightsVisible) {
                    Color mixColor = weights[i] < 0 ? Color.BLUE : Color.RED;
                    int mixFactor = Math.abs(weights[i]);
                    int newR = 255 - (255 - mixColor.getRed())   * mixFactor / 10;
                    int newG = 255 - (255 - mixColor.getGreen()) * mixFactor / 10;
                    int newB = 255 - (255 - mixColor.getBlue())  * mixFactor / 10;
                    g.setColor(new Color(newR, newG, newB));
                    g.fillRect(opl, opt, opr - opl, opb - opt);
                }
                switch (board.getBoard()[i]) {
                    case X:
                        g.setColor(Color.RED.darker());
                        g.drawLine(pl, pt, pr, pb);
                        g.drawLine(pr, pt, pl, pb);
                        break;
                    case O:
                        g.setColor(Color.BLUE.darker());
                        g.drawOval(pl, pt, pr - pl, pb - pt);
                        break;
                    default:
                }
            }
        }
        int top = 0;
        int my1 = getHeight() / 3;
        int my2 = getHeight() * 2 / 3;
        int bot = getHeight();
        int left = 0;
        int mx1 = getWidth() / 3;
        int mx2 = getWidth() * 2 / 3;
        int right = getWidth();
        g.setColor(Color.BLACK);
        g.drawLine(mx1, top, mx1, bot);
        g.drawLine(mx2, top, mx2, bot);
        g.drawLine(left, my1, right, my1);
        g.drawLine(left, my2, right, my2);
    }
}
