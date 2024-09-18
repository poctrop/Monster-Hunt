import java.awt.image.BufferedImage;

class Vechile extends NPC {

    
    public Vechile(BufferedImage img, int x, int y, Player p, int mapNum, Manager tnpm) {
        super(img, x, y, p, mapNum, tnpm);
    }

    @Override
    public void interCheck() {
        if (xPos > p.xPos - 150 && xPos < p.xPos + 150 && mapNum == tpm.getMapNum()) {
            if (p.getK().isInteract()) {
                tpm.setMapNum(4);
                p.sprite = p.driveUp1;
                p.getK().setInteract(false);
            }
        }
    }
}