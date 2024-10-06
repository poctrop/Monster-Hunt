
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

class Monster extends Entities {

    public static BufferedImage back, foward;

    private int playerSpeed;
    private int xAcc;
    private int yAcc;
    private int xFoward = 0;
    private int yFoward = 0;

    public int timer = 0;
    public int timeLeft = 70 * 60;

    private boolean messageShown = false;
    private Player p;

    public Monster(Player inP) {
        readMonster();
        p = inP; 
        playerSpeed = 6;
        xPos = (int) (Math.random() * 1200);
        yPos = (int) (Math.random() * 1200);
        xAcc = 0;
        yAcc = 0;
    }

    public void readMonster() {
        try {
            FileInputStream backStream = new FileInputStream(
                    MonsterHunt.WORK_DIR + "\\MonsterSprites\\MonsterBack.png");
            FileInputStream fdStream = new FileInputStream(
                    MonsterHunt.WORK_DIR + "\\MonsterSprites\\MonsterFoward.png");
            back = ImageIO.read(backStream);
            foward = ImageIO.read(fdStream);
        } catch (IOException e) {
            System.err.println(e + "Monster");
        }
    }

    public void update() {
        if (timeLeft <= 0) {
            p.getK().setKeys();
            if (messageShown == false) {
                messageShown = true;
                JOptionPane.showMessageDialog(null, "You were unable to catch the monster. :(");
                int playAgain;
                try {
                    playAgain = Integer.parseInt(JOptionPane.showInputDialog("Do you want to play again? Enter 1 for yes"));
                } catch (Exception e) {
                    playAgain = 0;
                }
                timer = 0;
                timeLeft = 70 * 60;
                messageShown = false;
                if (playAgain != 1) {
                    p.setScreenX(StartScreen.SCREEN_LENGTH/2);
                    p.setScreenY(StartScreen.SCREEN_HEIGHT/2);
                    p.setXPos(1000);
                    p.setYPos(400);
                    Vechile.carSound.stop();
                    Player.sound.loop();
                    Manager.MAPNUM = 3;
                }
            }
        } else {
            timeLeft--;
            if (xAcc == 0 && yAcc == 0) {
                xFoward = (int) (Math.random() * 500) - (int) (Math.random() * 500);
                yFoward = (int) (Math.random() * 500) - (int) (Math.random() * 500);
            }
            if (yFoward <= 0) {
                sprite = foward;
            } else {
                sprite = back;
            }
            xAcc += playerSpeed;
            yAcc += playerSpeed;
            if (Math.abs(xAcc) < Math.abs(xFoward) && Math.abs(yAcc) < Math.abs(yFoward)) {
                if (yPos > -StartScreen.SCREEN_HEIGHT / 2 && yPos < 48 * 50 - StartScreen.SCREEN_HEIGHT / 2 - 100) {
                    if (xPos > -StartScreen.SCREEN_LENGTH / 2 && xPos < 48 * 50 - StartScreen.SCREEN_LENGTH / 2 - 100) {
                        xPos += playerSpeed * xFoward / Math.abs(xFoward);
                        yPos += playerSpeed * yFoward / Math.abs(yFoward);
                    } else {
                        xPos = 1200;
                    }
                } else {
                    yPos = 500;
                }
            } else {
                yAcc = 0;
                xAcc = 0;
            }
            if (p.xPos > xPos - 150 && p.xPos < xPos + 150) {

                if (p.yPos > yPos - 100 && p.yPos < yPos + 100) {
                    timer++;
                }
            }
            if (timer > 500) {
                Vechile.carSound.stop();
                EndScreen.win.play();
                MonsterHunt.cl.show(MonsterHunt.control, "4");
            }
        }
    }

    //////////////////////// Getters////////////////////////////

    public int getTimer() {
        return timer;
    }

    public int getRemTime(){
        return timeLeft;
    }
}
