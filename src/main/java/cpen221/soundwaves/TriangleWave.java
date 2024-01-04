package cpen221.soundwaves;

public class TriangleWave extends ConcreteSoundWave {

    // Abstraction Function:
    //  represents an implementation of a sound wave given features of
    //  frequency, amplitude, phase, and duration. Abstract properties defined
    //  by ConcreteSoundWave are extended.

    /**
     * A private constructor.
     *
     * @param channel the time series of amplitude values, is not null
     */
    private TriangleWave(double[] channel) {
        super(channel, channel);
    }

    /**
     * Obtain a new {@code TriangleWave} instance.
     *
     * @param freq      the frequency of the wave, > 0
     * @param phase     the phase of the wave, >= 0
     * @param amplitude the amplitude of the wave, is in (0, 1]
     * @param duration  the duration of the wave, >= 0
     * @return a {@code TriangleWave} instance with the specified parameters
     */
    public static TriangleWave getInstance(double freq, double phase, double amplitude,
                                           double duration) {

        int numSamples = (int) (duration * SAMPLES_PER_SECOND);
        double timeStep = duration / numSamples;
        double instantaneousValue;
        double angularFreq = 2.0 * Math.PI * freq;

        double[] channel = new double[numSamples];

        for (int i = 0; i < numSamples; i++) {
            double time = i * timeStep;
            instantaneousValue = amplitude * 2 / (Math.PI) * Math.asin(Math.sin(Math.PI * (angularFreq * time + phase)));
            channel[i] = instantaneousValue;
        }
        return new TriangleWave(channel);
    }
}
