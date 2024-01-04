package cpen221.soundwaves;

import ca.ubc.ece.cpen221.mp1.utils.MP3Player;
import cpen221.soundwaves.soundutils.Audio;
import cpen221.soundwaves.soundutils.AudioFile;
import cpen221.soundwaves.soundutils.SoundWaveChart;

import java.util.Arrays;

/**
 * <p>Simple <code>Main</code> class to demonstrate how
 * to play and visualize sound waves.</p>
 *
 * <p>
 *     There are four examples:
 *     <ul>
 *         <li>{@link #examplePlayMP3file()};</li>
 *         <li>{@link #examplePlayWAVfile()};</li>
 *         <li>{@link #examplePlayAndDraw()};</li>
 *         <li>{@link #exampleSineWaveWithChart()}.</li>
 *     </ul>
 * </p>
 */
public class Main {

    public static void main(String[] args) {
//      examplePlayMP3file();
//      examplePlayWAVfile();
//      exampleSineWaveWithChart();
//      examplePlayAndDraw();
    }

    private static void examplePlayMP3file() {
        AudioFile af1 = new AudioFile("samples/force.mp3");

        while (!af1.isEmpty()) {
            double[] leftSamples = af1.readLeftNext();
            double[] rightSamples = af1.readRightNext();
            MP3Player.playWave(leftSamples, rightSamples);
        }
    }

    private static void examplePlayWAVfile() {
        AudioFile af1 = new AudioFile("samples/SnareDrum.wav");

        while (!af1.isEmpty()) {
            double[] leftSamples = af1.readLeftNext();
            Audio.play(leftSamples);
            double[] rightSamples = af1.readRightNext();
            Audio.play(rightSamples);
        }
    }

    private static void examplePlayAndDraw() {
        AudioFile af1 = new AudioFile("samples/pearlharbor.mp3");
        SoundWaveChart chart = new SoundWaveChart();
        while (!af1.isEmpty()) {
            double[] leftSamples = af1.readLeftNext();
            chart.updateDrawing(leftSamples);
            Audio.play(leftSamples);
            double[] rightSamples = af1.readRightNext();
            chart.updateDrawing(rightSamples);
            Audio.play(rightSamples);
        }
    }

    private static void exampleSineWaveWithChart() {
        ConcreteSoundWave sw = SinusoidalWave.getInstance(6, 0.5, 1, 10);

        SoundWaveChart chart = new SoundWaveChart();
        double[] allSamples = sw.getLeftChannel();

        // Let us play & update the chart on a per-second basis
        int duration = allSamples.length / (SoundWave.SAMPLES_PER_SECOND);
        for (int t = 0; t < duration; t++) {
            double[] samples = Arrays.copyOfRange(allSamples, t * SoundWave.SAMPLES_PER_SECOND, (t + 1) * SoundWave.SAMPLES_PER_SECOND);
            chart.updateDrawing(samples);

            try {
                Thread.sleep(1000); // Can increase if getting Index 0 out of bounds for length 0 exception
            } catch (InterruptedException e) {
                System.err.println("Delay was interrupted...");
            }
            Audio.play(samples);
        }
    }

}
