import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

class Object {
    
    private boolean inInventory;
    private BufferedImage bi;
    private int x;
    private int y;
    private int mapNum = 1;
    
    private Player p;
    
    public Object(String name, Player p) {
        inInventory = true;
        try {
            FileInputStream fis = new FileInputStream(MonsterHunt.WORK_DIR + "/Objects/" + name + ".png");
            bi = ImageIO.read(fis);
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    
    public Object(String name, int x, int y, int mapNum, Player p) {
        this.p = p;
        this.mapNum = mapNum;
        inInventory = false;
        this.x = x;
        this.y = y;
        try {
            FileInputStream fis = new FileInputStream(MonsterHunt.WORK_DIR + "/Objects/" + name + ".png");
            bi = ImageIO.read(fis);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public BufferedImage getBi() {
        return bi;
    }
    

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    

    public int getMapNum() {
        return mapNum;
    }
    
    
    
    
    public boolean pickUp() {
        if ((p.getxPos() > x - 50) && (p.getxPos() < x + 50) && (p.getyPos() < 50 + y)&& (p.getyPos() > -50 + y)) {
            inInventory = true;
            p.addObject(this);
            return true;
        }
        return false;
    }
    
}