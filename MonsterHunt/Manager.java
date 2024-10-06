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
    private BufferedImage back1, back2, back3, back5;

    // Stores the layout of the tile system/ Driving inteerface
    private int[][] spawn;
    // stores the number of columns and rows of tiles 
    private int numLines = 0;
    private int numCol;

    // Store the players, npc, Monster and vechile
    private Player p;
    private NPC richGuy, cameraWoman, grannyNpc, sonNpc, momNpc;
    private Vechile truck;
    private Monster m;

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
        m = new Monster(p);

        spawn = new int[1000][1000];// Stores the map layout of the first map (Strored in a text file as numbers)

        instNPC();
        inst();

    }

    public void inst() {
        try {
            int[] numOccSpawn = new int[50]; // Stores ow many times each tile has occured
            int numTilesSpawn = 0;// Sores the number of tiles there are in the map

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
            while (scObjSpawn.hasNext()) {// loop which runs until the file with the first maps objects has no lines
                Scanner scLineS = new Scanner(scObjSpawn.nextLine()).useDelimiter(",");
                String loc = scLineS.next();
                String objNameS = scLineS.next();
                int inInventoryS = Integer.parseInt(scLineS.next());// an integer is stored which can be treated as a
                                                                    // boolean, if the integer is = 1 then the item has
                                                                    // been picked up by the player
                if (inInventoryS == 0) {
                    Scanner coOrd = new Scanner(locSpawn.nextLine()).useDelimiter(",");
                    locX = coOrd.nextInt();
                    locY = coOrd.nextInt();
                    if (loc.equalsIgnoreCase("s")) {
                        objSpawn.add(new Object(objNameS, locX, locY, 1, p));// beacuse it is not in the players
                        numObjS++; // it will need the x and y pos it must be
                        // drawn at; // inventory,
                    } else {
                        objFor.add(new Object(objNameS, locX, locY, 3, p));
                        numObjF++;
                    }
                    coOrd.close();
                } else {
                    int pl = scLineS.nextInt();
                    if (pl == 0) {
                        p.addObject(new Object(objNameS, p));
                    } else if (pl == 1) {
                        cameraWoman.addObject(new Object(objNameS, cameraWoman)); // it wil be in the players inventory
                                                                                  // and therefore will
                        // not
                        // need an x position associated
                    } else if (pl == 5) {
                        grannyNpc.addObject(new Object(objNameS, grannyNpc));
                    }
                }
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

            tilesSpawn = new Tiles[numTilesSpawn];
            for (int i = 0; i < numTilesSpawn; i++) {
                tilesSpawn[i] = new Tiles(i, "Spawn");
            }

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void instNPC() {
        try {
            // reads in each background and stosthem as Buffered Images
            FileInputStream back1Url = new FileInputStream(MonsterHunt.WORK_DIR + "/Tiles/HomeBack.png");
            back1 = ImageIO.read(back1Url);
            FileInputStream back2Url = new FileInputStream(MonsterHunt.WORK_DIR + "/Tiles/screen2.png");
            back2 = ImageIO.read(back2Url);
            FileInputStream back3Url = new FileInputStream(MonsterHunt.WORK_DIR + "/Tiles/forest.png");
            back3 = ImageIO.read(back3Url);
            FileInputStream back5Url = new FileInputStream(MonsterHunt.WORK_DIR + "/Tiles/Bedroom.png");
            back5 = ImageIO.read(back5Url);

            // reads in the npc files and stores the sprites
            FileInputStream sprite1 = new FileInputStream(MonsterHunt.WORK_DIR + "/NPCSprites/RichGuy.png");
            BufferedImage img = ImageIO.read(sprite1);
            richGuy = new NPC(img, 1500, 630, p, 1, this);
            FileInputStream sprite2 = new FileInputStream(MonsterHunt.WORK_DIR + "/NPCSprites/CameraWomanR.png");
            BufferedImage img2 = ImageIO.read(sprite2);
            cameraWoman = new NPC(img2, 1000, 630, p, 2, this);
            FileInputStream sprite4 = new FileInputStream(MonsterHunt.WORK_DIR + "/NPCSprites/son.png");
            BufferedImage img4 = ImageIO.read(sprite4);
            sonNpc = new NPC(img4, 742, 630, p, 5, this);
            FileInputStream sprite5 = new FileInputStream(MonsterHunt.WORK_DIR + "/NPCSprites/mom.png");
            BufferedImage img5 = ImageIO.read(sprite5);
            momNpc = new NPC(img5, 1554, 630, p, 5, this);
            FileInputStream sprite6 = new FileInputStream(MonsterHunt.WORK_DIR + "/NPCSprites/granny.png");
            BufferedImage img6 = ImageIO.read(sprite6);
            grannyNpc = new NPC(img6, 770, 260, p, 5, this);

            // reads in the files for the vechile
            FileInputStream sprite3 = new FileInputStream(MonsterHunt.WORK_DIR + "/NPCSprites/MonsterTruck.png");
            BufferedImage img3 = ImageIO.read(sprite3);
            truck = new Vechile(img3, 1000, 630, p, 3, this);

            // garbage
            back1Url.close();
            back2Url.close();
            back3Url.close();

            // stores all the npcs in an arraylist, which is then used by the Story class
            ArrayList<NPC> npcs = new ArrayList<NPC>();
            npcs.add(richGuy);
            npcs.add(cameraWoman);
            npcs.add(grannyNpc);
            npcs.add(sonNpc);
            npcs.add(momNpc);

            s = new Story(npcs, p);

        } catch (IOException e) {
            System.out.println(e + "4");
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
        if (MAPNUM == 1) {
            for (int i = 0; i < numObjS; i++) {
                if (objSpawn.get(i).pickUp()) {
                    objSpawn.remove(i);
                    numObjS--;
                    break;
                }
                g.drawImage(objSpawn.get(i).getBi(),
                        (int) (objSpawn.get(i).getX() - x) + StartScreen.SCREEN_LENGTH / 2 + 64,
                        (int) (objSpawn.get(i).getY() - y) + StartScreen.SCREEN_HEIGHT / 2 + 100, 48, 48, null);
            }
        } else if (MAPNUM == 3) {
            for (int i = 0; i < numObjF; i++) {
                if (objFor.get(i).pickUp()) {
                    objFor.remove(i);
                    numObjF--;
                    break;
                }
                g.drawImage(objFor.get(i).getBi(),
                        (int) (objFor.get(i).getX() - x) + StartScreen.SCREEN_LENGTH / 2 + 64,
                        (int) (objFor.get(i).getY() - y) + StartScreen.SCREEN_HEIGHT / 2 + 100, 48, 48, null);
            }
        }
    }

    public static void drawChatNPC(NPC n, Graphics2D g, float xD, float yD) {
        int x = 0;
        yD += 120;
        if (n.interCheck() == true) { // checks if the player is in the vicinity of the npc
            n.chat();
            g.setColor(Color.white);
            g.fillOval((int) (n.xPos - xD) + StartScreen.SCREEN_LENGTH / 2 - 50,
                    (int) (n.yPos - yD) + StartScreen.SCREEN_HEIGHT / 2 - 50, 250, 150); // draws the background/ shape
                                                                                         // of the speech bubble
            g.setColor(Color.BLACK);
            for (String s : n.getChat().split("/")) {
                g.drawString(s, (int) (n.xPos - xD) + StartScreen.SCREEN_LENGTH / 2,
                        (int) (n.yPos - yD) + StartScreen.SCREEN_HEIGHT / 2 + x); // draws the text
                x += 25;
            }
        }
    }

    // Draws the speech bubble
    public void drawChat(Graphics2D g, float xD, float yD) {

        if (richGuy.interCheck() == true) { // checks if the player is in the vicinity of the npc
            drawChatNPC(richGuy, g, xD, yD);
        }
        if (cameraWoman.interCheck() == true) {
            drawChatNPC(cameraWoman, g, xD, yD);
        }
        if (grannyNpc.interCheck() == true) {
            drawChatNPC(grannyNpc, g, xD , yD);
        }
        if (sonNpc.interCheck()) {
            drawChatNPC(sonNpc, g , xD, yD);
        }
        if (momNpc.interCheck()) {
            drawChatNPC(momNpc, g, xD, yD);
        }
    }

    public void updateTiles(Graphics2D g) {
        s.task1();
        s.task2();
        if (MAPNUM == 1) {
            if (p.isxBounds()) {
                if (p.isyBounds()) {
                    lastXPos = p.xPos;
                    lastYPos = p.yPos;
                    g.drawImage(back1, (int) (-p.xPos), (int) (-p.yPos), null);
                    g.drawImage(richGuy.getSprite1(),
                            (int) (richGuy.xPos - p.xPos) + StartScreen.SCREEN_LENGTH / 2,
                            (int) (richGuy.yPos - p.yPos) + StartScreen.SCREEN_HEIGHT / 2 - 24, 120, 120, null);
                    drawObj(g, MAPNUM, p.xPos, p.yPos);
                    drawChat(g, p.xPos, p.yPos);
                } else {
                    lastXPos = p.xPos;
                    g.drawImage(back1, (int) (-p.xPos), (int) (-lastYPos), null);
                    g.drawImage(richGuy.getSprite1(),
                            (int) (richGuy.xPos - p.xPos) + StartScreen.SCREEN_LENGTH / 2,
                            (int) (richGuy.yPos - lastYPos) + StartScreen.SCREEN_HEIGHT / 2 - 24, 120, 120, null);
                    drawObj(g, MAPNUM, p.xPos, lastYPos);
                    drawChat(g, p.xPos, lastYPos);
                }

            } else if (p.isyBounds()) {
                lastYPos = p.yPos;
                g.drawImage(back1, (int) (-lastXPos), (int) (-p.yPos), null);
                g.drawImage(richGuy.getSprite1(),
                        (int) (richGuy.xPos - lastXPos) + StartScreen.SCREEN_LENGTH / 2,
                        (int) (richGuy.yPos - p.yPos) + StartScreen.SCREEN_HEIGHT / 2 - 24, 120, 120, null);
                drawObj(g, MAPNUM, lastXPos, p.yPos);
                drawChat(g, lastXPos, p.yPos);
            } else {
                g.drawImage(back1, (int) (-lastXPos), (int) (-lastYPos), null);
                g.drawImage(richGuy.getSprite1(),
                        (int) (richGuy.xPos - lastXPos) + StartScreen.SCREEN_LENGTH / 2,
                        (int) (richGuy.yPos - lastYPos) + StartScreen.SCREEN_HEIGHT / 2 - 24, 120, 120, null);
                drawObj(g, MAPNUM, lastXPos, lastYPos);
                drawChat(g, lastXPos, lastYPos);

            }
        } else if (MAPNUM == 2) {
            if (p.isxBounds()) {
                if (p.isyBounds()) {
                    lastXPos = p.xPos;
                    lastYPos = p.yPos;
                    g.drawImage(back2, (int) (-p.xPos), (int) (-p.yPos), null);
                    g.drawImage(cameraWoman.getSprite1(),
                            (int) (cameraWoman.xPos - p.xPos) + StartScreen.SCREEN_LENGTH / 2,
                            (int) (cameraWoman.yPos - p.yPos) + StartScreen.SCREEN_HEIGHT / 2 - 24, 120, 120, null);
                    drawChat(g, p.xPos, p.yPos);
                } else {
                    lastXPos = p.xPos;
                    g.drawImage(back2, (int) (-p.xPos), (int) (-lastYPos), null);
                    g.drawImage(cameraWoman.getSprite1(),
                            (int) (cameraWoman.xPos - p.xPos) + StartScreen.SCREEN_LENGTH / 2,
                            (int) (cameraWoman.yPos - lastYPos) + StartScreen.SCREEN_HEIGHT / 2 - 24, 120, 120, null);
                    drawChat(g, p.xPos, lastYPos);
                }

            } else if (p.isyBounds()) {
                lastYPos = p.yPos;
                g.drawImage(back2, (int) (-lastXPos), (int) (-p.yPos), null);
                g.drawImage(cameraWoman.getSprite1(),
                        (int) (cameraWoman.xPos - lastXPos) + StartScreen.SCREEN_LENGTH / 2,
                        (int) (cameraWoman.yPos - p.yPos) + StartScreen.SCREEN_HEIGHT / 2 - 24, 120, 120, null);
                drawChat(g, lastXPos, p.yPos);
            } else {
                g.drawImage(back2, (int) (-lastXPos), (int) (-lastYPos), null);
                g.drawImage(cameraWoman.getSprite1(),
                        (int) (cameraWoman.xPos - lastXPos) + StartScreen.SCREEN_LENGTH / 2,
                        (int) (cameraWoman.yPos - lastYPos) + StartScreen.SCREEN_HEIGHT / 2 - 24, 120, 120, null);
                drawChat(g, lastXPos, lastYPos);

            }
        } else if (MAPNUM == 3) {
            if (p.isxBounds()) {
                if (p.isyBounds()) {
                    lastXPos = p.xPos;
                    lastYPos = p.yPos;
                    g.drawImage(back3, (int) (-p.xPos), (int) (-p.yPos), null);
                    g.drawImage(truck.getSprite1(), (int) (truck.xPos - p.xPos) + StartScreen.SCREEN_LENGTH / 2,
                            (int) (truck.yPos - p.yPos) + StartScreen.SCREEN_HEIGHT / 2 - 150, null);
                    drawObj(g, MAPNUM, p.xPos, p.yPos);

                } else {
                    lastXPos = p.xPos;
                    g.drawImage(back3, (int) (-p.xPos), (int) (-lastYPos), null);
                    g.drawImage(truck.getSprite1(), (int) (truck.xPos - p.xPos) + StartScreen.SCREEN_LENGTH / 2,
                            (int) (truck.yPos - lastYPos) + StartScreen.SCREEN_HEIGHT / 2 - 150, null);
                    drawObj(g, MAPNUM, p.xPos, p.yPos);

                }

            } else if (p.isyBounds()) {
                lastYPos = p.yPos;
                g.drawImage(back3, (int) (-lastXPos), (int) (-p.yPos), null);
                g.drawImage(truck.getSprite1(), (int) (truck.xPos - lastXPos) + StartScreen.SCREEN_LENGTH / 2,
                        (int) (truck.yPos - p.yPos) + StartScreen.SCREEN_HEIGHT / 2 - 150, null);
                drawObj(g, MAPNUM, p.xPos, p.yPos);
            } else {
                g.drawImage(back3, (int) (-lastXPos), (int) (-lastYPos), null);
                g.drawImage(truck.getSprite1(), (int) (truck.xPos - lastXPos) + StartScreen.SCREEN_LENGTH / 2,
                        (int) (truck.yPos - lastYPos) + StartScreen.SCREEN_HEIGHT / 2 - 150, null);
                drawObj(g, MAPNUM, p.xPos, p.yPos);

            }
        } else if (MAPNUM == 4) {
            m.update();
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
                    g.drawImage(m.sprite,(int) (m.xPos - p.xPos) + StartScreen.SCREEN_LENGTH / 2,
                            (int) (m.yPos - p.yPos) + StartScreen.SCREEN_HEIGHT / 2, 150, 150, null);

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
                    g.drawImage(m.sprite,(int) (m.xPos - p.xPos) + StartScreen.SCREEN_LENGTH / 2,
                            (int) (m.yPos - lastYPos) + StartScreen.SCREEN_HEIGHT / 2, 150, 150, null);
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
                g.drawImage(m.sprite,(int) (m.xPos - lastXPos) + StartScreen.SCREEN_LENGTH / 2,
                        (int) (m.yPos - p.yPos) + StartScreen.SCREEN_HEIGHT / 2, 150, 150, null);
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
                g.drawImage(m.sprite, (int) (m.xPos - lastXPos) + StartScreen.SCREEN_LENGTH / 2,
                        (int) (m.yPos - lastXPos) + StartScreen.SCREEN_HEIGHT / 2, 150, 150, null);
            }

            yCol = 0;
            g.drawString("Total points: " + m.getTimer(), StartScreen.SCREEN_LENGTH / 2, 45);
            g.drawString("Time Left: " + m.getRemTime()/70, StartScreen.SCREEN_LENGTH / 2, 90);
            g.drawString("Click C to exit", StartScreen.SCREEN_LENGTH / 2, StartScreen.SCREEN_HEIGHT);
        } else if (MAPNUM == 5) {
            if (p.isxBounds()) {
                if (p.isyBounds()) {
                    lastXPos = p.xPos;
                    lastYPos = p.yPos;
                    g.drawImage(back5, (int) (-p.xPos), (int) (-p.yPos), null);
                    g.drawImage(sonNpc.getSprite1(), (int) (sonNpc.xPos - p.xPos) + StartScreen.SCREEN_LENGTH / 2,
                            (int) (sonNpc.yPos - p.yPos) + StartScreen.SCREEN_HEIGHT / 2 - 24, 120, 120, null);
                    g.drawImage(momNpc.getSprite1(), (int) (momNpc.xPos - p.xPos) + StartScreen.SCREEN_LENGTH / 2,
                            (int) (momNpc.yPos - p.yPos) + StartScreen.SCREEN_HEIGHT / 2, 120 - 24, 120, null);
                    g.drawImage(grannyNpc.getSprite1(), (int) (grannyNpc.xPos - p.xPos) + StartScreen.SCREEN_LENGTH / 2,
                            (int) (grannyNpc.yPos - p.yPos) + StartScreen.SCREEN_HEIGHT / 2, 120, 120, null);
                    drawChat(g, p.xPos, p.yPos);
                } else {
                    lastXPos = p.xPos;
                    g.drawImage(back5, (int) (-p.xPos), (int) (-lastYPos), null);
                    g.drawImage(sonNpc.getSprite1(), (int) (sonNpc.xPos - p.xPos) + StartScreen.SCREEN_LENGTH / 2,
                            (int) (sonNpc.yPos - lastYPos) + StartScreen.SCREEN_HEIGHT / 2 - 24, 120, 120, null);
                    g.drawImage(momNpc.getSprite1(), (int) (momNpc.xPos - p.xPos) + StartScreen.SCREEN_LENGTH / 2,
                            (int) (momNpc.yPos - lastYPos) + StartScreen.SCREEN_HEIGHT / 2 - 24, 120, 120, null);
                    g.drawImage(grannyNpc.getSprite1(), (int) (grannyNpc.xPos - p.xPos) + StartScreen.SCREEN_LENGTH / 2,
                            (int) (grannyNpc.yPos - lastYPos) + StartScreen.SCREEN_HEIGHT / 2, 120, 120, null);
                    drawChat(g, p.xPos, lastYPos);
                }

            } else if (p.isyBounds()) {
                lastYPos = p.yPos;
                g.drawImage(back5, (int) (-lastXPos), (int) (-p.yPos), null);
                g.drawImage(sonNpc.getSprite1(), (int) (sonNpc.xPos - lastXPos) + StartScreen.SCREEN_LENGTH / 2,
                        (int) (sonNpc.yPos - p.yPos) + StartScreen.SCREEN_HEIGHT / 2 - 24, 120, 120, null);
                g.drawImage(momNpc.getSprite1(), (int) (momNpc.xPos - lastXPos) + StartScreen.SCREEN_LENGTH / 2,
                        (int) (momNpc.yPos - p.yPos) + StartScreen.SCREEN_HEIGHT / 2 - 24, 120, 120, null);
                g.drawImage(grannyNpc.getSprite1(), (int) (grannyNpc.xPos - lastXPos) + StartScreen.SCREEN_LENGTH / 2,
                        (int) (grannyNpc.yPos - p.yPos) + StartScreen.SCREEN_HEIGHT / 2, 120, 120, null);
                drawChat(g, lastXPos, p.yPos);
            } else {
                g.drawImage(back5, (int) (-lastXPos), (int) (-lastYPos), null);
                g.drawImage(sonNpc.getSprite1(), (int) (sonNpc.xPos - lastXPos) + StartScreen.SCREEN_LENGTH / 2,
                        (int) (sonNpc.yPos - lastYPos) + StartScreen.SCREEN_HEIGHT / 2 - 24, 120, 120, null);
                g.drawImage(momNpc.getSprite1(), (int) (momNpc.xPos - lastXPos) + StartScreen.SCREEN_LENGTH / 2,
                        (int) (momNpc.yPos - lastYPos) + StartScreen.SCREEN_HEIGHT / 2 - 24, 120, 120, null);
                g.drawImage(grannyNpc.getSprite1(), (int) (grannyNpc.xPos - lastXPos) + StartScreen.SCREEN_LENGTH / 2,
                        (int) (grannyNpc.yPos - lastYPos) + StartScreen.SCREEN_HEIGHT / 2, 120, 120, null);
                drawChat(g, lastXPos, lastYPos);

            }
        }
        truck.enterCheck(g);
    }

}