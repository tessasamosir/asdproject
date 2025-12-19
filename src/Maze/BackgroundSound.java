package Maze;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;

public class BackgroundSound {
    private Clip clip;

    public BackgroundSound() {
        // Constructor kosong atau inisialisasi lain
    }

    public void play(String resourcePath) {  // contoh: "/sound/filename.wav" kalau di src/sound
        try {
            // Gunakan getResourceAsStream agar bisa dari dalam JAR
            InputStream is = getClass().getResourceAsStream(resourcePath);
            if (is == null) {
                throw new RuntimeException("File audio tidak ditemukan: " + resourcePath);
            }

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(
                    new BufferedInputStream(is)
            );

            clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.loop(Clip.LOOP_CONTINUOUSLY);  // untuk background music
            clip.start();

        } catch (Exception e) {
            throw new RuntimeException("Gagal memutar audio: " + resourcePath, e);
        }
    }

    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }
}