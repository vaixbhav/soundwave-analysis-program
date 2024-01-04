package cpen221.soundwaves;

// See: https://en.wikipedia.org/wiki/Smoke_testing_(software)

import org.junit.jupiter.api.Test;

import cpen221.soundwaves.soundutils.FilterType;


import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class SmokeTests {

    @Test
    public void testCreateWave() {
        double[] lchannel = {1.0, -1.0};
        double[] rchannel = {1.0, -1.0};
        SoundWave wave = new ConcreteSoundWave(lchannel, rchannel);
        double[] lchannel1 = wave.getLeftChannel();
        assertArrayEquals(lchannel, lchannel1, 0.00001);
        double[] rchannel1 = wave.getRightChannel();
        assertArrayEquals(rchannel, rchannel1, 0.00001);
    }

    /**
     * SoundWave(double[] lchannel, double[] rchannel)
     * Test when entries of lchannel and rchannel are all zero.
     */
    @Test
    public void testCreateWaveZero() {
        double[] leftChannel = {0.0, 0.0, 0.0, 0.0, 0.0};
        double[] rightChannel = {0.0, 0.0, 0.0, 0.0, 0.0};
        SoundWave wave = new ConcreteSoundWave(leftChannel, rightChannel);
        double[] leftChannel1 = wave.getLeftChannel();
        assertArrayEquals(leftChannel, leftChannel1, 0.00001);
        double[] rightChannel1 = wave.getRightChannel();
        assertArrayEquals(rightChannel, rightChannel1, 0.00001);
    }

    /**
     * SoundWave(double freq, double phase, double amplitude, double duration)
     */
    @Test
    public void testCreateSineWave() {
        double freq = 400.0;
        double phase = 0.0;
        double amplitude = 1.0;
        double duration = 4.0 / 44100.0;
        SoundWave sw1 = SinusoidalWave.getInstance(freq, phase, amplitude, duration);
        double[] left = {0, 0.056959498116, 0.113734047592, 0.17013930031};
        double[] right = {0, 0.056959498116, 0.113734047592, 0.17013930031};
        assertArrayEquals(left, sw1.getLeftChannel(), 0.00001);
        assertArrayEquals(right, sw1.getRightChannel(), 0.00001);
    }

    /**
     * append(double[] leftChannel, double[] rightChannel)
     * Test when both leftChannel and rightChannel are not empty. The wave is not empty too.
     * Left and right channels have different values to test more values.
     */
    @Test
    public void testAppend() {
        double[] leftChannel = {0.5, 0.3, -0.2};
        double[] rightChannel = {0.3, 0.4, 0.5};
        double[] addLeft = {0.9, 0.9};
        double[] addRight = {0.1, -0.5};
        double[] leftAfter = {0.5, 0.3, -0.2, 0.9, 0.9};
        double[] rightAfter = {0.3, 0.4, 0.5, 0.1, -0.5};
        SoundWave sw1 = new ConcreteSoundWave(leftChannel, rightChannel);
        sw1.append(addLeft, addRight);
        assertArrayEquals(leftAfter, sw1.getLeftChannel(), 0.0001);
        assertArrayEquals(rightAfter, sw1.getRightChannel(), 0.0001);
    }

    /**
     * SoundWave add(SoundWave other)
     * Create a new wave by adding another wave to this wave.
     * Wave is then normalized to [-1,1].
     * The length of the channels in both waves are the same.
     */
    @Test
    public void testAdd() {
        double[] left = {0.7, -0.3, 0.1, -0.9};
        double[] right = {0.7, -0.3, 0.1, -0.9};
        double[] otherLeft = {0.5, 0.7, -0.5, -0.5};
        double[] otherRight = {0.5, 0.7, -0.5, -0.5};
        double[] resultLeft = {1.2/1.4, 0.4/1.4, -0.4/1.4, -1.4/1.4};
        double[] resultRight = {1.2/1.4, 0.4/1.4, -0.4/1.4, -1.4/1.4};
        SoundWave wave = new ConcreteSoundWave(left, right);
        SoundWave other = new ConcreteSoundWave(otherLeft, otherRight);
        SoundWave result = wave.add(other);
        assertArrayEquals(resultLeft, result.getLeftChannel(), 0.00001);
        assertArrayEquals(resultRight, result.getRightChannel(), 0.00001);
    }

    /**
     * contains(SoundWave other)
     * Determine if this wave fully contains the other sound wave as a pattern.
     * Both the waves are not empty and have same length.
     * Wave 2 occurs in wave 1 with amplitude scaling of 2.
     */
    @Test
    public void testContains() {
        double[] left = {0.5, 0.5, 0.5};
        double[] right = {0.5, 0.5, 0.5};
        double[] otherLeft = {0.25, 0.25, 0.25};
        double[] otherRight = {0.25, 0.25, 0.25};
        SoundWave wave1 = new ConcreteSoundWave(left, right);
        SoundWave wave2 = new ConcreteSoundWave(otherLeft, otherRight);
        assertTrue(wave1.contains(wave2));
    }

    /**
     * similarity(SoundWave other)
     * Test similarity between two non-zero waves. Both waves have the same length.
     * sw2 is identical to sw1 after amplitude scaling by factor of 2.
     */
    @Test
    public void testSimilarity() {
        double[] left1 = {0.7, 0.5, 0.9, -0.7};
        double[] right1 = {0.7, 0.5, 0.9, -0.7};
        double[] left2 = {0.35, 0.25, 0.45, -0.35};
        double[] right2 = {0.35, 0.25, 0.45, -0.35};
        SoundWave sw1 = new ConcreteSoundWave(left1, right1);
        SoundWave sw2 = new ConcreteSoundWave(left2, right2);
        assertEquals((sw1.similarity(sw2)), 1.0, 0.000001);
        /* Test for symmetry. */
        assertEquals((sw2.similarity(sw1)), 1.0, 0.000001);
    }




    /*
    @Test
    public void testSimilarSounds() {
        var sounds = new ArrayList<SoundWave>();
        for (var i = 0; i < 5; i++) {
            var l = randomSignal();
            var r = randomSignal();
            sounds.add(new ConcreteSoundWave(l, r));

            sounds.add(new ConcreteSoundWave(randomNoise(l), randomNoise(r)));
            sounds.add(new ConcreteSoundWave(randomNoise(l), randomNoise(r)));
        }

        var set = new HashSet<>(sounds);

        //Verify the set we get back are the original three we added noise to.//
        var firstSet = new SoundWaveSimilarity().getSimilarSounds(set, 5, sounds.get(0));
        assert firstSet != null;
        assertTrue(firstSet.containsAll(sounds.subList(0, 3)));
    }
    */

    /**
     * Generate a random signal of length 100.
     *
     */
    private double[] randomSignal() {
        return new Random().doubles(100).map(x -> -1.0 + (x * 2)).toArray();
    }

    /**
     * Corrupt a signal slightly, adding +/- 0.1 to every sample.
     */
    private double[] randomNoise(double[] original) {
        var rand = new Random();
        return Arrays.stream(original).map(x -> x + rand.nextDouble() * 0.2 - 0.1).toArray();
    }
}
