import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

class NewGame extends JPanel implements ActionListener {

    public JButton save1 = new JButton();
    public JButton save2 = new JButton();
    public JButton back = new JButton();
    private boolean started = false;
 
    private Game g;

    private MonsterHunt mh;

    public NewGame(MonsterHunt inMH) {
        mh = inMH;
        save1.setText("Save 1");
        save1.setBounds(StartScreen.SCREEN_LENGTH / 2 - 270, 50, 550, 200);
        save1.addActionListener(this);

        save2.setText("Save 2");
        save2.setBounds(StartScreen.SCREEN_LENGTH / 2 - 270, StartScreen.SCREEN_WIDTH / 2, 550, 200);
        save2.addActionListener(this);

        back.setText("back");
        back.setBounds(0, 0, 70, 25);
        back.addActionListener(this);
 
        this.setLayout(null);
        this.setDoubleBuffered(true);
        this.setBackground(Color.green);
        this.add(save1);
        this.add(save2);
        this.add(back);
    }

    public void addMH(MonsterHunt inMH) {
        mh = inMH;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == save1) {
            if (started == false) {
                this.setFocusable(false);
                g = new Game();
                mh.control.add(g, "4");
                g.startThread();
                mh.cl.show(mh.control, "4");
                started = true;
            } else {
                Game.paused = false;
                this.setFocusable(false);
                mh.cl.show(mh.control, "4");
            }
        }
        if (e.getSource() == save2) {
            this.setFocusable(false);
            mh.cl.show(mh.control, "4");
        }
        if (e.getSource() == back) {
            this.setFocusable(false);
            mh.cl.show(mh.control, "1");
        }
    }

}