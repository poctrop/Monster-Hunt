import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JPanel;

class PauseScreen extends JPanel {

    public static JButton cont = new JButton();
    public static JButton home = new JButton();

    private Action a = new Action();

    public PauseScreen() {

        cont.setPreferredSize(new Dimension(200, 50));
        cont.setText("Back to Game");
        cont.setBounds(StartScreen.SCREEN_LENGTH / 2 - 100, StartScreen.SCREEN_WIDTH / 2, 200, 50);
        cont.addActionListener(a);

        home.setPreferredSize(new Dimension(200, 50));
        home.setText("Home Screen");
        home.setBounds(StartScreen.SCREEN_LENGTH / 2 - 100, StartScreen.SCREEN_WIDTH / 2 + 100, 200, 50);
        home.addActionListener(a);

        this.add(home);
        this.add(cont);
        this.setLayout(null);
        this.setPreferredSize(new Dimension(568, 376));
        this.setLocation(100, 100);
        this.setOpaque(false);

    }
    

}