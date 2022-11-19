import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameFrame extends JFrame implements KeyListener {
    private GamePanel panel;
    public GameFrame(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800,720));
        setMaximumSize(new Dimension(800,720));
        setResizable(false);
        setVisible(true);

        addKeyListener(this);

        panel = new GamePanel();
        add(panel);
        panel.drawTiles();

        revalidate();
        pack();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        //Left
        if(e.getKeyCode() == 37){
            panel.board.left();
        } //Up
        else if(e.getKeyCode() == 38){
            panel.board.up();
        } //Right
        else if(e.getKeyCode() == 39){
            panel.board.right();
        } //Down
        else if(e.getKeyCode() == 40){
            panel.board.down();
        }
        panel.drawTiles();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}

class GamePanel extends JPanel {
    protected Board board;
    public GamePanel(){
        board = new Board(3);
        setBackground(Color.WHITE);
        setLayout(null);
        Tile[][] tiles = board.getTiles();
        for(int y = 0; y < board.size();y++){
            for (int x = 0; x < board.size();x++){
                if(tiles[y][x] != null){
                    add(tiles[y][x]);
                    tiles[y][x].setLocation(50*x+200,50*y+200);
                }
            }
        }
    }
    public void drawTiles(){
        Tile[][] tiles = board.getTiles();
        for(int y = 0; y < board.size();y++){
            for (int x = 0; x < board.size();x++){
                if(tiles[y][x] != null){
                    tiles[y][x].setLocation(50*x+200,50*y+200);
                }
            }
        }
        getParent().repaint();
    }
}