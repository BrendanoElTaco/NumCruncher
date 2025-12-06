import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class SoundManager {
    private static final float MIN_DECIBEL_LEVEL = -80.0f;

    private final String[] soundFiles;
    private final Random random = new Random();
    private boolean isMuted = false;
    private int currentVolume = 100;
    private float savedVolume = 0.0f;

    public SoundManager(String[] soundFiles) {
        this.soundFiles = soundFiles != null ? soundFiles.clone() : new String[0];
        updateVolume(100);
    }

    public void playRandomSound() {
        if (soundFiles.length == 0) {
            return;
        }
        playSound(soundFiles[random.nextInt(soundFiles.length)]);
    }

    public void playSound(String soundFileName) {
        if (soundFileName == null || soundFileName.isEmpty()) {
            return;
        }

        File soundFile = new File(soundFileName).getAbsoluteFile();
        if (!soundFile.exists() || !soundFile.isFile()) {
            System.out.println("Sound file not found: " + soundFileName);
            return;
        }

        try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile)) {
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                if (isMuted) {
                    gainControl.setValue(gainControl.getMinimum());
                } else {
                    gainControl.setValue(savedVolume);
                }
            }

            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });
            clip.start();
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            System.out.println("Error playing sound: " + e.getMessage());
        }
    }

    public void updateVolume(int sliderValue) {
        int clampedValue = Math.max(0, Math.min(100, sliderValue));
        currentVolume = clampedValue;
        savedVolume = convertSliderValueToDecibels(currentVolume);
    }

    public void muteSound() {
        isMuted = true;
    }

    public void unmuteSound() {
        isMuted = false;
    }

    public boolean isMuted() {
        return isMuted;
    }

    public int getCurrentVolume() {
        return currentVolume;
    }

    private float convertSliderValueToDecibels(int sliderValue) {
        if (sliderValue == 0) {
            return MIN_DECIBEL_LEVEL;
        }
        // Use logarithmic scaling so mid-range values are not excessively quiet
        float decibels = (float) (20 * Math.log10(sliderValue / 100.0));
        return Math.max(decibels, MIN_DECIBEL_LEVEL);
    }
}
