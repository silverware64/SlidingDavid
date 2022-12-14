/**
 * GamePanel.java
 *
 * This class is responsible for game logic, event listeners, and displaying graphics of the tiles.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

public class GamePanel extends JPanel {
    private final Picture picture;
    private final int nrOfSwaps = 100;
    Vector<Vector<Integer>> cells;
    private Point empty_cell;
    private int n, step;
    private int startX, startY;

    private KeyAdapter keyboard_controls;
    private MouseAdapter mouse_controls;
    private boolean use_keyboard = false;
    private int left_key = 37;
    private int up_key = 38;
    private int right_key = 39;
    private int down_key = 40;

    /*
        0 = No image
        1 = Image loaded by user
        2 = All game
        3 = Game done
     */
    private int gameState = 0;

    /**
     * Initialize GamePanel with correct size.
     *
     * @param p
     *      Reference to a picture object with no Image yet.
     */
    public GamePanel(Picture p) {
        picture = p;
        setBounds(0, 0, picture.getSize(), picture.getSize());
        setFocusable(true);
    }

    /**
     *
     * @return
     *      Int representing current game state.
     */
    public int getGameState() {
        return this.gameState;
    }

    /**
     *
     * @param state
     *      Change game state to given state.
     */
    public void setGameState(int state) {
        this.gameState = state;
    }

    /**
     * Bind moving the tiles to different custom keys.
     *
     * @param direction
     *      Which direction to rebind
     * @param key
     *      Key to bind it to.
     */
    public void setDirection(String direction, int key){
        if(direction.equals("left")){
            left_key = key;
        } else if (direction.equals("up")) {
            up_key = key;
        } else if (direction.equals("right")) {
            right_key = key;
        } else if (direction.equals("down")) {
            down_key = key;
        }
    }

    /**
     * Split image into "cells" and start the game.
     *
     * @param difficulty
     *      Int representing the dimensions of the puzzle or "difficulty".
     */
    public void startGame(int difficulty) {
        gameState = 2;
        this.n = difficulty;
        this.step = picture.getSize() / n;
        picture.dissectImg(n);
        generatePuzzle();
        repaint();
        gamePhase();
    }

    private void generatePuzzle() {
        new Thread(() -> {
            ArrayList<Integer> randomizer = new ArrayList<>();
            for (int i = 0; i < (n * n - 1); i++)
                randomizer.add(i);

            cells = new Vector<>();
            cells.setSize(n);
            int next = 0;
            for (int i = 0; i < n; i++) {
                cells.set(i, new Vector<>());
                cells.get(i).setSize(n);
                for (int j = 0; j < n; j++) {
                    if (i == n - 1 && j == n - 1)
                        cells.get(i).set(j, -1);
                    else {
                        cells.get(i).set(j, randomizer.get(next));
                        next++;
                    }
                }
            }
            Random rand = new Random();
            for (int i = 0; i < nrOfSwaps; i++) {
                int a = rand.nextInt(n * n - 1);
                int b = a;
                while (b == a)
                    b = rand.nextInt(n * n - 1);

                int t = randomizer.get(a);
                randomizer.set(a, randomizer.get(b));
                randomizer.set(b, t);
                next = 0;
                for (int j = 0; j < n; j++) {
                    cells.set(j, new Vector<>());
                    cells.get(j).setSize(n);
                    for (int k = 0; k < n; k++) {
                        if (j == n - 1 && k == n - 1) {
                            cells.get(j).set(k, -1);
                            empty_cell = new Point(j, k);
                        }
                        else {
                            cells.get(j).set(k, randomizer.get(next));
                            next++;
                        }
                    }
                }
                repaint();
                try {
                    Thread.sleep(25);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        }).start();





    }
    /*
    Returns an Vector<Integer> of 4 elements, each representing a move, and the
    resulting manhattan score. If a move is invalid, then the corresponding
    element is null. The order is important:
    [0]: UP
    [1]: DOWN
    [2]: LEFT
    [3]: RIGHT
     */
    protected Vector<Integer> getHint(String lastHint){
        String[] lastHints = lastHint.split(" ");
        Vector<Integer> manhattanScores = new Vector<>();
        if (gameState != 2){
            return manhattanScores;
        }
        /*
        Find the "blank"
         */
        Point dest = new Point();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (cells.get(i).get(j) == -1){
                    dest.x = i;
                    dest.y = j;
                }
            }
        }
        /*
        Create a copy of Cells that we can manipulate
         */
        Vector<Vector<Integer>> tempCells = new Vector<>(cells);
        /*
        Find the Manhattan Score of each potential move
         */
        Vector<Point> moves = new Vector<>();
        Point up = new Point(dest.x+1, dest.y);
        Point down = new Point(dest.x-1, dest.y);
        Point left = new Point(dest.x, dest.y+1);
        Point right = new Point(dest.x, dest.y-1);
        moves.add(up);
        moves.add(down);
        moves.add(left);
        moves.add(right);
        for (int m = 0; m < moves.size(); m++){
           if (validMove(moves.get(m), dest)){
               tempCells = swapTempCells(tempCells, moves.get(m), dest);
               manhattanScores.add(m, calcManhattan(tempCells));
               tempCells = swapTempCells(tempCells, dest, moves.get(m));
           }
           else{
               manhattanScores.add(m, null);
           }
        }
        for (String lh: lastHints){
            if (lh.equals("Up"))
                manhattanScores.set(1, null);
            if (lh.equals("Down"))
                manhattanScores.set(0, null);
            if (lh.equals("Left"))
                manhattanScores.set(3, null);
            if (lh.equals("Right"))
                manhattanScores.set(2, null);
        }
        return manhattanScores;
    }

    /*
    With this formula, "homeX = val / n, homeY = val % n",
    where n is the size of the board, we can calculate the
    home position of an element based on its value
    i.e. on a 3x3 board, 4 should be at 1,1.
    Then using the home position and the actual position
    we can calculate how many moves it would take to move
    the element to its home, and summing these move
    for every element is the Manhattan distance.
    */
    private int calcManhattan(Vector<Vector<Integer>> boardState){
        int manhattan = 0;
        int cur;
        int homeX;
        int homeY;
        for (int i = 0; i < boardState.size(); i++){
            for (int j = 0; j < boardState.get(0).size(); j++){
                cur = boardState.get(i).get(j);
                if (cur == -1)
                    cur = (n * n - 1);
                homeX = cur / n;
                homeY = cur % n;
                manhattan = manhattan + Math.abs((homeX - i) + (homeY - j));
            }
        }
        return manhattan;
    }

    /**
     * Remove mouse listener if one exists and set parameter representing if keyboard commands will be used.
     */
    public void turnOnKeyboard(){
        if(mouse_controls != null){
            removeMouseListener(mouse_controls);
        }
        use_keyboard = true;
        if(gameState == 2)
            gamePhase();
    }

    /**
     * Remove key listener if one exists and set parareter representing keyboard commands won't be used.
     */
    public void turnOnMouse(){
        if (keyboard_controls != null){
            removeKeyListener(keyboard_controls);
        }
        use_keyboard = false;
        if(gameState == 2)
            gamePhase();
    }


    /**
     * Create new key or mouse listener based on settings.
     * This method holds the actual gameplay of SlidingDavid.
     */
    private void gamePhase() {
        requestFocus();
        if(use_keyboard){
              keyboard_controls = new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    Point start = new Point(startX, startY);

                    //Try to swap the empty tile with an adjacent tile based on
                    //the pressed key.
                    if (e.getKeyCode() == left_key){
                        start = new Point(empty_cell.x,empty_cell.y+1);
                    }
                    if (e.getKeyCode() == up_key){
                        start = new Point(empty_cell.x+1,empty_cell.y);
                    }
                    if (e.getKeyCode() == right_key){
                        start = new Point(empty_cell.x,empty_cell.y-1);
                    }
                    if (e.getKeyCode() == down_key){
                        start = new Point(empty_cell.x-1,empty_cell.y);
                    }

                    if (validMove(start, empty_cell)) {
                        swapCells(start, empty_cell);
                        empty_cell = start;
                        MusicPlayer.playMoveSFX();
                        repaint();
                    }

                    if(gameOver()){
                        gameState = 3;
                    }
                    startX = -1;
                    startY = -1;
                    requestFocus();
                }
            };
              addKeyListener(keyboard_controls);
        } else {
            mouse_controls = new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    super.mouseClicked(e);

                    startX = e.getX();
                    startY = e.getY();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    super.mouseReleased(e);

                    Point start = coordToCell(new Point(startX, startY));
                    Point dest = coordToCell(new Point(e.getX(), e.getY()));

                    if (validMove(start, dest)) {
                        swapCells(start, dest);
                        empty_cell = start;
                        MusicPlayer.playMoveSFX();
                        repaint();
                    }

                    if(gameOver()){
                        gameState = 3;
                    }
                    startX = -1;
                    startY = -1;
                }
            };
            addMouseListener(mouse_controls);
        }

    }
    private boolean validMove(Point start, Point dest) {
        System.out.println("Start:");
        System.out.println(start);
        System.out.println("Empty:");
        System.out.println(dest);
        // Not in game phase.
        if (gameState != 2)
            return false;
        // Out of bounds check on start & dest
        if (start.x < 0 || start.y < 0)
            return false;
        if (dest.x < 0 || dest.y < 0)
            return false;
        if (start.x >= n || start.y >= n)
            return false;
        if (dest.x >= n || dest.y >= n)
            return false;
        // Not empty.
        if (cells.get(dest.x).get(dest.y) != -1)
            return false;
        // Up & down.
        if (Math.abs(start.x - dest.x) == 1) {
            if (Math.abs(start.y - dest.y) > 0)
                return false;
        }
        // Left & right.
        if (Math.abs(start.y - dest.y) == 1) {
            return Math.abs(start.x - dest.x) == 0;
        }
        if ((Math.abs(dest.y - start.y) > 1) || (Math.abs(dest.x - start.x) > 1)){
            return false;
        }
        return true;
    }

    private Point coordToCell(Point p) {
        if (p.x < 0 || p.y < 0)
            return new Point(-1, -1);
        return new Point(p.y / step, p.x / step);
    }

    private Vector<Vector<Integer>> swapTempCells(Vector<Vector<Integer>> tempCells, Point start, Point dest) {
        int a = tempCells.get(start.x).get(start.y);
        int b = tempCells.get(dest.x).get(dest.y);
        tempCells.get(start.x).set(start.y, b);
        tempCells.get(dest.x).set(dest.y, a);
        return tempCells;
    }
    private void swapCells(Point x, Point y) {
        int a = cells.get(x.x).get(x.y);
        int b = cells.get(y.x).get(y.y);
        cells.get(x.x).set(x.y, b);
        cells.get(y.x).set(y.y, a);
    }

    private boolean gameOver() {
        /*
        for(int i = 0; i < n; i++)
        System.out.println(cells.get(i));
        System.out.println();
        */

        int expectedVal;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                expectedVal = (i + j) + i * (n - 1);
                if (i == n - 1 && j == n - 1)
                    expectedVal = -1;
                if (cells.get(i).get(j) != expectedVal) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (gameState == 0)
            return;
        if (gameState == 1)
            g.drawImage(picture.getImg(), 0, 0, null);
        else if (gameState > 1) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (cells.get(i).get(j) != -1) {
                        g.drawImage(picture.getImgAtCell(cells.get(i).get(j)), j * step, i * step, null);
                    } else {
                        g.setColor(new Color(238, 238, 238));
                        g.drawRect(j * step, i * step, step, step);
                    }
                }
            }
        }
    }
}
