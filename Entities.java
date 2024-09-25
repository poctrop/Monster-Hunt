import java.awt.image.BufferedImage;
import java.util.ArrayList;

class Entities {
    protected float xPos;
    protected float yPos;
    protected ArrayList<Object> obj = new ArrayList<>();
    private int numObjects = 0;
    protected BufferedImage spriteL1, spriteL2, spriteR1, spriteR2, spriteJ, sprite;

    public void addObject(Object o) {
        if (numObjects <= 2) {// If a player is near an object and the players inventory is not full, add it
                              // to the players inventory
            obj.add(o);
            numObjects++;
        }
    }
  
    public void remove(Object o){
        obj.remove(o);
        numObjects --;
    }

    public void update() {

    }

    public float getxPos() {
        return xPos;
    }

    public float getyPos() {
        return yPos;
    }

}