import java.awt.image.BufferedImage;
import java.util.*;

class NPC extends Entities {

    private LinkedList<String> npcDialog;
    private int count = 0;

    protected Player p;
    protected int mapNum;
    protected Manager tpm;

    private boolean talking = false;

    private int timer = 0;

    private String chat = ""; 

    public NPC(BufferedImage img, int x, int y, Player p, int mapNum, Manager tnpm) {
        this.mapNum = mapNum;
        this.p = p;
        spriteR1 = img;
        xPos = x;
        yPos = y;
        tpm = tnpm;
        npcDialog = new LinkedList<>();
    }

    public BufferedImage getSprite1() {
        return spriteR1;
    }

    // public void setNpcD(LinkedList<String> d){ 
    //     npcDialog = d;
    // }
    public boolean interCheck() {
        if(p.getK().isInteract()){
            talking = true;
            System.out.println("as"); 
        }
        if (xPos > p.xPos - 100 && xPos < p.xPos + 100 && mapNum == tpm.getMapNum()) {
            if (talking && timer < 70*3) {
                timer++;
                return true;
            } else if (timer >=70 * 3) {
                talking = false;
                timer = 0;
                count++;
                return false;
            }
        } else {
            return false;
        }
        return false;
    }

    public void chat() {
        if (npcDialog.size() == 0) {
            chat = npcDialog.get(count % npcDialog.size()); 
            
        }
        
    }
    
    public void chat(String s){
        chat = s;
    }

    @Override
    public void update() {

    }

    public String getChat() {
        return chat;
    }

    public LinkedList<String> getNpcDialog() {
        return npcDialog;
    }

    
    public void setNpcD(String s){
        npcDialog.add(s);
    }

    public void setTalk(boolean t){
        talking = t;
    }
}
