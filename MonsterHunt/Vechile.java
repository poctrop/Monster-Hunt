import java.awt.image.BufferedImage;

import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.Graphics2D;

class Vechile extends NPC {

    private boolean firstMessage = true; //A variable which checks if message which tells the player the object of the driving part of the game has already been shown
    public static Music carSound;

    public Vechile(BufferedImage img, int x, int y, Player p, int mapNum, Manager tnpm) { 
        super(img, x, y, p, mapNum, tnpm); 
        try {
            carSound = new Music(MonsterHunt.WORK_DIR + "\\Music\\carSound.wav");
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void enterCheck(Graphics2D g) {
        if (xPos > p.xPos - 150 && xPos < p.xPos + 150 && mapNum == tpm.getMapNum()) { //checks if the player is in a cerarting range from the truck
            if (p.getK().isInteract() && p.findObject("carKeys") != null) { //checks if the player has car keys, if they dont they cant enter the car
                //checkls if the message has already been show
                p.getK().setKeys();
                if (firstMessage) {
                    firstMessage = false;
                    JOptionPane.showMessageDialog(null,
                            "Find the monster and stay around it until the timer reaches 500");      
                }
                Player.sound.stop();
                carSound.loop();
                tpm.setMapNum(4);
                p.sprite = p.driveUp1;
                p.getK().setInteract(false);
            //if the player does not have car keys yet they must see the message
            } else if (p.getK().isInteract() && p.findObject("carKeys") == null) {
                g.setColor(Color.black);
                g.drawString("You need car keys to enter the car!", StartScreen.SCREEN_LENGTH / 2,
                        StartScreen.SCREEN_HEIGHT / 2);
            }
        }
    } 
}
