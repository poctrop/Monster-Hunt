import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import javax.swing.*;

class StartScreen extends JPanel implements ActionListener {

    public final static int SCREEN_LENGTH = 720;
    public final static int SCREEN_WIDTH = 540;
    private MonsterHunt mh;

    public JButton buttonstart = new JButton();
    public JButton buttonLoad = new JButton();

    public StartScreen() {

        buttonstart.setPreferredSize(new Dimension(200, 50));
        buttonstart.setText("Start Game");
        buttonstart.setBounds(SCREEN_LENGTH / 2 - 100, SCREEN_WIDTH / 2, 200, 50);
        buttonstart.setFocusable(false);
        buttonstart.addActionListener(this);

        buttonLoad.setPreferredSize(new Dimension(200, 50));
        buttonLoad.setText("Load Game");
        buttonLoad.setBounds(SCREEN_LENGTH / 2 - 100, SCREEN_WIDTH / 2 + 100, 200, 50);
        buttonLoad.addActionListener(this);

        this.setLayout(null);
        this.setDoubleBuffered(true);
        this.setBackground(Color.black);
        this.add(buttonstart);
        this.add(buttonLoad);

    }

    public void addMH(MonsterHunt inMH) {
        mh = inMH;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonstart) {
            System.out.println("yo");
            this.setFocusable(false);
            NewGame ng = new NewGame(mh);
            mh.cl.show(mh.control, "2");
        }
        if (e.getSource() == buttonLoad) {
            System.out.println("yo");
            this.setFocusable(false);
            LoadGame lg = new LoadGame(mh);
            mh.cl.show(mh.control, "3");
        }

    }

}