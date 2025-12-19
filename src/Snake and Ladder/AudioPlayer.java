import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Random;

class AudioPlayer {
    private static Clip backgroundClip;
    private static boolean isMusicPlaying = false;

    private static final String BACKGROUND_MUSIC_PATH = "sound/Kristen Bell, Idina Menzel - For the First Time in Forever (From Frozen_Sing-Along) (1).wav";
    private static final String DICE_SOUND_PATH = "sound/ytmp3free.cc_dice-sound-effect-youtubemp3free.org.wav";
    private static final String SUCCESS_SOUND_PATH = "sound/ytmp3free.cc_success-sound-effect-youtubemp3free.org.wav";
    private static final String WIN_SOUND_PATH = "sound/ytmp3free.cc_victory-sound-effect-youtubemp3free.org.wav";

    public static void playBackgroundMusic() {
        try {
            if (isMusicPlaying) return;

            File soundFile = new File(BACKGROUND_MUSIC_PATH);
            if (soundFile.exists()) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
                backgroundClip = AudioSystem.getClip();
                backgroundClip.open(audioStream);
                backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
                backgroundClip.start();
                isMusicPlaying = true;
                return;
            }

            playGeneratedBackgroundMusic();

        } catch (Exception e) {
            playGeneratedBackgroundMusic();
        }
    }

    private static void playGeneratedBackgroundMusic() {
        try {
            float sampleRate = 44100;
            int duration = 10;
            int numSamples = (int)(duration * sampleRate);
            byte[] buffer = new byte[numSamples * 2];

            double[] notes = {261.63, 293.66, 329.63, 349.23, 392.00, 440.00, 493.88};
            int samplesPerNote = numSamples / 14;
            int idx = 0;

            for (int i = 0; i < numSamples; i++) {
                if (i % samplesPerNote == 0) idx++;

                double freq = notes[idx % notes.length];
                double angle = 2 * Math.PI * i * freq / sampleRate;
                short sample = (short)(Math.sin(angle) * 3000);

                buffer[i * 2] = (byte)(sample & 0xFF);
                buffer[i * 2 + 1] = (byte)((sample >> 8) & 0xFF);
            }

            AudioFormat format = new AudioFormat(sampleRate, 16, 1, true, false);
            Clip clip = AudioSystem.getClip();
            clip.open(new AudioInputStream(new ByteArrayInputStream(buffer), format, numSamples));
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
            backgroundClip = clip;
            isMusicPlaying = true;

        } catch (Exception e) {
            System.out.println("Audio gagal dibuat: " + e.getMessage());
        }
    }

    public static void stopBackgroundMusic() {
        if (backgroundClip != null) {
            backgroundClip.stop();
            backgroundClip.close();
            isMusicPlaying = false;
        }
    }

    private static void playSoundEffect(String filePath, Runnable fallback) {
        new Thread(() -> {
            try {
                File f = new File(filePath);
                if (f.exists()) {
                    AudioInputStream ais = AudioSystem.getAudioInputStream(f);
                    Clip clip = AudioSystem.getClip();
                    clip.open(ais);
                    clip.start();
                    Thread.sleep(clip.getMicrosecondLength() / 1000);
                    return;
                }
            } catch (Exception ignored) {}

            fallback.run();
        }).start();
    }

    public static void playDiceSound() {
        playSoundEffect(DICE_SOUND_PATH, AudioPlayer::generateDiceSound);
    }

    public static void playSuccessSound() {
        playSoundEffect(SUCCESS_SOUND_PATH, AudioPlayer::generateSuccessSound);
    }

    public static void playWinSound() {
        playSoundEffect(WIN_SOUND_PATH, AudioPlayer::generateWinSound);
    }

    private static void generateDiceSound() {
        try {
            float sampleRate = 44100;
            int duration = 200;
            int numSamples = duration * 44;
            byte[] buffer = new byte[numSamples * 2];
            Random r = new Random();

            for (int i = 0; i < numSamples; i++) {
                short sample = (short)(r.nextInt(6000) - 3000);
                buffer[i * 2] = (byte)(sample & 0xFF);
                buffer[i * 2 + 1] = (byte)((sample >> 8) & 0xFF);
            }

            AudioFormat format = new AudioFormat(sampleRate, 16, 1, true, false);
            Clip clip = AudioSystem.getClip();
            clip.open(new AudioInputStream(new ByteArrayInputStream(buffer), format, numSamples));
            clip.start();
            Thread.sleep(220);
            clip.close();
        } catch (Exception ignored) {}
    }

    private static void generateSuccessSound() {
        try {
            float sampleRate = 44100;
            int duration = 500;
            int numSamples = duration * 44;
            byte[] buffer = new byte[numSamples * 2];

            for (int i = 0; i < numSamples; i++) {
                double freq = 440 + (i * 440.0 / numSamples);
                double angle = 2 * Math.PI * i * freq / sampleRate;
                short sample = (short)(Math.sin(angle) * (5000 * (1.0 - (double)i / numSamples)));

                buffer[i * 2] = (byte)(sample & 0xFF);
                buffer[i * 2 + 1] = (byte)((sample >> 8) & 0xFF);
            }

            AudioFormat format = new AudioFormat(sampleRate, 16, 1, true, false);
            Clip clip = AudioSystem.getClip();
            clip.open(new AudioInputStream(new ByteArrayInputStream(buffer), format, numSamples));
            clip.start();
            Thread.sleep(550);
            clip.close();
        } catch (Exception ignored) {}
    }

    private static void generateWinSound() {
        try {
            float sampleRate = 44100;
            int duration = 1000;
            int numSamples = duration * 44;
            byte[] buffer = new byte[numSamples * 2];

            double[] melody = {523.25, 587.33, 659.25, 783.99};
            int samplesPerNote = numSamples / melody.length;

            for (int i = 0; i < numSamples; i++) {
                double freq = melody[Math.min(i / samplesPerNote, melody.length - 1)];
                double angle = 2 * Math.PI * i * freq / sampleRate;
                short sample = (short)(Math.sin(angle) * (8000 * (1.0 - (double)i / numSamples)));

                buffer[i * 2] = (byte)(sample & 0xFF);
                buffer[i * 2 + 1] = (byte)((sample >> 8) & 0xFF);
            }

            AudioFormat format = new AudioFormat(sampleRate, 16, 1, true, false);
            Clip clip = AudioSystem.getClip();
            clip.open(new AudioInputStream(new ByteArrayInputStream(buffer), format, numSamples));
            clip.start();
            Thread.sleep(1100);
            clip.close();

        } catch (Exception ignored) {}
    }
}