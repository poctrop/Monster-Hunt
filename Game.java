import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;


class Game extends JPanel implements Runnable {//implements runnable allows the use of a thread

    private Font f = new Font("Sans Serif", Font.BOLD, 10);
    public Thread t; //allows this class to have a method for it to run like the main method
    private float xWorld; //players x position
    private float yWorld; //players y position

    public static boolean notHome; // checks if the player is stil playing the game
    public static boolean paused = false;
 
    public float screenX = StartScreen.SCREEN_LENGTH / 2; //where to draw the players on the screen
    public float screenY = StartScreen.SCREEN_WIDTH / 2;
    private Keys k = new Keys(); // Checks for key inputs
    private Player p = new Player(k); // The class that hndls everything that the player does

    private Manager tm = new Manager(8, p); //This class handles the npcs, vechile. Background and tile drawing system of the game

    public Game() {
        p.addManager(tm); //adds the manager class to the player class which uses the maps from the class
        notHome = true; // checks if the player is on the home screen on not
        t = new Thread(this); //Starts a new thread which rubs simultaniously to the main method thread 

        this.setLayout(null);
        this.setDoubleBuffered(true); //allows for the computer to draw the next image while the current image is being displayed
        this.addKeyListener(k); //listens for key inputs

    }

    public void startThread() {
        t.start(); //starts the rthread which starts th run mehod
    }

    public static void setNotHome(boolean notHome) {
        Game.notHome = notHome; 
    }

    @Override
    public void run() { // handles the game loop of the game, runs his simultaniously with the main mehtod
        long drawInterval = 1000 / 70;//how long in milliseconds the omputer mut wait for the next draw
        while (notHome) {
            if (paused == false) {  //if the player has not paused, the game listens for key inputs if it isnt iust top listening
                this.setFocusable(true);
                this.requestFocus();
            }else{
                this.setFocusable(false);
            }

            update();//edits the x and y position of the plaer
            repaint();//paints the background, objects player and npcs on the screen
            try {
                Thread.sleep(drawInterval);//pauses the thread for 1/70 of a second
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
        g.setFont(f);
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
        if (p.getObj().size() != 0) {
            g2.drawImage(p.getObj().get(0).getBi(), StartScreen.SCREEN_LENGTH, 0, 48, 48, null);
        }

        g2.drawString("X: " + xWorld + " Y: " + yWorld, 25, 25);
        g2.dispose();//garbage collection

    }

}