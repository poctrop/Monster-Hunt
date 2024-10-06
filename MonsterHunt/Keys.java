import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class Keys implements KeyListener {// listens for input from the keyboard

    private boolean upP = false, downP = false, leftP = false, rightP = false, interact = false;
    private boolean firstPress = true;
    private int times = 0;

    public void setTimes(int times) {
        this.times = times;
    }

    public int getTimes() {
        return times;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        // S = down, A = left, W = UP, checks if each of these keys are clicked if they
        // are the player will move in those directions

        if (keyCode == KeyEvent.VK_UP) {
            upP = true;
            if (Manager.MAPNUM != 4 && firstPress) {
                Player.jump.play();
            }
        }
        if (keyCode == KeyEvent.VK_LEFT) {

            leftP = true;
        }
        if (keyCode == KeyEvent.VK_RIGHT) {
            rightP = true;
        }
        if (keyCode == KeyEvent.VK_C) {
            interact = true;
        }
        if (Manager.MAPNUM == 4) {
            if (keyCode == KeyEvent.VK_DOWN) {
                downP = true;
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        // S = down, A = left, W = UP, checks if each of these keys are released if they
        // are the player will stop move in those directions
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_ESCAPE) {
            MonsterHunt.cl.show(MonsterHunt.control, "3");
        }
        if (Manager.MAPNUM == 4) {
            if (keyCode == KeyEvent.VK_UP) {
                upP = false;
            }
            if (keyCode == KeyEvent.VK_DOWN) {
                downP = false;
            }
        }
        if (keyCode == KeyEvent.VK_LEFT) {
            leftP = false;
        }
        if (keyCode == KeyEvent.VK_RIGHT) {
            rightP = false;
        }

        if (keyCode == KeyEvent.VK_C) {
            interact = false;

        }

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
    // getters

    public boolean isInteract() {
        return interact;
    }

    public boolean isUpP() {
        return upP;
    }

    public boolean isDownP() {
        return downP;
    }

    public boolean isLeftP() {
        return leftP;
    }

    public boolean isRightP() {
        return rightP;
    }
    // setters

    public void setUpP(boolean upP) {
        this.upP = upP;
    }

    public void setKeys() {
        upP = false;
        downP = false;
        leftP = false;
        rightP = false;
        interact = false;
    }

    public void setInteract(boolean interact) {
        this.interact = interact;
    }

}