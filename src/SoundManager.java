import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.*;

public final class SoundManager {
    private static final Logger LOGGER = Logger.getLogger(SoundManager.class.getName());
    private static final float MIN_DECIBEL_LEVEL = -80.0f;

    private final String[] soundFiles;
    private final Random random = new Random();
    private boolean isMuted = false;
    private int currentVolume = 100;
    private float savedVolume = convertSliderValueToDecibels(currentVolume);

    public SoundManager(String[] soundFiles) {
        this.soundFiles = soundFiles != null ? soundFiles.clone() : new String[0];
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
            LOGGER.warning(() -> "Sound file not found: " + soundFileName);
            return;
        }

        Clip clip = null;
        try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile)) {
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            applyVolume(clip);

            Clip playbackClip = clip;
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    playbackClip.close();
                }
            });
            clip.start();
            clip = null;
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException | IllegalArgumentException e) {
            LOGGER.log(Level.WARNING, "Error playing sound: " + soundFileName, e);
        } finally {
            if (clip != null) {
                clip.close();
            }
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

    private void applyVolume(Clip clip) {
        if (!clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            return;
        }

        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float gainValue = isMuted
                ? gainControl.getMinimum()
                : clamp(savedVolume, gainControl.getMinimum(), gainControl.getMaximum());
        gainControl.setValue(gainValue);
    }

    private static float convertSliderValueToDecibels(int sliderValue) {
        if (sliderValue == 0) {
            return MIN_DECIBEL_LEVEL;
        }
        // Use logarithmic scaling so mid-range values are not excessively quiet
        float decibels = (float) (20 * Math.log10(sliderValue / 100.0));
        return Math.max(decibels, MIN_DECIBEL_LEVEL);
    }

    private static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }
}
