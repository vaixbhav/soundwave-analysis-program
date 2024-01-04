package cpen221.soundwaves;

import cpen221.soundwaves.soundutils.AudioFile;

import java.util.ArrayList;

public class MP3Wave extends ConcreteSoundWave {

    /**
     * A private constructor.
     * @param lchannel the time series of left amplitude values, is not null
     * @param rchannel the time series of right amplitude values, is not null
     */
    private MP3Wave(double[] lchannel, double[] rchannel) {
        super(lchannel, rchannel);
    }

    /**
     * Obtain a new MP3Wave instance.
     *
     * @param fileName name of the audio file
     *                 from which to extract the sound wave.
     *                 Is a valid audio file name.
     * @return an MP3 wave instance from the specified audio file.
     */
    public static MP3Wave getInstance(String fileName) {
        AudioFile af1 = new AudioFile(fileName);

        ArrayList<Double> leftSampleList = new ArrayList<>();
        ArrayList<Double> rightSampleList = new ArrayList<>();

        while (!af1.isEmpty()) {
            double[] leftSamples = af1.readLeftNext();
            double[] rightSamples = af1.readRightNext();
            for (double sample : leftSamples) {
                leftSampleList.add(sample);
            }
            for (double sample : rightSamples) {
                rightSampleList.add(sample);
            }
        }
        double[] rchannel = rightSampleList.stream().mapToDouble(Double::doubleValue).toArray();
        double[] lchannel = leftSampleList.stream().mapToDouble(Double::doubleValue).toArray();
        return new MP3Wave(lchannel, rchannel);
    }
}
