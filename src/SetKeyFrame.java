/**
 * SetKeyFrame.java
 * by Tobin Nickels
 *
 * This frame will pop up and prompt the user to press a key.
 * That will key be bound to a direction in GamePanel.java and
 * the frame will dispose.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class SetKeyFrame extends JFrame implements KeyListener {
    private String direction_to_set;
    private GamePanel gamePanel;
    private JPanel popup;
    public SetKeyFrame(String direction,GamePanel g){
        setBounds(250,200,200,200);
        direction_to_set = direction;
        this.gamePanel = g;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        setResizable(false);
        setFocusable(true);
        requestFocus();
        popup = new JPanel(new BorderLayout());
        popup.add(new JLabel("Press a key to set"+direction),BorderLayout.CENTER);
        add(popup);
        addKeyListener(this);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        gamePanel.setDirection(direction_to_set,e.getKeyCode());
        dispose();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}

