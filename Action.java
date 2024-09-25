import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


class Action implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) { //checks if the player choses to go to the home screen
        if (e.getSource() == PauseScreen.home) {
            MonsterHunt.cl.show(MonsterHunt.control, "1");//Switches to the home screen panel
            Game.paused = true;
 
        }
        if (e.getSource() == PauseScreen.cont) {//checks if the player clicked to continut the game
            MonsterHunt.cl.show(MonsterHunt.control, "4");//the panel switches back to the game panel
        } 
    }

} 