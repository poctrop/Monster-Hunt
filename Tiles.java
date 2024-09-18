import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

class Tiles {

    private BufferedImage tile;
    public boolean passable;


    public Tiles(int tileNum, String map) {
        try {
            FileInputStream fs = new FileInputStream(MonsterHunt.WORK_DIR + "/Tiles/" + map + "/tile" + tileNum + ".png");
            tile = ImageIO.read(fs);
            fs.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public boolean isPassable() {
        return passable;
    }

    public BufferedImage getTile() {
        return tile;
    }

    

}