import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*; 

class EndScreen extends JPanel {
 
    private JLabel endText = new JLabel();
    private BufferedImage img;
    public static Music win = new Music(MonsterHunt.WORK_DIR + "\\Music\\win.wav");

    public EndScreen() {
        try {
            FileInputStream fis = new FileInputStream(MonsterHunt.WORK_DIR + "\\PlayerSprites\\adventureJump.png");
            img = ImageIO.read(fis);
        } catch (IOException e) {
            System.err.println(e);
        }
        this.setBackground(Color.GRAY);
        endText.setText("Well done!! You found the monster and got the $1,000,000!!!");
        endText.setForeground(Color.WHITE);
        this.add(endText);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(img,200, 100,400,400,null);
    }
}