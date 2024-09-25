import java.util.*;
import java.io.*;

class Story {

    public ArrayList<String> itemsNPC, itemsPlayer, itemsEnv; //used to store the items that are a) in the npcs inventory b) the players inventory c) the environment
    public ArrayList<Integer> taskCompleted; //stores which tasks hava been completed.
    private ArrayList<NPC> people; // stores all the npcs in the game
    private Scanner[] scanners = new Scanner[2];
    private Player p; //the player
    private int timerT = 0; //used to make the npc speak for a certain ammount of time
    private boolean itemGiven = false; 

    public Story(ArrayList<NPC> people, Player p){
        this.p = p;
        this.people = people;
        try {
            String line;
            scanners[0] = new Scanner(new File(MonsterHunt.WORK_DIR + "\\Story\\demoDialog.txt"));
            scanners[1] = new Scanner(new File(MonsterHunt.WORK_DIR + "\\Story\\CameraWamen.txt"));
            Scanner scFile = new Scanner(new File(MonsterHunt.WORK_DIR + "/Tiles/spawn/map1.txt"));
            Scanner scObjSpawn = new Scanner(new File(MonsterHunt.WORK_DIR + "/Objects/foundObjects.txt"));
            Scanner locSpawn = new Scanner(new File(MonsterHunt.WORK_DIR + "/Objects/objLoc.txt")).useDelimiter(",");

            for (int i = 0; i < people.size(); i++) {
                while (scanners[i].hasNextLine()) {
                    line = scanners[i].nextLine();
                    System.out.println(line);
                    people.get(i).setNpcD(line);
                }
                scanners[i].close(); 
            }
            

        } catch (Exception e) {
            System.out.println(e + MonsterHunt.WORK_DIR + "\\Story\\demoDialog.txt ");
        }

    }

    public boolean task1() {

        if (p.getK().isInteract() && (p.getxPos() < people.get(0).getxPos() + 100)
                && (p.getxPos() > people.get(0).getxPos() - 100)) {
            Object o = p.getObj("Lens");
            if (o != null) {
                LinkedList<String> s = new LinkedList<>();
                s.add("Thanks for your help");
                itemGiven = true;
                people.get(0).addObject(o);
                p.remove(o);
                //people.get(0).setNpcD(s);
                people.get(0).setTalk(true);
                return true;
            }
        }
        if (timerT <= 70 * 3 && itemGiven) {
            timerT++;
            return true;
        } else {
            itemGiven = false;
            timerT = 0;
            return false;
        }
    }

    public void task2() {

    }

    public void task3() {

    }

    public void task4() {

    }
}