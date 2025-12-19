package Maze;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import javax.sound.sampled.*;
import java.io.*;
import javax.sound.sampled.*;
import java.io.*;

public class BackgroundSound {
    public void play(String resourcePath, boolean loop) {
        try {
            // Cara yang benar untuk load dari resources
            InputStream audioSrc = getClass().getClassLoader().getResourceAsStream(resourcePath);
            if (audioSrc == null) {
                throw new RuntimeException("File audio tidak ditemukan: " + resourcePath);
            }

            // Buffered supaya bisa dibaca ulang kalau perlu
            BufferedInputStream bufferedIn = new BufferedInputStream(audioSrc);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);

            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);

            if (loop) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
            clip.start();

        } catch (Exception e) {
            throw new RuntimeException("Error playing sound: " + e.getMessage(), e);
        }
    }
}