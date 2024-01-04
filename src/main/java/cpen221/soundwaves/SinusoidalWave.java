package cpen221.soundwaves;


public class SinusoidalWave extends ConcreteSoundWave {

    // Abstraction Function:
    //  represents an implementation of a sound wave given features of
    //  frequency, amplitude, phase, and duration. Abstract properties defined
    //  by ConcreteSoundWave are extended.

    /**
     * A private constructor.
     *
     * @param channel the time series of amplitude values, is not null
     */
    private SinusoidalWave(double[] channel) {
        super(channel, channel);
    }

    /**
     * Obtain a new {@code SinusoidalWave} instance.
     *
     * @param freq      the frequency of the wave, > 0
     * @param phase     the phase of the wave, >= 0
     * @param amplitude the amplitude of the wave, is in (0, 1]
     * @param duration  the duration of the wave, >= 0
     * @return a {@code SinusoidalWave} instance with the specified parameters
     */
    public static SinusoidalWave getInstance(double freq, double phase, double amplitude,
                                             double duration) {

        int numSamples = (int) (duration * SAMPLES_PER_SECOND);
        double timeStep = duration / numSamples;
        double angularFreq = 2.0 * Math.PI * freq;

        double[] channel = new double[numSamples];

        for (int i = 0; i < numSamples; i++) {
            double time = i * timeStep;
            double amplitudeValue = amplitude * Math.sin(angularFreq * time + phase);
            channel[i] = amplitudeValue;
        }

        return new SinusoidalWave(channel);
    }

}
