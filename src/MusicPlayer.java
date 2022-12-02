import javax.sound.sampled.*;
import java.io.File;

/**
 *  Contains static methods to play the game background music and the puzzle move sound effect
 */
public class MusicPlayer {
    public static void playBGmusic(){
        try {
            AudioInputStream bgmStream = AudioSystem.getAudioInputStream(new File("Sounds/Fluffing-a-Duck.wav").getAbsoluteFile());
            Clip bgmClip = AudioSystem.getClip();
            bgmClip.open(bgmStream);
            bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
            bgmClip.start();
        }
        catch (Exception e){
            System.out.println("Problem getting sound file");
        };

    }

    public static void playMoveSFX(){
        try{
            AudioInputStream moveStream = AudioSystem.getAudioInputStream(new File("Sounds/woosh.wav").getAbsoluteFile());
            Clip moveClip = AudioSystem.getClip();
            moveClip.open(moveStream);
            moveClip.start();
        }
        catch (Exception e){
            System.out.println("Problem getting sound file");
        };

    }
}
