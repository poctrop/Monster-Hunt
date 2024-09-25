import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;

class Manager {// class tomanage drawing tiles and npcs

    private Tiles[] tilesSpawn;

    // Stores the objects that are held in each map with an object
    private ArrayList<Object> objFor = new ArrayList<>();
    private ArrayList<Object> objSpawn = new ArrayList<>();

    // Stores the number of objects in each map
    private int numObjS;
    private int numObjF;

    // Stores the background images from each map
    private BufferedImage back1, back2, back3;

    // Stores the layout of the tile system/ Driving inteerface
    private int[][] spawn;
    // stores the number of columns and rows of tiles
    private int numLines = 0;
    private int numCol;

    // Store the players, npc and vechile
    private Player p;
    private NPC richGuy, cameraWoman;
    private Vechile truck;

    // used to calculate here to draw the player from when wither the x, y or both
    // co-ordinates are near the edg
    private float lastXPos = 0;
    private float lastYPos = 0;

    // USed by the tile to draw each ile 1 tile away fromeac other
    private int xCol;
    private int yCol = 0;

    // Class which hndles the implemntation of the stor of the game
    private Story s;

    // Stores the current map number
    public static int MAPNUM = 1;

    public Manager(int n, Player inP) {
        p = inP;
        // p.addTileManager(this);
        MAPNUM = 1;
        numObjF = 0;
        numObjS = 0;
        try {
            // reads in each background and stosthem as Buffered Images
            FileInputStream back1Url = new FileInputStream(MonsterHunt.WORK_DIR + "/Tiles/HomeBack.png");
            back1 = ImageIO.read(back1Url);
            FileInputStream back2Url = new FileInputStream(MonsterHunt.WORK_DIR + "/Tiles/screen2.png");
            back2 = ImageIO.read(back2Url);
            FileInputStream back3Url = new FileInputStream(MonsterHunt.WORK_DIR + "/Tiles/forest.png");
            back3 = ImageIO.read(back3Url);

            // reads in the npc files and stores the sprites
            var sprite1 = new FileInputStream(MonsterHunt.WORK_DIR + "/NPCSprites/RichGuy.png");
            var img = ImageIO.read(sprite1);
            richGuy = new NPC(img, 1500, 630, p, 2, this);
            var sprite2 = new FileInputStream(MonsterHunt.WORK_DIR + "/NPCSprites/CameraWomanR.png");
            var img2 = ImageIO.read(sprite2);
            cameraWoman = new NPC(img2, 1000, 630, p, 1, this);

            // reads in the files for th vechile
            var sprite3 = new FileInputStream(MonsterHunt.WORK_DIR + "/NPCSprites/MonsterTruck.png");
            var img3 = ImageIO.read(sprite3);
            truck = new Vechile(img3, 1000, 630, p, 3, this);

            // garbage
            back1Url.close();
            back2Url.close();
            back3Url.close();

            // stores all the npcs in an arraylist, which is then used by the Story class
            ArrayList<NPC> npcs = new ArrayList<NPC>();
            npcs.add(richGuy);
            npcs.add(cameraWoman);
            s = new Story(npcs, p);

        } catch (IOException e) {
            System.out.println(e);
        }
        spawn = new int[1000][1000];// Stores the map layout of the first map (Strored in a text file as numbers)

        int[] numOccSpawn = new int[50]; // Stores ow many times each tile has occured
        int numTilesSpawn = 0;// Sores the number of tiles there are in the map
        try {

            Scanner scFile = new Scanner(new File(MonsterHunt.WORK_DIR + "/Tiles/spawn/map1.txt")); // File which themap
                                                                                                    // layout when the
                                                                                                    // player is driving
            Scanner scObjSpawn = new Scanner(new File(MonsterHunt.WORK_DIR + "/Objects/foundObjects.txt")); // File
                                                                                                            // which has
                                                                                                            // each of
                                                                                                            // thee
                                                                                                            // objets in
                                                                                                            // the game
                                                                                                            // and also
                                                                                                            // if those
                                                                                                            // objects
                                                                                                            // hav been
                                                                                                            // picked up
                                                                                                            // or not
            Scanner locSpawn = new Scanner(new File(MonsterHunt.WORK_DIR + "/Objects/objLoc.txt")).useDelimiter(","); // File
                                                                                                                      // which
                                                                                                                      // has
                                                                                                                      // the
                                                                                                                      // location
                                                                                                                      // of
                                                                                                                      // each
                                                                                                                      // of
                                                                                                                      // these
                                                                                                                      // objecs

            int locX = 0;
            int locY = 0;
            while (scObjSpawn.hasNext()) {// loop hich runs until the file with the first maps objects has no lines
                Scanner scLineS = new Scanner(scObjSpawn.nextLine()).useDelimiter(",");
                String objNameS = scLineS.next();
                int inInventoryS = Integer.parseInt(scLineS.next());// an integer is stres which can be treated as a
                                                                    // booeann, if the integer is = 1 then the item has
                                                                    // been picked up by the player
                if (inInventoryS == 0) {
                    locX = locSpawn.nextInt();
                    locY = locSpawn.nextInt();
                    objSpawn.add(new Object(objNameS, locX, locY, 1, p));// beacuse it is not in the players inventory,
                                                                         // it will need the x and y pos it must be
                                                                         // drawn at;
                } else {
                    objSpawn.add(new Object(objNameS, p)); // it wil be in the players inventory and therefore will not
                                                           // need an x position associated
                }

                numObjS++;// if there is another line the we know that ther eis another bject
                scLineS.close();
            }

            // adds the tiles from the txt files by reading the line by line and associating
            // each with a different numbr
            String[] scDrive = {};
            while (scFile.hasNextLine()) { // loops until there are no more new lines
                scDrive = (scFile.nextLine()).split(" ");
                for (int i = 0; i < scDrive.length; i++) { // loops the number of columns
                    spawn[i][numLines] = Integer.parseInt(scDrive[i]);
                    if (numOccSpawn[spawn[i][numLines]] == 0) { // used to check if the tile is a new tile or one that
                                                                // has already been added
                        numTilesSpawn++;
                        numOccSpawn[spawn[i][numLines]]++;
                    }
                }
                numLines++;
            }

            // Garbage

            scFile.close();
            scObjSpawn.close();
            locSpawn.close();
            numCol = scDrive.length;

        } catch (IOException e) {
            System.out.println(e);
        }

        tilesSpawn = new Tiles[numTilesSpawn];
        for (int i = 0; i < numTilesSpawn; i++) {
            tilesSpawn[i] = new Tiles(i, "Spawn");
        }

    }

    /////////////////// Getters///////////////////////////////////////////////

    public int[][] getMapLayout() {
        return spawn;
    }

    public int getMapNum() {
        return MAPNUM;
    }

    public int[][] getSpawn() {
        return spawn;
    }

    public Tiles[] getTiles() {
        return tilesSpawn;
    }

    public int getNumLines() {
        return numLines;
    }

    public int getNumCol() {
        return numCol;
    }

    //////////////////// End of getters/////////////////////////////////////////////

    //////////////////// Setters//////////////////////////////////////////////////////

    public void setLastXPos(float lastXPos) {
        this.lastXPos = lastXPos;
    }

    public void setMapNum(int mapNum) {
        this.MAPNUM = mapNum;
    }

    public void setLastYPos(float lastYPos) {
        this.lastYPos = lastYPos;
    }

    /////////////// end of setter/////////////////////////////////////////////

    public void drawObj(Graphics2D g, int n, float x, float y) {// draws all the objects in the area
        g.setColor(Color.WHITE);
        for (int i = 0; i < numObjS; i++) {
            if (objSpawn.get(i).pickUp()) {
                objSpawn.remove(i);
                numObjS--;
                break;
            }
            if (objSpawn.get(i).getMapNum() == n) {
                g.drawImage(objSpawn.get(i).getBi(),
                        (int) (objSpawn.get(i).getX() - x) + StartScreen.SCREEN_LENGTH / 2 + 64,
                        (int) (objSpawn.get(i).getY() - y) + StartScreen.SCREEN_WIDTH / 2 + 100, 48, 48, null);
            }

        }
    }

    public void drawChat(Graphics2D g) {
        if (richGuy.interCheck() == true) {
            if (s.task1()) {
                richGuy.chat("Thanks for the help.");
            } else {
                richGuy.chat();
                g.setColor(Color.white);
                g.fillOval((int) (richGuy.xPos - p.xPos) + StartScreen.SCREEN_LENGTH / 2 - 50,
                        (int) (richGuy.yPos - p.yPos) + StartScreen.SCREEN_WIDTH / 2 - 50, 250, 150);
                g.setColor(Color.BLACK);
                g.drawString(richGuy.getChat(), (int) (richGuy.xPos - p.xPos) + StartScreen.SCREEN_LENGTH / 2 - 30,
                        (int) (richGuy.yPos - p.yPos) + StartScreen.SCREEN_WIDTH / 2 - 30);
            }
        }
        if (cameraWoman.interCheck() == true) {
            cameraWoman.chat();
            g.setColor(Color.white);
            // g.fillOval((int) (cameraWoman.xPos - p.xPos) + StartScreen.SCREEN_LENGTH / 2 - 50,
            //         (int) (cameraWoman.yPos - p.yPos) + StartScreen.SCREEN_WIDTH / 2 - 50, 250, 150);
            g.setColor(Color.BLACK);
            g.drawString(cameraWoman.getChat(), (int) (cameraWoman.xPos - p.xPos) + StartScreen.SCREEN_LENGTH / 2 - 30,
                    (int) (cameraWoman.yPos - p.yPos) + StartScreen.SCREEN_WIDTH / 2 - 30);
        }
    }

    public void updateTiles(Graphics2D g) {
        truck.enterCheck();
        s.task1();
        if (MAPNUM == 1) {
            if (p.isxBounds()) {
                if (p.isyBounds()) {
                    lastXPos = p.xPos;
                    lastYPos = p.yPos;
                    g.drawImage(back1, (int) (-p.xPos), (int) (-p.yPos), null);
                    g.drawImage(cameraWoman.getSprite1(),
                            (int) (cameraWoman.xPos - p.xPos) + StartScreen.SCREEN_LENGTH / 2,
                            (int) (cameraWoman.yPos - p.yPos) + StartScreen.SCREEN_WIDTH / 2 - 24, 120, 120, null);
                    drawObj(g, MAPNUM, p.xPos, p.yPos);
                } else {
                    lastXPos = p.xPos;
                    g.drawImage(back1, (int) (-p.xPos), (int) (-lastYPos), null);
                    g.drawImage(cameraWoman.getSprite1(),
                            (int) (cameraWoman.xPos - p.xPos) + StartScreen.SCREEN_LENGTH / 2,
                            (int) (cameraWoman.yPos - lastYPos) + StartScreen.SCREEN_WIDTH / 2 - 24, 120, 120, null);
                    drawObj(g, MAPNUM, p.xPos, lastYPos);
                    drawChat(g);
                }

            } else if (p.isyBounds()) {
                lastYPos = p.yPos;
                g.drawImage(back1, (int) (-lastXPos), (int) (-p.yPos), null);
                g.drawImage(cameraWoman.getSprite1(),
                        (int) (cameraWoman.xPos - lastXPos) + StartScreen.SCREEN_LENGTH / 2,
                        (int) (cameraWoman.yPos - p.yPos) + StartScreen.SCREEN_WIDTH / 2 - 24, 120, 120, null);
                drawObj(g, MAPNUM, lastXPos, p.yPos);
                drawChat(g);
            } else {
                g.drawImage(back1, (int) (-lastXPos), (int) (-lastYPos), null);
                g.drawImage(cameraWoman.getSprite1(),
                        (int) (cameraWoman.xPos - lastXPos) + StartScreen.SCREEN_LENGTH / 2,
                        (int) (cameraWoman.yPos - lastYPos) + StartScreen.SCREEN_WIDTH / 2 - 24, 120, 120, null);
                drawObj(g, MAPNUM, lastXPos, lastYPos);
                drawChat(g);

            }
        } else if (MAPNUM == 2) {
            if (p.isxBounds()) {
                if (p.isyBounds()) {
                    lastXPos = p.xPos;
                    lastYPos = p.yPos;
                    g.drawImage(back2, (int) (-p.xPos), (int) (-p.yPos), null);
                    g.drawImage(richGuy.getSprite1(), (int) (richGuy.xPos - p.xPos) + StartScreen.SCREEN_LENGTH / 2,
                            (int) (richGuy.yPos - p.yPos) + StartScreen.SCREEN_WIDTH / 2 - 24, 120, 120, null);
                    drawChat(g);
                } else {
                    lastXPos = p.xPos;
                    g.drawImage(back2, (int) (-p.xPos), (int) (-lastYPos), null);
                    g.drawImage(richGuy.getSprite1(), (int) (richGuy.xPos - p.xPos) + StartScreen.SCREEN_LENGTH / 2,
                            (int) (richGuy.yPos - lastYPos) + StartScreen.SCREEN_WIDTH / 2 - 24, 120, 120, null);
                    drawChat(g);
                }

            } else if (p.isyBounds()) {
                lastYPos = p.yPos;
                g.drawImage(back2, (int) (-lastXPos), (int) (-p.yPos), null);
                g.drawImage(richGuy.getSprite1(), (int) (richGuy.xPos - lastXPos) + StartScreen.SCREEN_LENGTH / 2,
                        (int) (richGuy.yPos - p.yPos) + StartScreen.SCREEN_WIDTH / 2 - 24, 120, 120, null);
                drawChat(g);
            } else {
                g.drawImage(back2, (int) (-lastXPos), (int) (-lastYPos), null);
                g.drawImage(richGuy.getSprite1(), (int) (richGuy.xPos - lastXPos) + StartScreen.SCREEN_LENGTH / 2,
                        (int) (richGuy.yPos - lastYPos) + StartScreen.SCREEN_WIDTH / 2 - 24, 120, 120, null);
                drawChat(g);

            }
        } else if (MAPNUM == 3) {
            if (p.isxBounds()) {
                if (p.isyBounds()) {
                    lastXPos = p.xPos;
                    lastYPos = p.yPos;
                    g.drawImage(back3, (int) (-p.xPos), (int) (-p.yPos), null);
                    g.drawImage(truck.getSprite1(), (int) (truck.xPos - p.xPos) + StartScreen.SCREEN_LENGTH / 2,
                            (int) (truck.yPos - p.yPos) + StartScreen.SCREEN_WIDTH / 2 - 150, null);

                } else {
                    lastXPos = p.xPos;
                    g.drawImage(back3, (int) (-p.xPos), (int) (-lastYPos), null);
                    g.drawImage(truck.getSprite1(), (int) (truck.xPos - p.xPos) + StartScreen.SCREEN_LENGTH / 2,
                            (int) (truck.yPos - lastYPos) + StartScreen.SCREEN_WIDTH / 2 - 150, null);
                }

            } else if (p.isyBounds()) {
                lastYPos = p.yPos;
                g.drawImage(back3, (int) (-lastXPos), (int) (-p.yPos), null);
                g.drawImage(truck.getSprite1(), (int) (truck.xPos - lastXPos) + StartScreen.SCREEN_LENGTH / 2,
                        (int) (truck.yPos - p.yPos) + StartScreen.SCREEN_WIDTH / 2 - 150, null);
            } else {
                g.drawImage(back3, (int) (-lastXPos), (int) (-lastYPos), null);
                g.drawImage(truck.getSprite1(), (int) (truck.xPos - lastXPos) + StartScreen.SCREEN_LENGTH / 2,
                        (int) (truck.yPos - lastYPos) + StartScreen.SCREEN_WIDTH / 2 - 150, null);

            }
        } else if (MAPNUM == 4) {
            if (p.isxBounds()) {
                if (p.isyBounds()) {
                    lastXPos = p.xPos;
                    lastYPos = p.yPos;
                    for (int j = 0; j < numLines; j++) {
                        xCol = 0;
                        for (int i = 0; i < numCol; i++) {
                            g.drawImage(tilesSpawn[(spawn[i][j])].getTile(), (int) (xCol - p.xPos),
                                    (int) (yCol - p.yPos), null);
                            xCol += 48;
                        }
                        yCol += 48;

                    }

                } else {
                    lastXPos = p.xPos;
                    for (int j = 0; j < numLines; j++) {
                        xCol = 0;
                        for (int i = 0; i < numCol; i++) {
                            g.drawImage(tilesSpawn[(spawn[i][j])].getTile(), (int) (xCol - p.xPos),
                                    (int) (yCol - lastYPos), null);
                            xCol += 48;
                        }
                        yCol += 48;

                    }
                }

            } else if (p.isyBounds()) {
                lastYPos = p.yPos;
                for (int j = 0; j < numLines; j++) {
                    xCol = 0;
                    for (int i = 0; i < numCol; i++) {
                        g.drawImage(tilesSpawn[(spawn[i][j])].getTile(), (int) (xCol - lastXPos), (int) (yCol - p.yPos),
                                null);
                        xCol += 48;
                    }
                    yCol += 48;

                }
            } else {
                for (int j = 0; j < numLines; j++) {
                    xCol = 0;
                    for (int i = 0; i < numCol; i++) {
                        g.drawImage(tilesSpawn[(spawn[i][j])].getTile(), (int) (xCol - lastXPos),
                                (int) (yCol - lastYPos), null);
                        xCol += 48;
                    }
                    yCol += 48;

                }
            }

            yCol = 0;
            g.drawString("Click C to exit", StartScreen.SCREEN_LENGTH / 2, StartScreen.SCREEN_WIDTH);
        }
    }

}