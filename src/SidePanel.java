/*
    Name: SidePanel.java
 */

import javax.swing.*;
import java.awt.*;

public class SidePanel extends JPanel {
    private final GamePanel gamePanel;
    private TimeController timeController;
    private JLabel currentScore;
    private JLabel lastScore;
    private JLabel hsText;
    private JLabel hsScore;
    private JLabel diff;
    private JRadioButton buttonEasy;
    private JRadioButton buttonNormal;
    private JRadioButton buttonMedium;
    private JRadioButton buttonHard;
    private JRadioButton buttonExpert;
    private JRadioButton buttonInsane;
    private JButton buttonStart;
    private JButton buttonReset;

    public SidePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        setLayout(null);
        setBounds(800, 0, 200, 800);
        setup();
    }

    private void setup() {
        setupScoreUI();
        setupDifficultyButtons();
        setupStartResetButtons();
        updateScoreText();
        timeController = new TimeController(this.gamePanel, this, currentScore);
    }

    private void setupScoreUI() {
        JLabel timeL = new JLabel("Time:");
        timeL.setFont(new Font("Arial", Font.BOLD, 15));
        timeL.setBounds(10, 40, 100, 50);
        currentScore = new JLabel("0:0:0");
        currentScore.setFont(new Font("Arial", Font.PLAIN, 14));
        currentScore.setBounds(80, 40, 100, 50);
        add(timeL);
        add(currentScore);

        JLabel lastTimeL = new JLabel("Last score:");
        lastTimeL.setBounds(10, 100, 100, 50);
        lastScore = new JLabel("0:0:0");
        lastScore.setBounds(125, 101, 100, 50);
        lastScore.setFont(new Font("Arial", Font.PLAIN, lastScore.getFont().getSize()));
        add(lastTimeL);
        add(lastScore);

        hsText = new JLabel("Highscore(4x4):");
        hsText.setBounds(10, 125, 140, 50);
        hsScore = new JLabel();
        hsScore.setBounds(125, 126, 100, 50);
        hsScore.setFont(new Font("Arial", Font.PLAIN, hsScore.getFont().getSize()));
        add(hsText);
        add(hsScore);
    }

    private void setupDifficultyButtons() {
        diff = new JLabel("DIFFICULTY");
        diff.setBounds(10, 250, 100, 50);
        add(diff);

        buttonEasy = new JRadioButton("Easy (3x3)");
        buttonNormal = new JRadioButton("Normal (4x4)");
        buttonMedium = new JRadioButton("Medium (5x5)");
        buttonHard = new JRadioButton("Hard (6x6)");
        buttonExpert = new JRadioButton("Expert (7x7)");
        buttonInsane = new JRadioButton("Insane (10x10)");
        buttonEasy.setBounds(10, 300, 200, 30);
        buttonNormal.setBounds(10, 330, 200, 30);
        buttonMedium.setBounds(10, 360, 200, 30);
        buttonHard.setBounds(10, 390, 200, 30);
        buttonExpert.setBounds(10, 420, 200, 30);
        buttonInsane.setBounds(10, 450, 200, 30);
        buttonEasy.addActionListener(e -> updateScoreText());
        buttonNormal.addActionListener(e -> updateScoreText());
        buttonMedium.addActionListener(e -> updateScoreText());
        buttonHard.addActionListener(e -> updateScoreText());
        buttonExpert.addActionListener(e -> updateScoreText());
        buttonInsane.addActionListener(e -> updateScoreText());

        ButtonGroup diffButtons = new ButtonGroup();
        diffButtons.add(buttonEasy);
        diffButtons.add(buttonNormal);
        diffButtons.add(buttonMedium);
        diffButtons.add(buttonHard);
        diffButtons.add(buttonExpert);
        diffButtons.add(buttonInsane);

        add(buttonEasy);
        add(buttonNormal);
        add(buttonMedium);
        add(buttonHard);
        add(buttonExpert);
        add(buttonInsane);
        buttonNormal.setSelected(true);
    }

    private void setupStartResetButtons() {
        buttonStart = new JButton("Start");
        buttonStart.setBounds(30, 530, 100, 30);
        add(buttonStart);
        buttonStart.setEnabled(false);
        buttonStart.addActionListener(e -> {
            gamePanel.startGame(getSelectedDifficulty());
            setDifficultyEnabled(false);
            buttonStart.setEnabled(false);
            new Thread(timeController).start();
        });

        buttonReset = new JButton("Reset");
        buttonReset.setBounds(40, 570, 80, 20);
        add(buttonReset);
        buttonReset.setEnabled(false);
        buttonReset.addActionListener(e -> {
            gamePanel.setGameState(1);
            settingsPhase();
        });
    }

    public void imgLoaded() {
        buttonReset.setEnabled(true);
        gamePanel.setGameState(1);
        settingsPhase();
    }

    private void updateScoreText() {
        hsText.setText("Highscore(" + getSelectedDifficulty() + "x" + getSelectedDifficulty() + "):");
        hsScore.setText(Scores.getHighScore(getSelectedDifficulty()));
    }

    private void setDifficultyEnabled(boolean b) {
        diff.setEnabled(b);
        buttonEasy.setEnabled(b);
        buttonNormal.setEnabled(b);
        buttonMedium.setEnabled(b);
        buttonHard.setEnabled(b);
        buttonExpert.setEnabled(b);
        buttonInsane.setEnabled(b);
    }

    public int getSelectedDifficulty() {
        if (buttonEasy.isSelected())
            return 3;
        if (buttonNormal.isSelected())
            return 4;
        if (buttonMedium.isSelected())
            return 5;
        if (buttonHard.isSelected())
            return 6;
        if (buttonExpert.isSelected())
            return 7;
        if (buttonInsane.isSelected())
            return 10;
        return 0;
    }

    public void settingsPhase() {
        gamePanel.repaint();
        setDifficultyEnabled(true);
        buttonStart.setEnabled(true);
        currentScore.setText("0:0:0");
    }

    public void finishPhase() {
        Scores.addScore(currentScore.getText(), getSelectedDifficulty());
        lastScore.setText(currentScore.getText());
        hsScore.setText(Scores.getHighScore(getSelectedDifficulty()));
    }

    public void updateScoreFiles() {
        for (int i = 3; i <= 7; i++)
            Scores.saveSession(i);
        Scores.saveSession(10);
    }
}
