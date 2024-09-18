import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;


class Game extends JPanel implements Runnable {//implements runnable allows the use of a thread

    public Thread t; //allows this class to have a method for it to run like the main method
    private float xWorld; //players x position
    private float yWorld; //players y position

    public static boolean notHome; // checks if the player is stil playing the game
    public static boolean paused = false;

    public float screenX = StartScreen.SCREEN_LENGTH / 2; //where to draw the players on the screen
    public float screenY = StartScreen.SCREEN_WIDTH / 2;
    private Keys k = new Keys();
    private Player p = new Player(k);

    private Manager tm = new Manager(8, p);

    public Game() {

        p.addTileManager(tm);
        notHome = true;
        t = new Thread(this);

        this.setLayout(null);
        this.setDoubleBuffered(true); //allows for the computer to draw the next image while the current image is being displayed
        this.addKeyListener(k); //listens for key inputs

    }

    public void startThread() {
        t.start();
    }

    public static void setNotHome(boolean notHome) {
        Game.notHome = notHome;
    }

    @Override
    public void run() {
        long drawInterval = 1000 / 70;
        while (notHome) {
            if (paused == false) {
                this.setFocusable(true);
                this.requestFocus();
            }else{
                this.setFocusable(false);
            }

            update();
            repaint();
            try {
                t.sleep(drawInterval);//pauses the thread for 1/70 of a second, (might be bad)
            } catch (InterruptedException ex) {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex); // IDE Generated
            }
        }
    }

    public void update() {
        p.update();
        xWorld = p.getxPos();
        yWorld = p.getyPos();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        //checks if the player is near the edges of the map or not if the player is near the edge the position on the screen changes and the map stops moving
        if (tm.MAPNUM != 4) {

            if ((p.xPos > 0) && (p.xPos < 2521 - StartScreen.SCREEN_LENGTH - 36)) {
                p.setxBounds(true);
            } else {
                p.setxBounds(false);
            }
            if ((p.yPos > 0) && (p.yPos < 1083 - StartScreen.SCREEN_WIDTH - 24)) {
                p.setyBounds(true);
            } else {
                p.setyBounds(false);
            }

            tm.updateTiles(g2);//draws the background and npcs

            //if the player is not on the edge of the screen the player is drawn in the middle of the screen, if the player is on the edges of the screen
            //then the players screen position will change with its x position
            if (p.isxBounds() == true) {
                if (p.isyBounds()) {
                    g2.drawImage(p.getSprite1(), StartScreen.SCREEN_LENGTH / 2, StartScreen.SCREEN_WIDTH / 2, 120, 120, null);
                } else if (p.isyBounds() == false) {
                    g2.drawImage(p.getSprite1(), StartScreen.SCREEN_LENGTH / 2, (int) p.getScreenY(), 120, 120, null);
                }
            } else {
                if (p.isyBounds()) {
                    g2.drawImage(p.getSprite1(), (int) p.getScreenX(), StartScreen.SCREEN_WIDTH / 2, 120, 120, null);
                } else if (p.isyBounds() == false) {
                    g2.drawImage(p.getSprite1(), (int) p.getScreenX(), (int) p.getScreenY(), 120, 120, null);
                }
            }

        } else {

            if ((p.xPos > 0) && (p.xPos < 48 * 50 - StartScreen.SCREEN_LENGTH - 36)) {
                p.setxBounds(true);
            } else {
                p.setxBounds(false);
            }
            if ((p.yPos > 0) && (p.yPos < 48 * 50 - StartScreen.SCREEN_WIDTH - 24)) {
                p.setyBounds(true);
            } else {
                p.setyBounds(false);
            }

            tm.updateTiles(g2);//draws the background and npcs

            //if the player is not on the edge of the screen the player is drawn in the middle of the screen, if the player is on the edges of the screen
            //then the players screen position will change with its x position
            if (p.isxBounds() == true) {
                if (p.isyBounds()) {
                    g2.drawImage(p.getSprite1(), StartScreen.SCREEN_LENGTH / 2, StartScreen.SCREEN_WIDTH / 2, 120, 120, null);
                } else if (p.isyBounds() == false) {
                    g2.drawImage(p.getSprite1(), StartScreen.SCREEN_LENGTH / 2, (int) p.getScreenY(), 120, 120, null);
                }
            } else {
                if (p.isyBounds()) {
                    g2.drawImage(p.getSprite1(), (int) p.getScreenX(), StartScreen.SCREEN_WIDTH / 2, 120, 120, null);
                } else if (p.isyBounds() == false) {
                    g2.drawImage(p.getSprite1(), (int) p.getScreenX(), (int) p.getScreenY(), 120, 120, null);
                }
            }
        }
        if (p.getObj()[0] != null) {
            g2.drawImage(p.getObj()[0].getBi(), StartScreen.SCREEN_LENGTH, 0, 48, 48, null);
        }

        g2.drawString("X: " + xWorld + " Y: " + yWorld, 25, 25);
        g2.dispose();//garbage collection

    }

}