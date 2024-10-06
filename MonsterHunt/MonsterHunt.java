import java.awt.CardLayout;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MonsterHunt {

    public static JFrame window = new JFrame(); // acts as the window (all panels are shown from here) 
    public static CardLayout cl = new CardLayout(); //managers each of the panels of the game
    public static JPanel control = new JPanel(); // A panel conataining all other panels (the card layout class can switch between them) 
    public static String WORK_DIR = System.getProperty("user.dir");//The working directory of the user 
    public static Music m;

    public MonsterHunt(JPanel p, JFrame window) {
        System.out.println(WORK_DIR);
        control.setPreferredSize(new Dimension(768, 576));
        window.add(control);
        window.pack();
        window.setFocusable(false);
        m = new Music(WORK_DIR + "\\Music\\gameMusic.wav");
    }


    public static void main(String[] args) {
		StartScreen st = new StartScreen(); //The first screen you will see, gives you the option to either start or load a new game		
		MonsterHunt mn = new MonsterHunt(st, window); //This claas (Used to pass this clas to other classes)
				
        ImageIcon icon = new ImageIcon(WORK_DIR + "\\icon.png"); //icon for the game
        window.setIconImage(icon.getImage());
        window.setResizable(false); 
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setTitle("Monster Hunt");

        PauseScreen ps = new PauseScreen(); // the screen that shows up while paused
        EndScreen es = new EndScreen();

        st.addMH(mn);

        control.setLayout(cl); // allows this pannel to switch between other pannels
        control.add(st, "1");
        control.add(ps, "3");
        control.add(es, "4");

        window.setLocationRelativeTo(null);

        window.setFocusable(false);
        window.setVisible(true);

        m.loop();

    }

}
