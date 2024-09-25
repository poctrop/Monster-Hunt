import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;

class Player extends Entities {

    // the sprites for the player as well as the car when driving it;
    public BufferedImage driveUp1, driveUp2, driveDown1, driveDown2, driveR1, driveR2, driveL1, driveL2;

    // alterantes between the sprites left and right for more fluid movement
    private int spriteRCount = 0;
    private int spriteLCount = 0;
    private int driveCount = 0;

    // The starting position of the player
    public float startPosX = 1;
    public float startPosY = 1;

    // used to draw the player in the middle of the screen
    private float screenX = StartScreen.SCREEN_LENGTH / 2;
    private float screenY = StartScreen.SCREEN_WIDTH / 2;

    // checks if the player is not near the edge of the screen
    private boolean xBounds = true; 
    private boolean yBounds = true;

    // checks if the player is on a surface
    private boolean gravOn = true;
    private boolean touchGround = false;
    private boolean onPlatform = false; 
    private boolean level = false;

    // keeps the platform locations
    private int[][] startPosSpawn = new int[50][2];
    private int[][] endPosSpawn = new int[50][2];
    private int[][] startPosForest = new int[50][2];
    private int[][] endPosForest = new int[50][2];

    // used to check how many platforms ont each map
    private int platformCountSpawn = 0;
    private int platformCountForest = 0;

    // used for player movement
    private float driveSpeed = 5;
    private float playerSpeed = 7;
    private float playerJump = 22;
    // used when the player is falling down
    private float accGrav = 0.6f;
    private float speedDown = 10;

    // listens for key evensts
    private Keys k;

    // draws the background and npcs
    private Manager tm;

    public Player(Keys k) {
        this.k = k;
        xPos = startPosX;
        yPos = startPosY;
        try {

            // reads in a file which has the information about which co-ordinates are solid
            Scanner spawnPos = new Scanner(new File(MonsterHunt.WORK_DIR + "/Tiles/solidSpawn.txt"));
            Scanner forestPos = new Scanner(new File(MonsterHunt.WORK_DIR + "/Tiles/solidForest.txt"));
            // player sprites
            FileInputStream fsj = new FileInputStream(
                    new File(MonsterHunt.WORK_DIR + "/PlayerSprites/adventureJump.png"));
            FileInputStream fsl1 = new FileInputStream(
                    new File(MonsterHunt.WORK_DIR + "/PlayerSprites/adventureWalkL1.png"));
            FileInputStream fsl2 = new FileInputStream(
                    new File(MonsterHunt.WORK_DIR + "/PlayerSprites/adventureWalkL2.png"));
            FileInputStream fsr1 = new FileInputStream(
                    new File(MonsterHunt.WORK_DIR + "/PlayerSprites/adventureWalkR1.png"));
            FileInputStream fsr2 = new FileInputStream(
                    new File(MonsterHunt.WORK_DIR + "/PlayerSprites/adventureWalkR2.png"));
            // truck sprites
            FileInputStream fsDriveU1 = new FileInputStream(
                    new File(MonsterHunt.WORK_DIR + "/PlayerSprites/MonsterTruckUp1.png"));
            FileInputStream fsDriveU2 = new FileInputStream(
                    new File(MonsterHunt.WORK_DIR + "/PlayerSprites/MonsterTruckUp2.png"));
            FileInputStream fsDriveD1 = new FileInputStream(
                    new File(MonsterHunt.WORK_DIR + "/PlayerSprites/MonsterTruckDown1.png"));
            FileInputStream fsDriveD2 = new FileInputStream(
                    new File(MonsterHunt.WORK_DIR + "/PlayerSprites/MonsterTruckDown2.png"));
            FileInputStream fsDriveR1 = new FileInputStream(
                    new File(MonsterHunt.WORK_DIR + "/PlayerSprites/MonsterTruckRight1.png"));
            FileInputStream fsDriveR2 = new FileInputStream(
                    new File(MonsterHunt.WORK_DIR + "/PlayerSprites/MonsterTruckRight2.png"));
            FileInputStream fsDriveL1 = new FileInputStream(
                    new File(MonsterHunt.WORK_DIR + "/PlayerSprites/MonsterTruckLeft1.png"));
            FileInputStream fsDriveL2 = new FileInputStream(
                    new File(MonsterHunt.WORK_DIR + "/PlayerSprites/MonsterTruckLeft2.png"));

            driveDown1 = ImageIO.read(fsDriveD1);
            driveDown2 = ImageIO.read(fsDriveD2);
            driveUp1 = ImageIO.read(fsDriveU1);
            driveUp2 = ImageIO.read(fsDriveU2);
            driveR1 = ImageIO.read(fsDriveR1);
            driveR2 = ImageIO.read(fsDriveR2);
            driveL1 = ImageIO.read(fsDriveL1);
            driveL2 = ImageIO.read(fsDriveL2);

            spriteJ = ImageIO.read(fsj);
            spriteL1 = ImageIO.read(fsl1);
            spriteL2 = ImageIO.read(fsl2);
            spriteR1 = ImageIO.read(fsr1);
            spriteR2 = ImageIO.read(fsr2);

            sprite = spriteJ;

            fsj.close();
            fsl1.close();
            fsl2.close();
            fsr1.close();
            fsr2.close();

            // populates the start and end posistions it alternates between adding a value
            // to the starting positions an then adds to the ending positions
            boolean isStartPos = true;
            while (spawnPos.hasNext()) {
                if (isStartPos) {
                    startPosSpawn[platformCountSpawn] = pos(spawnPos.nextLine());
                    isStartPos = false;
                } else {
                    endPosSpawn[platformCountSpawn] = pos(spawnPos.nextLine());
                    platformCountSpawn++;
                    isStartPos = true;
                }

            }
            while (forestPos.hasNext()) {
                if (isStartPos) {
                    startPosForest[platformCountForest] = pos(forestPos.nextLine());
                    isStartPos = false;
                } else {
                    endPosForest[platformCountForest] = pos(forestPos.nextLine());
                    platformCountForest++;
                    isStartPos = true;
                }
            }
            // garbage
            spawnPos.close();
            forestPos.close();

        } catch (IOException e) {
            System.out.println(e);
        }

    }

    public Keys getK() {
        return k;
    }

    public BufferedImage getSprite1() {
        return sprite;
    }

    public int[] pos(String s) {
        String[] splitS = s.split(",");
        int x = Integer.parseInt(splitS[0]) - 393;
        int y = Integer.parseInt(splitS[1]) - 96 + 20;
        return new int[] { x, y };
    }

    public void isGround() {
        if (tm.getMapNum() == 1) {
            for (int i = 0; i < platformCountSpawn; i++) {
                // checks if the players position is near one of the platform locations (Uses
                // start and end pos arrays)
                if ((int) xPos > startPosSpawn[i][0] && (int) xPos < endPosSpawn[i][0]
                        && (int) yPos < endPosSpawn[i][1] + speedDown && (int) yPos > endPosSpawn[i][1]
                        && playerJump - speedDown <= 0) {
                    yPos = (int) endPosSpawn[i][1];
                    onPlatform = true;
                    touchGround = false;
                    level = true;// used to check if the player is on the platform or near the platform, if it is
                                 // on the platform level is true
                    break;
                } else if ((int) yPos == endPosSpawn[i][1] && (int) xPos > startPosSpawn[i][0]
                        && (int) xPos < endPosSpawn[i][0]) {
                    onPlatform = true;
                    touchGround = false;
                    speedDown = 0;
                    break;
                    // checks if the player is on the ground
                } else if ((int) yPos >= 630) {
                    onPlatform = false;
                    touchGround = true;
                    break;
                } else {
                    onPlatform = false;
                    level = false;
                    touchGround = false;
                }
            }
        } else if (tm.getMapNum() == 3) {
            for (int i = 0; i < platformCountForest; i++) {
                // checks if the players position is near one of the platform locatioons (Uses
                // start and end pos arrays)
                if ((int) xPos > startPosForest[i][0] && (int) xPos < endPosForest[i][0]
                        && (int) yPos < endPosForest[i][1] + speedDown && (int) yPos > endPosForest[i][1]
                        && playerJump - speedDown <= 0) {
                    yPos = (int) endPosForest[i][1];
                    onPlatform = true;
                    touchGround = false;
                    level = true;// used to check if the player is on the platform or near the platform, if it is
                                 // on the platform level is true
                    break;
                } else if ((int) yPos == endPosForest[i][1] && (int) xPos > startPosForest[i][0]
                        && (int) xPos < endPosForest[i][0]) {
                    onPlatform = true;
                    touchGround = false;
                    speedDown = 0;
                    break;
                    // checks if the player is on the ground
                } else if ((int) yPos >= 630) {
                    onPlatform = false;
                    touchGround = true;
                    break;
                } else {
                    onPlatform = false;
                    level = false;
                    touchGround = false;
                }
            }
        } else {
            if ((int) yPos >= 630) {
                onPlatform = false;
                touchGround = true;
            } else {
                onPlatform = false;
                level = false;
                touchGround = false;
            }
        }
    }

    ////////////Getters///////////////////////////////

    public ArrayList<Object> getObjects() {
        return obj;
    }

    public Object getObj(String s) {
        for (int i = 0; i < obj.size(); i++) {
            if (obj.get(i).getName().equalsIgnoreCase(s)) {
                return obj.get(i);
            }
        }
        return null;
    }

    public ArrayList<Object> getObj() {
        return obj;
    }

    public float getPlayerSpeed() {
        return playerSpeed;
    }

    public float getxPos() {
        return xPos;
    }

    public float getyPos() {
        return yPos;
    }

    public float getScreenX() {
        return screenX;
    }

    public float getScreenY() {
        return screenY;
    }

    public boolean isxBounds() {
        return xBounds;
    }

    public boolean isyBounds() {
        return yBounds;
    }
////////////////////////////////end of getters/////////////////////////////////////////////

/////////////////////////////////setters/////////////////////////////////////////////////

    public void setxBounds(boolean xBounds) {
        this.xBounds = xBounds;
    }

    public void setyBounds(boolean yBounds) {
        this.yBounds = yBounds;
    }

////////////////////////////end of setter////////////////////////////////////////
    @Override
    public void update() {
        if (tm.getMapNum() != 4) {
            if (touchGround == false && onPlatform == false) {
                sprite = spriteJ;
            } else if (k.isRightP()) {

                if (spriteRCount < 15) {
                    sprite = spriteR1;
                } else {
                    sprite = spriteR2;
                }
                if (spriteRCount < 30) {
                    spriteRCount++;
                } else {
                    spriteRCount = 0;
                }
            } else if (k.isLeftP()) {

                if (spriteLCount < 15) {
                    sprite = spriteL1;
                } else {
                    sprite = spriteL2;
                }
                if (spriteLCount < 30) {
                    spriteLCount++;
                } else {
                    spriteLCount = 0;
                }
            } else {
                sprite = spriteR1;
            }

            // movement when the map is side view~
            if (xBounds && yBounds) { // checks whether the player is near the edges of the screen
                screenX = StartScreen.SCREEN_LENGTH / 2; // if it is the player is drawn in the middle of the screen
                screenY = StartScreen.SCREEN_WIDTH / 2;
                // uses the key listener to check of the up, left and right buttons are pressed
                if (k.isUpP() && k.isLeftP()) {
                    yPos -= playerJump;
                    xPos -= playerSpeed;
                } else if (k.isUpP() && k.isRightP()) {
                    yPos -= playerJump;
                    xPos += playerSpeed;
                } else if (k.isLeftP()) {
                    xPos -= playerSpeed;
                } else if (k.isUpP()) {
                    yPos -= playerJump;
                } else if (k.isRightP()) {
                    xPos += playerSpeed;
                }
            } else if (xBounds) { // only the x is not near the edge of the screen
                screenX = StartScreen.SCREEN_LENGTH / 2;
                if (k.isUpP() && k.isLeftP()) {
                    screenY -= playerJump;// the y coodinate where the caracter is drawn changes the same amount that
                                          // the y coordinate of the player changes
                    yPos -= playerJump;
                    xPos -= playerSpeed;
                } else if (k.isUpP() && k.isRightP()) {
                    screenY -= playerJump;
                    yPos -= playerJump;
                    xPos += playerSpeed;
                } else if (k.isLeftP()) {
                    xPos -= playerSpeed;
                } else if (k.isUpP()) {
                    screenY -= playerJump;
                    yPos -= playerJump;
                } else if (k.isRightP()) {
                    xPos += playerSpeed;
                }
            } else if (yBounds) {// only the y is not near the edge of the screen
                screenY = StartScreen.SCREEN_WIDTH / 2;
                if (k.isUpP() && k.isLeftP() && xPos > -StartScreen.SCREEN_LENGTH / 2 - 120) {
                    yPos -= playerJump;
                    xPos -= playerSpeed;
                    screenX -= playerSpeed;// the x coodinate where the caracter is drawn changes the same amount that
                                           // the y coordinate of the player changes
                } else if (k.isUpP() && k.isRightP() && xPos < 2521 - StartScreen.SCREEN_LENGTH / 2) {
                    yPos -= playerJump;
                    xPos += playerSpeed;
                    screenX += playerSpeed;
                } else if (k.isLeftP() && xPos > -StartScreen.SCREEN_LENGTH / 2 - 120) {
                    screenX -= playerSpeed;
                    xPos -= playerSpeed;
                } else if (k.isUpP()) {
                    yPos -= playerJump;
                } else if (k.isRightP() && xPos < 2521 - StartScreen.SCREEN_LENGTH / 2) {
                    screenX += playerSpeed;
                    xPos += playerSpeed;
                }
            } else {// both the x and y coordinate are near the edge of the screen
                if (k.isUpP() && k.isLeftP() && xPos > -StartScreen.SCREEN_LENGTH / 2 - 120) {
                    screenY -= playerJump;
                    screenX -= playerSpeed;
                    yPos -= playerJump;
                    xPos -= playerSpeed;

                } else if (k.isUpP() && k.isRightP() && xPos < 2521 - StartScreen.SCREEN_LENGTH / 2) {
                    screenY -= playerJump;
                    screenX += playerSpeed;
                    yPos -= playerJump;
                    xPos += playerSpeed;
                } else if (k.isLeftP() && xPos > -StartScreen.SCREEN_LENGTH / 2 - 120) {
                    screenX -= playerSpeed;
                    xPos -= playerSpeed;
                } else if (k.isUpP()) {
                    yPos -= playerJump;
                    screenY -= playerJump;
                } else if (k.isRightP() && xPos < 2521 - StartScreen.SCREEN_LENGTH / 2) {
                    screenX += playerSpeed;
                    xPos += playerSpeed;
                }
            }
            float diff = 0;
            isGround();

            if (touchGround == true) {
                k.setUpP(false);
                if ((int) (yPos + speedDown + accGrav) > 630 && gravOn == true) { // checks if the next position is
                                                                                  // lower than the lowest position the
                                                                                  // player can be
                    speedDown = 0;
                    diff = yPos - 630;
                    yPos = 630;
                    screenY = screenY - diff;
                }
                gravOn = false;
                if (((int) yPos > 630)) {
                    speedDown = 0;
                }
            } else if (!onPlatform) {
                if (level == false) {
                    gravOn = true;
                }
            } else {
                k.setUpP(false);
                speedDown = 0;
                gravOn = false;
            }
            if (gravOn) {
                yPos += speedDown;
                if (yBounds == false) {
                    screenY += speedDown;
                }
                if (speedDown < 35) {
                    speedDown += accGrav;
                }

            }
            if (tm.getMapNum() == 1 && xPos > 2521 - StartScreen.SCREEN_LENGTH / 2 - 75) {
                tm.setMapNum(2);
                xPos = 0;
                // yPos = 630;
            } else if (tm.getMapNum() == 2 && xPos < -StartScreen.SCREEN_LENGTH / 2 - 75) {
                tm.setMapNum(1);
                tm.setLastXPos(1758.0f);
                tm.setLastYPos(507.6f);
                screenX = StartScreen.SCREEN_LENGTH / 2;
                xPos = 2521 - StartScreen.SCREEN_LENGTH;
                // yPos = 630;
            } else if (tm.getMapNum() == 1 && xPos < -StartScreen.SCREEN_LENGTH / 2 - 75) {
                tm.setMapNum(3);
                tm.setLastXPos(1758.0f);
                tm.setLastYPos(507.6f);
                screenX = StartScreen.SCREEN_LENGTH / 2;
                xPos = 2521 - StartScreen.SCREEN_LENGTH;
                // yPos = 630;
            } else if (tm.getMapNum() == 3 && xPos > 2521 - StartScreen.SCREEN_LENGTH / 2 - 75) {
                tm.setMapNum(1);
                xPos = 0;

            }

            // When the player is driving
        } else {
            if (k.isInteract()) {
                tm.setMapNum(3);
                k.setInteract(false);
                xPos = 1080;
                yPos = 450;
            }
            if (k.isDownP()) {
                if (driveCount < 10) {
                    sprite = driveDown1;
                } else {
                    sprite = driveDown2;
                }

            } else if (k.isLeftP()) {
                if (driveCount < 10) {
                    sprite = driveL1;
                } else {
                    sprite = driveL2;
                }
            } else if (k.isUpP()) {
                if (driveCount < 10) {
                    sprite = driveUp1;
                } else {
                    sprite = driveUp2;
                }
            } else if (k.isRightP()) {
                if (driveCount < 10) {
                    sprite = driveR1;
                } else {
                    sprite = driveR2;

                }
            }
            if (driveCount == 20) {
                driveCount = 0;
            } else {
                driveCount++;
            }
            if (xBounds && yBounds) { // checks whether the player is near the edges of the screen
                screenX = StartScreen.SCREEN_LENGTH / 2; // if it is the player is drawn in the middle of the screen
                screenY = StartScreen.SCREEN_WIDTH / 2;
                // uses the key listener to check of the up, left and right buttons are pressed
                if (k.isDownP()) {
                    yPos += driveSpeed;
                } else if (k.isLeftP()) {
                    xPos -= driveSpeed;
                } else if (k.isUpP()) {
                    yPos -= driveSpeed;
                } else if (k.isRightP()) {
                    xPos += driveSpeed;
                }
            } else if (xBounds) { // only the x is not near the edge of the screen
                screenX = StartScreen.SCREEN_LENGTH / 2;
                if (k.isDownP() && yPos < 48 * 50 - StartScreen.SCREEN_LENGTH / 2) {
                    screenY += driveSpeed;
                    yPos += driveSpeed;
                } else if (k.isLeftP()) {
                    xPos -= driveSpeed;
                } else if (k.isUpP() && yPos > -StartScreen.SCREEN_WIDTH / 2) {
                    screenY -= driveSpeed;
                    yPos -= driveSpeed;
                } else if (k.isRightP()) {
                    xPos += driveSpeed;
                }
            } else if (yBounds) {// only the y is not near the edge of the screen
                screenY = StartScreen.SCREEN_WIDTH / 2;
                if (k.isDownP()) {
                    yPos += driveSpeed;
                } else if (k.isLeftP() && xPos > -StartScreen.SCREEN_LENGTH / 2) {
                    screenX -= driveSpeed;
                    xPos -= driveSpeed;
                } else if (k.isUpP()) {
                    yPos -= driveSpeed;
                } else if (k.isRightP() && xPos < 48 * 50 - StartScreen.SCREEN_LENGTH / 2) {
                    screenX += driveSpeed;
                    xPos += driveSpeed;
                }
            } else {// both the x and y coordinate are near the edge of the screen
                if (k.isDownP() && yPos < 48 * 50 - StartScreen.SCREEN_WIDTH / 2) {
                    yPos += driveSpeed;
                    screenY += driveSpeed;
                } else if (k.isLeftP() && xPos > -StartScreen.SCREEN_LENGTH / 2) {
                    screenX -= driveSpeed;
                    xPos -= driveSpeed;
                } else if (k.isUpP() && yPos > -StartScreen.SCREEN_WIDTH / 2) {
                    yPos -= driveSpeed;
                    screenY -= driveSpeed;
                } else if (k.isRightP() && xPos < 48 * 50 - StartScreen.SCREEN_LENGTH / 2) {
                    screenX += driveSpeed;
                    xPos += driveSpeed;
                }
            }

        }
    }

    public void addManager(Manager tm) {
        this.tm = tm;
    }
}
