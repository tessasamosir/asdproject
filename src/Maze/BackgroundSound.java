package Maze;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;

class BackgroundSound {
    private Clip clip;

    public void play(String path, boolean loop) {
        try {
            InputStream audioSrc = getClass().getResourceAsStream(path);
            if (audioSrc == null) {
                throw new RuntimeException("File audio tidak ditemukan: " + path);
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(
                    new BufferedInputStream(audioSrc)
            );

            clip = AudioSystem.getClip();
            clip.open(audioStream);

            if (loop) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                clip.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (clip != null) {
            clip.stop();
            clip.close();
        }
    }
}