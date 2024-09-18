import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import javax.imageio.ImageIO;

class Manager {//class tomanage drawing tiles and npcs

    private Tiles[] tilesSpawn;
    private Tiles[] tilesCity;

    private Object[] objFor = new Object[10];
    private Object[] objSpawn = new Object[10];

    private int numObjS;
    private int numObjF;

    private BufferedImage back1, back2, back3;

    private int[][] spawn;
    private int[][] city;

    private int numLines = 0;
    private int numCol;
    private Player p;
    private NPC npc1, npc2;
    private Vechile truck;

    private float lastXPos = 0;
    private float lastYPos = 0;
    private int xCol;
    private int yCol = 0;

    public static int MAPNUM = 1;

//    public TileManager(int[][] mapLayout, int[][] city) {
//        this.spawn = mapLayout;
//        this.city = city;
//    }
    public Manager(int n, Player inP) {
        p = inP;
        //p.addTileManager(this);
        MAPNUM = 1;
        numObjF = 0;
        numObjS = 0;
        try {
            FileInputStream back1Url = new FileInputStream(MonsterHunt.WORK_DIR + "/Tiles/HomeBack.png");
            back1 = ImageIO.read(back1Url);
            FileInputStream back2Url = new FileInputStream(MonsterHunt.WORK_DIR + "/Tiles/screen2.png");
            back2 = ImageIO.read(back2Url);
            FileInputStream back3Url = new FileInputStream(MonsterHunt.WORK_DIR + "/Tiles/forest.png");

            back3 = ImageIO.read(back3Url);
            var sprite1 = new FileInputStream(MonsterHunt.WORK_DIR + "/NPCSprites/RichGuy.png");
            var img = ImageIO.read(sprite1);
            npc1 = new NPC(img, 1500, 630, p, 2, this);
            var sprite2 = new FileInputStream(MonsterHunt.WORK_DIR + "/NPCSprites/CameraWomanR.png");
            var img2 = ImageIO.read(sprite2);
            npc2 = new NPC(img2, 1000, 630, p, 1, this);
            var sprite3 = new FileInputStream(MonsterHunt.WORK_DIR + "/NPCSprites/MonsterTruck.png");
            var img3 = ImageIO.read(sprite3);
            truck = new Vechile(img3, 1000, 630, p, 3, this);
            setNpcDialog();
            
            back1Url.close();
            back2Url.close();
            back3Url.close();

        } catch (IOException e) {
            System.out.println(e);
        }
        spawn = new int[1000][1000];

        int[] numOccSpawn = new int[50];
        int numTilesSpawn = 0;
        try {

            Scanner scFile = new Scanner(new File(MonsterHunt.WORK_DIR + "/Tiles/spawn/map1.txt"));
            Scanner scObjSpawn = new Scanner(new File(MonsterHunt.WORK_DIR + "/Objects/foundObjects.txt"));
            Scanner locSpawn = new Scanner(new File(MonsterHunt.WORK_DIR + "/Objects/objLoc.txt")).useDelimiter(",");

            int locX = 0;
            int locY = 0;
            while (scObjSpawn.hasNext()) {
                Scanner scLineS = new Scanner(scObjSpawn.nextLine()).useDelimiter(",");
                String objNameS = scLineS.next();
                int inInventoryS = Integer.parseInt(scLineS.next());
                if (inInventoryS == 0) {                                    
                    locX = locSpawn.nextInt();
                    locY = locSpawn.nextInt();
                    objSpawn[numObjS] = new Object(objNameS, locX, locY, 1,p);
                }else{
                    objSpawn[numObjS] = new Object(objNameS,p );
                }

                numObjS++;
            }
            String[] scLineSpawn = {};
            while (scFile.hasNextLine()) {
                scLineSpawn = (scFile.nextLine()).split(" ");
                for (int i = 0; i < scLineSpawn.length; i++) {
                    spawn[i][numLines] = Integer.parseInt(scLineSpawn[i]);
                    if (numOccSpawn[spawn[i][numLines]] == 0) {
                        numTilesSpawn++;
                        numOccSpawn[spawn[i][numLines]]++;
                    }

                }
//
                numLines++;
//
            }
//
            numCol = scLineSpawn.length;
        } catch (IOException e) {
            System.out.println(e);
        }
        tilesSpawn = new Tiles[numTilesSpawn];
        for (int i = 0; i < numTilesSpawn; i++) {
            tilesSpawn[i] = new Tiles(i, "Spawn");
        }

    }

    public int[][] getMapLayout() {
        return spawn;
    }

    public int getMapNum() {
        return MAPNUM;
    }

    public int[][] getSpawn() {
        return spawn;
    }

    public int[][] getCity() {
        return city;
    }

    public void setNpcDialog() {
        String[] s = new String[10];
        int index = 0;
        try {
            Scanner scFile = new Scanner(new File(MonsterHunt.WORK_DIR + "/Dialog/demoDialog.txt"));
            while (scFile.hasNextLine()) {
                s[index] = scFile.nextLine();
                index++;
            }
            npc1.setNPCDialog(s);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void setLastXPos(float lastXPos) {
        this.lastXPos = lastXPos;
    }

    public void setMapNum(int mapNum) {
        this.MAPNUM = mapNum;
    }

    public void setLastYPos(float lastYPos) {
        this.lastYPos = lastYPos;
    }

    public void drawObj(Graphics2D g, int n, float x, float y){
        for (int i = 0; i < numObjS; i++) {
            if (objSpawn[i].pickUp()) {
                for (int j = i; j < numObjS -1; j++) {
                    objSpawn[j] = objSpawn[j+1];
                }
                numObjS --;
                break;
            }
            if (objSpawn[i].getMapNum() == n) {
                g.drawImage(objSpawn[i].getBi(), (int)(objSpawn[i].getX() - x)+ StartScreen.SCREEN_LENGTH / 2+64, (int)(objSpawn[i].getY() - y) + StartScreen.SCREEN_WIDTH / 2 + 100, 48,48,null);
                System.out.println(objSpawn[i].getX() + "   " + objSpawn[i].getY());
            }
        }
    }
    public void updateTiles(Graphics2D g) {
        npc1.interCheck();
        npc2.interCheck();
        truck.interCheck();
        if (MAPNUM == 1) {
            //System.out.println(lastXPos + " " + lastYPos);
            if (p.isxBounds()) {
                if (p.isyBounds()) {
                    lastXPos = p.xPos;
                    lastYPos = p.yPos;
                    g.drawImage(back1, (int) (-p.xPos), (int) (-p.yPos), null);
                    //g.drawImage(npc1.getSprite1(), (int) (npc1.xPos - p.xPos) + SCREEN_LENGTH / 2, (int) (npc1.yPos - p.yPos) + SCREEN_WIDTH / 2 - 24, 120, 120, null);
                    g.drawImage(npc2.getSprite1(), (int) (npc2.xPos - p.xPos) + StartScreen.SCREEN_LENGTH / 2, (int) (npc2.yPos - p.yPos) + StartScreen.SCREEN_WIDTH / 2 - 24, 120, 120, null);
                    drawObj(g,MAPNUM,p.xPos,p.yPos);
                } else {
                    lastXPos = p.xPos;
                    g.drawImage(back1, (int) (-p.xPos), (int) (-lastYPos), null);
                    //g.drawImage(npc1.getSprite1(), (int) (npc1.xPos - p.xPos) + SCREEN_LENGTH / 2, (int) (npc1.yPos - lastYPos) + SCREEN_WIDTH / 2 - 24, 120, 120, null);
                    g.drawImage(npc2.getSprite1(), (int) (npc2.xPos - p.xPos) + StartScreen.SCREEN_LENGTH / 2, (int) (npc2.yPos - lastYPos) + StartScreen.SCREEN_WIDTH / 2 - 24, 120, 120, null);
                    drawObj(g,MAPNUM,p.xPos,lastYPos);
                }

            } else if (p.isyBounds()) {
                lastYPos = p.yPos;
                g.drawImage(back1, (int) (-lastXPos), (int) (-p.yPos), null);
                //g.drawImage(npc1.getSprite1(), (int) (npc1.xPos - lastXPos) + SCREEN_LENGTH / 2, (int) (npc1.yPos - p.yPos) + SCREEN_WIDTH / 2 - 24, 120, 120, null);
                g.drawImage(npc2.getSprite1(), (int) (npc2.xPos - lastXPos) + StartScreen.SCREEN_LENGTH / 2, (int) (npc2.yPos - p.yPos) + StartScreen.SCREEN_WIDTH / 2 - 24, 120, 120, null);
                drawObj(g,MAPNUM,lastXPos,p.yPos);
            } else {
                g.drawImage(back1, (int) (-lastXPos), (int) (-lastYPos), null);
                //g.drawImage(npc1.getSprite1(), (int) (npc1.xPos - lastXPos) + SCREEN_LENGTH / 2, (int) (npc1.yPos - lastYPos) + SCREEN_WIDTH / 2 - 24, 120, 120, null);
                g.drawImage(npc2.getSprite1(), (int) (npc2.xPos - lastXPos) + StartScreen.SCREEN_LENGTH / 2, (int) (npc2.yPos - lastYPos) + StartScreen.SCREEN_WIDTH / 2 - 24, 120, 120, null);
                drawObj(g,MAPNUM,lastXPos,lastYPos);

            }
        } else if (MAPNUM == 2) {
            if (p.isxBounds()) {
                if (p.isyBounds()) {
                    lastXPos = p.xPos;
                    lastYPos = p.yPos;
                    g.drawImage(back2, (int) (-p.xPos), (int) (-p.yPos), null);
                    g.drawImage(npc1.getSprite1(), (int) (npc1.xPos - p.xPos) + StartScreen.SCREEN_LENGTH / 2, (int) (npc1.yPos - p.yPos) + StartScreen.SCREEN_WIDTH / 2 - 24, 120, 120, null);
                    //g.drawImage(npc2.sprite1, (int) (npc2.xPos - p.xPos) + SCREEN_LENGTH / 2, (int) (npc2.yPos - p.yPos) + SCREEN_WIDTH / 2 - 24, 120, 120, null);
                } else {
                    lastXPos = p.xPos;
                    g.drawImage(back2, (int) (-p.xPos), (int) (-lastYPos), null);
                    g.drawImage(npc1.getSprite1(), (int) (npc1.xPos - p.xPos) + StartScreen.SCREEN_LENGTH / 2, (int) (npc1.yPos - lastYPos) + StartScreen.SCREEN_WIDTH / 2 - 24, 120, 120, null);
                    //g.drawImage(npc2.sprite1, (int) (npc2.xPos - p.xPos) + SCREEN_LENGTH / 2, (int) (npc2.yPos - lastYPos) + SCREEN_WIDTH / 2 - 24, 120, 120, null);
                }

            } else if (p.isyBounds()) {
                lastYPos = p.yPos;
                g.drawImage(back2, (int) (-lastXPos), (int) (-p.yPos), null);
                g.drawImage(npc1.getSprite1(), (int) (npc1.xPos - lastXPos) + StartScreen.SCREEN_LENGTH / 2, (int) (npc1.yPos - p.yPos) + StartScreen.SCREEN_WIDTH / 2 - 24, 120, 120, null);
                //g.drawImage(npc2.sprite1, (int) (npc2.xPos - lastXPos) + SCREEN_LENGTH / 2, (int) (npc2.yPos - p.yPos) + SCREEN_WIDTH / 2 - 24, 120, 120, null);
            } else {
                g.drawImage(back2, (int) (-lastXPos), (int) (-lastYPos), null);
                g.drawImage(npc1.getSprite1(), (int) (npc1.xPos - lastXPos) + StartScreen.SCREEN_LENGTH / 2, (int) (npc1.yPos - lastYPos) + StartScreen.SCREEN_WIDTH / 2 - 24, 120, 120, null);
                //g.drawImage(npc2.sprite1, (int) (npc2.xPos - lastXPos) + SCREEN_LENGTH / 2, (int) (npc2.yPos - lastYPos) + SCREEN_WIDTH / 2 - 24, 120, 120, null);

            }
        } else if (MAPNUM == 3) {
            if (p.isxBounds()) {
                if (p.isyBounds()) {
                    lastXPos = p.xPos;
                    lastYPos = p.yPos;
                    g.drawImage(back3, (int) (-p.xPos), (int) (-p.yPos), null);
                    g.drawImage(truck.getSprite1(), (int) (truck.xPos - p.xPos) + StartScreen.SCREEN_LENGTH / 2, (int) (truck.yPos - p.yPos) + StartScreen.SCREEN_WIDTH / 2 - 150, null);

                } else {
                    lastXPos = p.xPos;
                    g.drawImage(back3, (int) (-p.xPos), (int) (-lastYPos), null);
                    g.drawImage(truck.getSprite1(), (int) (truck.xPos - p.xPos) + StartScreen.SCREEN_LENGTH / 2, (int) (truck.yPos - lastYPos) + StartScreen.SCREEN_WIDTH / 2 - 150, null);
                }

            } else if (p.isyBounds()) {
                lastYPos = p.yPos;
                g.drawImage(back3, (int) (-lastXPos), (int) (-p.yPos), null);
                g.drawImage(truck.getSprite1(), (int) (truck.xPos - lastXPos) + StartScreen.SCREEN_LENGTH / 2, (int) (truck.yPos - p.yPos) + StartScreen.SCREEN_WIDTH / 2 - 150, null);
            } else {
                g.drawImage(back3, (int) (-lastXPos), (int) (-lastYPos), null);
                g.drawImage(truck.getSprite1(), (int) (truck.xPos - lastXPos) + StartScreen.SCREEN_LENGTH / 2, (int) (truck.yPos - lastYPos) + StartScreen.SCREEN_WIDTH / 2 - 150, null);

            }
        } else if (MAPNUM == 4) {
            if (p.isxBounds()) {
                if (p.isyBounds()) {
                    lastXPos = p.xPos;
                    lastYPos = p.yPos;
                    for (int j = 0; j < numLines; j++) {
                        xCol = 0;
                        for (int i = 0; i < numCol; i++) {
                            g.drawImage(tilesSpawn[(spawn[i][j])].getTile(), (int) (xCol - p.xPos), (int) (yCol - p.yPos), null);
                            xCol += 48;
                        }
                        yCol += 48;

                    }

                } else {
                    lastXPos = p.xPos;
                    for (int j = 0; j < numLines; j++) {
                        xCol = 0;
                        for (int i = 0; i < numCol; i++) {
                            g.drawImage(tilesSpawn[(spawn[i][j])].getTile(), (int) (xCol - p.xPos), (int) (yCol - lastYPos), null);
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
                        g.drawImage(tilesSpawn[(spawn[i][j])].getTile(), (int) (xCol - lastXPos), (int) (yCol - p.yPos), null);
                        xCol += 48;
                    }
                    yCol += 48;

                }
            } else {
                for (int j = 0; j < numLines; j++) {
                    xCol = 0;
                    for (int i = 0; i < numCol; i++) {
                        g.drawImage(tilesSpawn[(spawn[i][j])].getTile(), (int) (xCol - lastXPos), (int) (yCol - lastYPos), null);
                        xCol += 48;
                    }
                    yCol += 48;

                }
            }

            yCol = 0;
            g.drawString("Click C to exit", StartScreen.SCREEN_LENGTH / 2, StartScreen.SCREEN_WIDTH);
        }
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

}