import java.awt.CardLayout;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MonsterHunt {

    public static JFrame window = new JFrame(); // acts as the window (all panels are shown from here) 
    public static CardLayout cl = new CardLayout(); //managers each of the panels of the game
    public static JPanel control = new JPanel(); // A panel conataining all other panels (the card layout class can switch between them) 
    public static String WORK_DIR;

    public MonsterHunt(JPanel p, JFrame window) {
        WORK_DIR = System.getProperty("user.dir");
        control.setPreferredSize(new Dimension(768, 576));
        window.add(control);
        window.pack();
        window.setFocusable(false);
    }


    public static void main(String[] args) {
		StartScreen st = new StartScreen(); //The first screen you will see, gives you the option to either start or load a new game		
		MonsterHunt mn = new MonsterHunt(st, window); //This claas (Used to pass this clas to other classes)
				
        ImageIcon icon = new ImageIcon(WORK_DIR + "\\icon.png"); //icon for the game
        window.setIconImage(icon.getImage());
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setTitle("Monster Hunt");

        NewGame ng = new NewGame(mn); //Screen if new game option is chosen
        LoadGame lg = new LoadGame(mn); //Screen if load game option is chosen
        PauseScreen ps = new PauseScreen(); // the screen that shows up while paused

        st.addMH(mn);

        control.setLayout(cl); // allows this pannel to switch between other pannels
        control.add(st, "1");
        control.add(ng, "2");
        control.add(lg, "3");
        control.add(ps, "5");

        window.setLocationRelativeTo(null);

        window.setFocusable(false);
        window.setVisible(true);

    }

}
