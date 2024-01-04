package cpen221.soundwaves;


import org.junit.jupiter.api.Test;

import cpen221.soundwaves.soundutils.FilterType;
import static org.junit.jupiter.api.Assertions.*;

public class ComprehensiveTests {

    @Test
    public void testCreateSineWaveZeroDuration() {
        double freq = 400.0;
        double phase = 0.0;
        double amplitude = 1.0;
        double duration = 0.0 / 44100.0;
        SoundWave sw1 = SinusoidalWave.getInstance(freq, phase, amplitude, duration);
        double[] left = {};
        double[] right = {};
        assertArrayEquals(left, sw1.getLeftChannel(), 0.00001);
        assertArrayEquals(right, sw1.getRightChannel(), 0.00001);
    }

    @Test
    public void testCreateSineWavePhase2PI() {
        double freq = 400.0;
        double phase = 2 * Math.PI;
        double amplitude = 1.0;
        double duration = 4.0 / 44100.0;
        SoundWave sw1 = SinusoidalWave.getInstance(freq, phase, amplitude, duration);
        double[] left = {0, 0.056959498116, 0.113734047592, 0.17013930031};
        double[] right = {0, 0.056959498116, 0.113734047592, 0.17013930031};
        assertArrayEquals(left, sw1.getLeftChannel(), 0.00001);
        assertArrayEquals(right, sw1.getRightChannel(), 0.00001);
        assertEquals(duration, sw1.duration(), 0.00001);
    }

    @Test
    public void testCreateSineWavePhasePI() {
        double freq = 400.0;
        double phase = Math.PI;
        double amplitude = 1.0;
        double duration = 4.0 / 44100.0;
        SoundWave sw1 = SinusoidalWave.getInstance(freq, phase, amplitude, duration);
        double[] left = {0, -0.056959498116, -0.113734047592, -0.17013930031};
        double[] right = {0, -0.056959498116, -0.113734047592, -0.17013930031};
        assertArrayEquals(left, sw1.getLeftChannel(), 0.00001);
        assertArrayEquals(right, sw1.getRightChannel(), 0.00001);
        assertEquals(duration, sw1.duration(), 0.00001);
    }

    @Test
    public void testCreateSquareWave() {
        double freq = 400.0;
        double phase = 0.0;
        double amplitude = 1.0;
        double duration = 4.0 / 44100.0;
        SoundWave sw1 = SquareWave.getInstance(freq, phase, amplitude, duration);
        double[] left = {0, Math.signum(800.0*Math.PI*(1.0/44100.0)), Math.signum(800.0*Math.PI*(2.0/44100.0)), Math.signum(800.0*Math.PI*(3.0/44100.0))};
        double[] right = {0, Math.signum(800.0*Math.PI*(1.0/44100.0)), Math.signum(800.0*Math.PI*(2.0/44100.0)), Math.signum(800.0*Math.PI*(3.0/44100.0))};
        assertArrayEquals(left, sw1.getLeftChannel(), 0.00001);
        assertArrayEquals(right, sw1.getRightChannel(), 0.00001);
    }

    @Test
    public void testCreateTriangleWave() {
        double freq = 400.0;
        double phase = 0.0;
        double amplitude = 1.0;
        double duration = 4.0 / 44100.0;
        SoundWave sw1 = TriangleWave.getInstance(freq, phase, amplitude, duration);
        double[] left = {0, 0.113980685844527, 0.227961371689055, 0.341942057533582};
        double[] right = {0, 0.113980685844527, 0.227961371689055, 0.341942057533582};
        assertArrayEquals(left, sw1.getLeftChannel(), 0.00001);
        assertArrayEquals(right, sw1.getRightChannel(), 0.00001);
    }

    @Test
    public void testEchoPositiveDelta(){
        double[] lchannel = {1.0, 1.0, 1.0};
        double[] rchannel = {1.0, 1.0, 1.0};
        double delta = 1.0;
        double alpha = 0.5;
        double[] leftAfter = {1.0 / 1.5, 1.0, 1.0, 0.5 / 1.5};
        double[] rightAfter = {1.0 / 1.5, 1.0, 1.0, 0.5 / 1.5};
        SoundWave sw1 = new ConcreteSoundWave(lchannel, rchannel);
        sw1 = sw1.addEcho(delta, alpha);
        assertArrayEquals(leftAfter, sw1.getLeftChannel(), 0.0001);
        assertArrayEquals(rightAfter, sw1.getRightChannel(), 0.0001);
    }

    @Test
    public void testEchoNegativeDelta(){
        double[] lchannel = {1.0, 1.0, 1.0};
        double[] rchannel = {1.0, 1.0, 1.0};
        double delta = -1.0;
        double alpha = 0.5;
        double[] leftAfter = {1.0, 1.0, 1.0 / 1.5};
        double[] rightAfter = {1.0, 1.0, 1.0 / 1.5};
        SoundWave sw1 = new ConcreteSoundWave(lchannel, rchannel);
        sw1 = sw1.addEcho(delta, alpha);
        assertArrayEquals(leftAfter, sw1.getLeftChannel(), 0.0001);
        assertArrayEquals(rightAfter, sw1.getRightChannel(), 0.0001);
    }

    @Test
    public void testAppendEmptyToEmptyWave(){
        double[] lchannel = {};
        double[] rchannel = {};
        double[] addLeft = {};
        double[] addRight = {};
        double[] leftAfter = {};
        double[] rightAfter = {};
        SoundWave sw1 = new ConcreteSoundWave(lchannel, rchannel);
        SoundWave sw2 = new ConcreteSoundWave(addLeft, addRight);
        sw1.append(sw2);
        assertArrayEquals(leftAfter, sw1.getLeftChannel(), 0.0001);
        assertArrayEquals(rightAfter, sw1.getRightChannel(), 0.0001);
    }

    @Test
    public void testAppendEmptyToEmpty(){
        double[] lchannel = {};
        double[] rchannel = {};
        double[] addLeft = {};
        double[] addRight = {};
        double[] leftAfter = {};
        double[] rightAfter = {};
        SoundWave sw1 = new ConcreteSoundWave(lchannel, rchannel);
        sw1.append(addLeft, addRight);
        assertArrayEquals(leftAfter, sw1.getLeftChannel(), 0.0001);
        assertArrayEquals(rightAfter, sw1.getRightChannel(), 0.0001);
    }

    @Test
    public void testAddEmptyToEmpty() {
        double[] left = {};
        double[] right = {};
        double[] otherLeft = {};
        double[] otherRight = {};
        double[] resultLeft = {};
        double[] resultRight = {};
        SoundWave wave = new ConcreteSoundWave(left, right);
        SoundWave other = new ConcreteSoundWave(otherLeft, otherRight);
        SoundWave result = wave.add(other);
        assertArrayEquals(resultLeft, result.getLeftChannel(), 0.00001);
        assertArrayEquals(resultRight, result.getRightChannel(), 0.00001);
    }

    @Test
    public void testAddSmallerWave() {
        double[] left = {0.2, 0.6, -0.7, 0.2};
        double[] right = {0.2, 0.6, -0.7, 0.2};
        double[] otherLeft = {0.3, 0.5};
        double[] otherRight = {0.3, 0.5};
        double[] resultLeft = {0.5/1.1, 1.0, -0.7/1.1, 0.2/1.1};
        double[] resultRight = {0.5/1.1, 1.0, -0.7/1.1, 0.2/1.1};
        SoundWave wave = new ConcreteSoundWave(left, right);
        SoundWave other = new ConcreteSoundWave(otherLeft, otherRight);
        SoundWave result = wave.add(other);
        assertArrayEquals(resultLeft, result.getLeftChannel(), 0.00001);
        assertArrayEquals(resultRight, result.getRightChannel(), 0.00001);
    }

    @Test
    public void testAddBiggerWaveNoNormalize() {
        double[] left = {0.5, 0.3, 0.2};
        double[] right = {0.5, 0.3, 0.2};
        double[] otherLeft = {0.4, 0.4, 0.3, 0.1};
        double[] otherRight = {0.4, 0.4, 0.3, 0.1};
        double[] resultLeft = {0.9, 0.7, 0.5, 0.1};
        double[] resultRight = {0.9, 0.7, 0.5, 0.1};
        SoundWave wave = new ConcreteSoundWave(left, right);
        SoundWave other = new ConcreteSoundWave(otherLeft, otherRight);
        SoundWave result = wave.add(other);
        assertArrayEquals(resultLeft, result.getLeftChannel(), 0.00001);
        assertArrayEquals(resultRight, result.getRightChannel(), 0.00001);
    }

    @Test
    public void testScaleEmpty(){
        double[] left = {};
        double[] right = {};
        double scalingFactor = 0.0;
        double[] resultLeft = {};
        double[] resultRight = {};
        SoundWave wave = new ConcreteSoundWave(left, right);
        wave.scale(scalingFactor);
        assertArrayEquals(resultLeft, wave.getLeftChannel(), 0.00001);
        assertArrayEquals(resultRight, wave.getRightChannel(), 0.00001);
    }

    @Test
    public void testScaleNoNormalize() {
        double[] left = {0.5, 0.4, 0.2};
        double[] right = {0.5, 0.4, 0.2};
        double scalingFactor = 2.0;
        double[] resultLeft = {1.0, 0.8, 0.4};
        double[] resultRight = {1.0, 0.8, 0.4};
        SoundWave wave = new ConcreteSoundWave(left, right);
        wave.scale(scalingFactor);
        assertArrayEquals(resultLeft, wave.getLeftChannel(), 0.00001);
        assertArrayEquals(resultRight, wave.getRightChannel(), 0.00001);
    }

    @Test
    public void testScaleNormalize() {
        double[] left = {0.5, 0.4, 0.2};
        double[] right = {0.7, 0.4, 0.8};
        double scalingFactor = 3.0;
        double[] resultLeft = {1.0, 0.8, 0.4};
        double[] resultRight = {2.1/2.4, 1.2/2.4, 1.0};
        SoundWave wave = new ConcreteSoundWave(left, right);
        wave.scale(scalingFactor);
        assertArrayEquals(resultLeft, wave.getLeftChannel(), 0.00001);
        assertArrayEquals(resultRight, wave.getRightChannel(), 0.00001);
    }

    @Test
    public void testFilterExceptions(){
        double[] lchannel = {0.3, 0.4, 0.6};
        double[] rchannel = {0.3, 0.4, 0.6};
        ConcreteSoundWave sw = new ConcreteSoundWave(lchannel, rchannel);

        try {
            sw.filter(FilterType.BANDPASS, 0.2, 0.4, 0.5);
            fail("Exception expected!");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        try {
            sw.filter(FilterType.LOWPASS, 0.2, 0.4);
            fail("Exception expected!");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        try {
            sw.filter(FilterType.BANDPASS, 0.2);
            fail("Exception expected!");
        }
        catch (IllegalArgumentException noe) {
            assertTrue(true);
        }

        try {
            sw.filter(FilterType.HIGHPASS, 0.2, 0.4);
            fail("Exception expected!");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

    }

    @Test
    public void testFilter(){
        double[] lchannel = {0.3, 0.4, 0.6};
        double[] rchannel = {0.3, 0.4, 0.6};
        double[] rightAfterLow = {0.2645751311064591, 0.26457513110645914};
        double[] rightAfterBand = {0.2645751311064591, 0.26457513110645914};
        double[] rightAfterHigh = {1.2999999999999998};
        double[] leftAfterLow = {0.2645751311064591, 0.26457513110645914};
        double[] leftAfterBand = {0.2645751311064591, 0.26457513110645914};
        double[] leftAfterHigh = {1.2999999999999998};

        ConcreteSoundWave wave = new ConcreteSoundWave(lchannel, rchannel);

        SoundWave sw1= wave.filter(FilterType.LOWPASS, 0.5);
        assertArrayEquals(leftAfterLow, sw1.getLeftChannel());
        assertArrayEquals(rightAfterLow, sw1.getRightChannel());

        SoundWave sw2 = wave.filter(FilterType.BANDPASS, 0.2, 0.6);
        assertArrayEquals(leftAfterBand, sw2.getLeftChannel());
        assertArrayEquals(rightAfterBand, sw2.getRightChannel());

        SoundWave sw3 = wave.filter(FilterType.HIGHPASS, 0.6);
        assertArrayEquals(leftAfterHigh, sw3.getLeftChannel());
        assertArrayEquals(rightAfterHigh, sw3.getRightChannel());
    }

    @Test
    public void testFilterNoFilters(){
        double[] lchannel = {10., 1.0, 1.0};
        double[] rchannel = {0.3, 0.4, 0.6};
        ConcreteSoundWave sw1 = new ConcreteSoundWave(lchannel, rchannel);

        SoundWave sw2 = sw1.filter(FilterType.LOWPASS);
        assertArrayEquals(sw1.getLeftChannel(), sw2.getLeftChannel());
        assertArrayEquals(sw1.getRightChannel(), sw2.getRightChannel());
    }

    @Test
    public void testFilterEmpty(){
        double[] lchannel = {};
        double[] rchannel = {};
        ConcreteSoundWave wave = new ConcreteSoundWave(lchannel, rchannel);

        SoundWave sw1= wave.filter(FilterType.LOWPASS, 0.4);
        assertArrayEquals(lchannel, sw1.getLeftChannel());
        assertArrayEquals(rchannel, sw1.getRightChannel());

        SoundWave sw2 = wave.filter(FilterType.BANDPASS, 0.4, 0.6);
        assertArrayEquals(lchannel, sw2.getLeftChannel());
        assertArrayEquals(rchannel, sw2.getRightChannel());

        SoundWave sw3 = wave.filter(FilterType.HIGHPASS, 0.6);
        assertArrayEquals(lchannel, sw3.getLeftChannel());
        assertArrayEquals(rchannel, sw3.getRightChannel());
    }

    @Test
    public void testEmptyContainsEmpty() {
        double[] left = {};
        double[] right = {};
        double[] otherLeft = {};
        double[] otherRight = {};
        SoundWave wave1 = new ConcreteSoundWave(left, right);
        SoundWave wave2 = new ConcreteSoundWave(otherLeft, otherRight);
        assertTrue(wave1.contains(wave2));
    }

    @Test
    public void testContainsBiggerWave() {
        double[] left = {};
        double[] right = {};
        double[] otherLeft = {1.0};
        double[] otherRight = {1.0};
        SoundWave wave1 = new ConcreteSoundWave(left, right);
        SoundWave wave2 = new ConcreteSoundWave(otherLeft, otherRight);
        assertFalse(wave1.contains(wave2));
    }

    @Test
    public void testContainsOutOfPhase() {
        double[] left = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] right = {3.0, 4.0, 5.0, 6.0, 7.0};
        double[] otherLeft = {4.0, 5.0};
        double[] otherRight = {4.0, 5.0};
        SoundWave wave1 = new ConcreteSoundWave(left, right);
        SoundWave wave2 = new ConcreteSoundWave(otherLeft, otherRight);
        assertFalse(wave1.contains(wave2));
    }

    @Test 
    public void testHighestAmplitudeFrequencyComponent(){
        double[] lchannel = {0.3, 0.4, 0.6};
        double[] rchannel = {0.3, 0.4, 0.6};
        ConcreteSoundWave wave = new ConcreteSoundWave(lchannel, rchannel);
        double actual = wave.highestAmplitudeFrequencyComponent();
        assertEquals(1.29, actual, 0.01);
    }

    @Test
    public void testHighestAmplitudeFrequencyComponent2(){
        double[] lchannel = {0.9, 0.73, 0.41};
        double[] rchannel = {0.88, 0.53, 0.61};
        ConcreteSoundWave wave = new ConcreteSoundWave(lchannel, rchannel);
        double actual = wave.highestAmplitudeFrequencyComponent(); 
        assertEquals(2.04, actual, 0.01);
    }

    @Test
    public void testhighestAmplitudeFrequencyComponent3(){
        double[] lchannel = {};
        double[] rchannel = {};
        ConcreteSoundWave wave = new ConcreteSoundWave(lchannel, rchannel);
        double actual = wave.highestAmplitudeFrequencyComponent(); 
        assertEquals(0.0, actual, 0.01);
    }

    @Test
    public void testSimilarityEmptyWave() {
        double[] left1 = {};
        double[] right1 = {};
        double[] left2 = {};
        double[] right2 = {};
        SoundWave sw1 = new ConcreteSoundWave(left1, right1);
        SoundWave sw2 = new ConcreteSoundWave(left2, right2);
        assertEquals((sw1.similarity(sw2)), 1.0, 0.000001);
        /* Test for symmetry. */
        assertEquals((sw2.similarity(sw1)), 1.0, 0.000001);
    }

    @Test
    public void testSimilarity() {
        double[] left1 = {2.0, 3.0, 4.0};
        double[] right1 = {2.0, 3.0, 4.0};
        double[] left2 = {2.0, 6.0, 7.0, 8.0};
        double[] right2 = {2.0, 6.0, 7.0, 8.0};
        SoundWave sw1 = new ConcreteSoundWave(left1, right1);
        SoundWave sw2 = new ConcreteSoundWave(left2, right2);
        assertEquals((sw1.similarity(sw2)), 0.022709570331083558, 0.000001);
        /* Test for symmetry. */
        assertEquals((sw2.similarity(sw1)), 0.022709570331083558, 0.000001);
    }

    @Test
    public void testMP3Wave() {
        String fileName = "samples/force.mp3"; // Replace this with the path to your test MP3 file

        // Act
        MP3Wave mp3Wave = MP3Wave.getInstance(fileName);

        // Assert
        assertNotNull(mp3Wave);
        assertNotNull(mp3Wave.getLeftChannel());
        assertNotNull(mp3Wave.getRightChannel());

        // Check if channels are not empty
        assertTrue(mp3Wave.getLeftChannel().length > 0);
        assertTrue(mp3Wave.getRightChannel().length > 0);
    }



}
