import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class Tile extends JLabel {
    public Tile(String s){
        super(s);
        setBounds(0,0,50,50);
        setBorder(new LineBorder(Color.BLACK));
    }
}
