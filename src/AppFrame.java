/*
    Name: AppFrame.java
    Purpose: Acts as the controller of the program.
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class AppFrame extends JFrame {
    private final SidePanel sidePanel;
    private final Picture picture;
    private PictureFrame picFrame;
    private PastScoresFrame hScoreFrame;

    public AppFrame(){
        setLayout(null);
        setTitle("Sliding David Puzzle");

        picture = new Picture();
        GamePanel gamePanel = new GamePanel(picture);
        sidePanel = new SidePanel(gamePanel);
        add(gamePanel);
        add(sidePanel);
        setupMenuBar();
        setBounds(100, 100, picture.getSize()+400, picture.getSize() + 60);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        setResizable(false);
        MusicPlayer.playBGmusic();

        // Saves the score on close.
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                gamePanel.setGameState(0);
                sidePanel.updateScoreFiles();
                super.windowClosing(e);
            }
        });
    }

    private void setupMenuBar(){
        JMenuBar menuBar = new JMenuBar();
        JMenu menuFile = new JMenu("File");
        JMenuItem menuFileLoad = new JMenuItem("Load");
        menuFileLoad.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();

            if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
                try{
                    picture.setImg(ImageIO.read(fileChooser.getSelectedFile()));
                    sidePanel.imgLoaded();
                } catch (IOException exc){
                    exc.printStackTrace();
                }
            }
        });
        menuFile.add(menuFileLoad);
        menuBar.add(menuFile);

        JMenu menuView = new JMenu("View");
        JMenuItem menuViewScores = new JMenuItem("Scores");
        menuViewScores.addActionListener(e -> {
            if(hScoreFrame != null)
                hScoreFrame.dispose();
            hScoreFrame = new PastScoresFrame(sidePanel.getSelectedDifficulty());
        });
        menuView.add(menuViewScores);

        JMenuItem menuViewFullImage = new JMenuItem("Full Image");
        menuViewFullImage.addActionListener(e -> {
            if(picFrame != null)
                picFrame.dispose();
            picFrame = new PictureFrame(picture);
        });
        menuView.add(menuViewFullImage);
        menuBar.add(menuView);
        /*
         * This checks if an insane score of 30mins or less has been achieved
         * if so, then a new, secret menu option is added.
        */
        if (insaneScore()) {
            JMenu menuSecret = new JMenu("Secret");
            JMenuItem menuSecretUnlock = new JMenuItem("Secret Unlock");
            menuSecretUnlock.addActionListener(e -> {
                try{
                    picture.setImg(ImageIO.read(new File("Images/secret_unlock.jpg")));
                    sidePanel.imgLoaded();
                } catch (IOException exc){
                    exc.printStackTrace();
                }
            });
            menuSecret.add(menuSecretUnlock);
            menuBar.add(menuSecret);
        }

        setJMenuBar(menuBar);
    }

    /**
     * This function checks for an insane score of 30 mins or less
     */
    private Boolean insaneScore(){
        File input = new File("scores10.txt");
        try {
            Scanner scanner = new Scanner(input);
            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] score = line.split(":");
                if (score[0].equals("0")){
                    if (Integer.parseInt(score[1]) <= 30){
                        return true;
                    }
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}
