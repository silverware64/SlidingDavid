/*
    Name: GamePanel.java
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

public class GamePanel extends JPanel {
    private final Picture picture;
    private final int nrOfSwaps = 100;
    Vector<Vector<Integer>> cells;
    private int n, step;
    private int startX, startY;

    /*
        0 = No image
        1 = Image loaded by user
        2 = All game
        3 = Game done
     */
    private int gameState = 0;

    public GamePanel(Picture p) {
        picture = p;
        setBounds(0, 0, picture.getSize(), picture.getSize());
    }

    public int getGameState() {
        return this.gameState;
    }

    public void setGameState(int state) {
        this.gameState = state;
    }

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
        ArrayList<Integer> randomizer = new ArrayList<>();
        for (int i = 0; i < (n * n - 1); i++)
            randomizer.add(i);

        Random rand = new Random();
        for (int i = 0; i < nrOfSwaps; i++) {
            int a = rand.nextInt(n * n - 1);
            int b = a;
            while (b == a)
                b = rand.nextInt(n * n - 1);

            int t = randomizer.get(a);
            randomizer.set(a, randomizer.get(b));
            randomizer.set(b, t);
        }

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
    private void gamePhase() {
        addMouseListener(new MouseAdapter() {
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
                    MusicPlayer.playMoveSFX();
                    repaint();
                }

                startX = -1;
                startY = -1;
            }
        });
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
