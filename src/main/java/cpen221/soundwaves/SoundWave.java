package cpen221.soundwaves;

import cpen221.soundwaves.soundutils.FilterType;

public interface SoundWave {

    public static final int SAMPLES_PER_SECOND = 44100;

    /* ===== TASK 1 ==== */

    /**
     * Obtain the left channel for this wave.
     * Changes to the returned array should not affect this SoundWave.
     *
     * @return an array that represents the left channel for this wave.
     */
    public double[] getLeftChannel();

    /**
     * Obtain the right channel for this wave.
     * Changes to the returned array should not affect this SoundWave.
     *
     * @return an array that represents the right channel for this wave.
     */
    public double[] getRightChannel();

    /**
     * Obtain the duration of this sound wave
     *
     * @return the duration of this sound wave, in seconds
     */
    public double duration();

    /* ===== TASK 2 ==== */

    /**
     * Append a wave to this wave.
     *
     * @param lchannel
     * @param rchannel
     */
    public void append(double[] lchannel, double[] rchannel);

    /**
     * Append a wave to this wave.
     *
     * @param other the wave to append.
     */
    public void append(SoundWave other);

    /**
     * Create a new wave by adding another wave to this wave.
     * (You should also write clear specifications for this method.)
     */
    public SoundWave add(SoundWave other);

    /**
     * Create a new wave by adding an echo to this wave.
     *
     * @param delta > 0. delta, in seconds, is the time lag between this wave and
     *              the echo wave.
     * @param alpha > 0. alpha is the damping factor applied to the echo wave.
     * @return a new sound wave with an echo.
     */
    public SoundWave addEcho(double delta, double alpha);

    /**
     * Scale the amplitude of this wave by a scaling factor.
     * After scaling, the amplitude values are normalized to remain
     * between -1 and +1.
     *
     * @param scalingFactor is a value > 0.
     */
    public void scale(double scalingFactor);

    /* ===== TASK 3 ==== */

    /**
     * Determine if this wave fully contains the other sound wave as a pattern.
     *
     * @param other is the wave to search for in this wave.
     * @return true if the other wave is contained in this after amplitude scaling,
     * and false if the other wave is not contained in this with any
     * possible amplitude scaling.
     */
    public boolean contains(SoundWave other);

    /**
     * Determine the similarity between this wave and another wave.
     * The similarity metric, gamma, is the sum of squares of
     * instantaneous differences.
     *
     * @param other is not null.
     * @return the similarity between this wave and other.
     */
    public double similarity(SoundWave other);

    /* ===== TASK 4 ==== */

    /**
     * Return the frequency of the component with the greatest amplitude
     * contribution to this wave. This component is obtained by applying the
     * Discrete Fourier Transform to this wave.
     *
     * @return the frequency of the wave component of highest amplitude.
     *
     */
    public double highestAmplitudeFrequencyComponent();

    /**
     * Filter this {@code SoundWave} based on the filter type and the
     * provided frequency parameters.
     *
     * @param type the type of filter to apply
     * @param frequencies the frequencies to use for filtering
     * @return the filtered {@code SoundWave}
     */
    public SoundWave filter(FilterType type, Double... frequencies);
}
