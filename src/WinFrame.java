import javax.swing.*;
import java.awt.*;
import java.util.jar.JarFile;

public class WinFrame extends JFrame {
    private JPanel popup;
    public WinFrame(){
        setBounds(250,200,200,200);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        setResizable(false);
        setFocusable(true);
        requestFocus();
        popup = new JPanel(new BorderLayout());
        JLabel text = new JLabel("YOU WIN");
        text.setForeground(Color.GREEN);
        text.setFont(new Font(Font.MONOSPACED,Font.BOLD,40));
        popup.add(text,BorderLayout.CENTER);
        add(popup);
    }
}
