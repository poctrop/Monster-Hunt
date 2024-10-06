import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;

import javax.imageio.ImageIO;
import javax.swing.*;

class StartScreen extends JPanel implements ActionListener {
 
    public final static int SCREEN_LENGTH = 720; //The length of the screen
    public final static int SCREEN_HEIGHT = 540; //the height of the screen
    private MonsterHunt mh;
    private Game g;
    private boolean started = false;

    public static BufferedImage img;
    private JButton buttonstart = new JButton("Start Game");
    private JButton buttonInstr = new JButton("Instructions");

    public StartScreen() {
        super(); 
        try{
            FileInputStream fis = new FileInputStream(MonsterHunt.WORK_DIR + "\\Tiles\\TitleScreen.png");
            img = ImageIO.read(fis);
        }catch(Exception e){
            System.out.println(e);
        }
        //sets the values of the start button
        buttonstart.setPreferredSize(new Dimension(200, 50));
        buttonstart.setBounds(SCREEN_LENGTH / 2 - 70, SCREEN_HEIGHT / 2 + 40, 200, 50);
        buttonstart.setFocusable(false);
        buttonstart.addActionListener(this);

        buttonInstr.setPreferredSize(new Dimension(200, 50));
        buttonInstr.setBounds(SCREEN_LENGTH / 2 - 70, SCREEN_HEIGHT / 2 + 100, 200, 50);
        buttonInstr.setFocusable(false);
        buttonInstr.addActionListener(this);
        
        //sets the values of the layout
        this.setLayout(null);
        this.setDoubleBuffered(true);
        this.setBackground(Color.black); 
        this.add(buttonInstr);
        this.add(buttonstart);

        repaint();

    }

    public void addMH(MonsterHunt inMH) {
        mh = inMH;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonstart) {//checks if the start button has been pressed
            if (started == false) {
                this.setFocusable(false);
                g = new Game();
                mh.control.add(g, "2");
                g.startThread();
                mh.cl.show(mh.control, "2");
                started = true;
                mh.m.stop();
            } else {
                Game.paused = false;
                this.setFocusable(false);
                mh.cl.show(mh.control, "2");
            }
        }
        if (e.getSource() == buttonInstr) {
            JOptionPane.showMessageDialog(null, "This game is a Role-Playing game, where the player recieves tasks from different NPC's \n\n Controls:\nJump/drive up:    Up arrow \nLeft:    Left Arrow\nRight:    Right arrow\nDrive Down:    Down Arrow\nInteraction:    C\n\nThe game will end once you complete the final task\nGood luck!!");
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.drawImage(img,0,0, 765,575,null);
    }

}