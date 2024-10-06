import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

class Music {

    private Clip c;

    public Music(String fP) {
        try {
            File file = new File(fP);
            AudioInputStream sound = AudioSystem.getAudioInputStream(file);
            c = AudioSystem.getClip();
            c.open(sound);
        } catch (Exception e) {
            System.err.println(e);;
        }
    }

    public void play() {
        c.setFramePosition(0);
        c.start();
    }

    public void stop(){
        c.stop();
    }

    public void loop() {
        c.loop(Clip.LOOP_CONTINUOUSLY);
    }
}
