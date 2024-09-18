import java.awt.image.BufferedImage;

class NPC extends Entities {

    private String[] npcDialog,playerDialog;
    private int count = 0;
    
    protected Player p;
    protected int mapNum;
    protected Manager tpm;
    
    public NPC(BufferedImage img, int x, int y, Player p, int mapNum, Manager tnpm) {
        this.mapNum = mapNum;
        this.p = p;
        spriteR1 = img;
        xPos = x;
        yPos = y;
        tpm = tnpm;
    }
    
    public void setNPCDialog(String[] s){
        npcDialog = s;
    }
    
    public void setPlayerDialog(String[] s){
        playerDialog = s;
    }

    public BufferedImage getSprite1() {
        return spriteR1;
    }
    public void interCheck(){
        if (xPos > p.xPos - 50 && xPos < p.xPos + 50 && mapNum == tpm.getMapNum()) {
            if (p.getK().isInteract()) {
                if (npcDialog[count] == null) {
                    count = 0;
                }
                System.out.println(npcDialog[count]);
                count++;
                p.getK().setInteract(false);
            }
        }
    }
    

    @Override
    public void update() {
        
    }
}
