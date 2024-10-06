import java.util.*;
import java.io.*;

class Story {

    public ArrayList<String> itemsNPC, itemsPlayer, itemsEnv; // used to store the items that are a) in the npcs
                                                              // inventory b) the players inventory c) the environment
    public ArrayList<Integer> taskCompleted; // stores which tasks hava been completed.
    private ArrayList<NPC> people; // stores all the npcs in the game
    private Scanner[] scanners = new Scanner[5]; //stores scanners which read in all the dialog files
    private Player p; // the player

    public Story(ArrayList<NPC> people, Player p) {
        this.p = p;
        this.people = people;
        try {
            String line;
            scanners[0] = new Scanner(new File(MonsterHunt.WORK_DIR + "\\Story\\demoDialog.txt"));
            scanners[1] = new Scanner(new File(MonsterHunt.WORK_DIR + "\\Story\\CameraWamen.txt"));
            scanners[2] = new Scanner(new File(MonsterHunt.WORK_DIR + "\\Story\\GrannyDialog.txt"));
            scanners[3] = new Scanner(new File(MonsterHunt.WORK_DIR + "\\Story\\Son.txt"));
            scanners[4] = new Scanner(new File(MonsterHunt.WORK_DIR + "\\Story\\Mom.txt"));
            //loops the number of people, each person is linked to each file
            for (int i = 0; i < people.size(); i++) {
                while (scanners[i].hasNextLine()) {//reads in every line of the file
                    line = scanners[i].nextLine();
                    people.get(i).setNpcD(line);
                }
                scanners[i].close();
            }

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void task1() {

        if (p.getK().isInteract() && (p.getxPos() < people.get(1).getxPos() + 100)
                && (p.getxPos() > people.get(1).getxPos() - 100)) {
            Object o = p.getObj("Lens");
            if (o != null) {
                LinkedList<String> s = new LinkedList<>();
                s.add("Thanks for your help/ Here are the keys to/ the house");
                people.get(1).addObject(o);
                Object obj = people.get(1).getObjList().get(0);
                p.remove(o);
                p.addObject(obj);
                // people.get(0).setNpcD(s);
                people.get(1).setNpcDPerm(s);
            }
        }
    }

    public void task2() {
        if (p.getK().isInteract() && (p.getxPos() < people.get(2).getxPos() + 100)
                && (p.getxPos() > people.get(2).getxPos() - 100)) {
            Object o = p.getObj("cane");
            if (o != null) {
                LinkedList<String> s = new LinkedList<>();
                s.add("Thanks so much lovey/ Here are the keys/ to the truck please drive/ safe");
                people.get(2).addObject(o);
                Object obj = people.get(2).getObjList().get(0);
                p.remove(o);
                p.addObject(obj);
                // people.get(0).setNpcD(s);
                people.get(2).setNpcDPerm(s);
            }
        }
    }
}