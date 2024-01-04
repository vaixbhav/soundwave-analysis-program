package cpen221.soundwaves;

import cpen221.soundwaves.soundutils.FilterType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;

public class ConcreteSoundWave implements SoundWave {

    /**
     * Smallest allowable error difference between
     * two 'equal' floating point values.
     */
    private static final double SMALLEST_FLOATING_POINT_ERROR = 0.00001;

    /**
     * Step size in which beta is increased
     *  to find optimum beta for maximizing gamma.
     */
    private static final double BETA_STEP = 0.01;

    /**
     * Biggest value of beta to test.
     */
    private static final double BETA_MAX = 100.0;

    /**
     * Highpass filter value.
     */
    private static final int HIGHPASS = 3;

    /**
     * Bandpass filter value.
     */
    private static final int BANDPASS = 2;

    /**
     * Lowpass filter value.
     */
    private static final int LOWPASS = 1;

    /**
     * Left channel of audio samples at specified sampling rate.
     */
    private double[] leftChannel;

    /**
     * Right channel of audio samples at specified sampling rate.
     */
    private double[] rightChannel;

    // Representation Invariants:
    //  both leftChannel and rightChannel must have the same length
    //  all elements in leftChannel and rightChanel must represent valid audio samples from (-1 to 1)
    //  ConcreteSoundWave objects should be immutable after initialization.

    // Abstraction Functions:
    //  ConcreteSoundWave object represents sound wave with separate left and right channels
    //  - leftChannel: an array of doubles representing audio samples in left channel
    //  - rightChannel: an array of doubles representing audio samples in right channel

    /**
     * Create an instance of {@code SoundWave} with specified amplitude values for
     * the left and right channel (assuming stereo).
     *
     * @param leftChannel  left channel. leftChannel is not null.
     * @param rightChannel right channel. rightChannel is not null
     *                     <p>
     *                     Modifies this.
     */
    public ConcreteSoundWave(double[] leftChannel, double[] rightChannel) {
        this.leftChannel = leftChannel;
        this.rightChannel = rightChannel;
    }


    /**
     * Gets the left channel of this wave.
     *
     * @return left channel of this wave.
     */
    @Override
    public double[] getLeftChannel() {
        double[] leftChannelInstance = new double[this.leftChannel.length];
        System.arraycopy(this.leftChannel, 0, leftChannelInstance, 0, this.leftChannel.length);
        return leftChannelInstance;
    }


    /**
     * Gets the right channel of this wave.
     *
     * @return right channel of this wave.
     */
    @Override
    public double[] getRightChannel() {
        double[] rightChannelInstance = new double[this.rightChannel.length];
        System.arraycopy(this.rightChannel, 0, rightChannelInstance, 0, this.rightChannel.length);
        return rightChannelInstance;
    }


    /**
     * Gets the duration of the sound wave.
     *
     * @return duration of the sound wave.
     */
    @Override
    public double duration() {
        return (this.leftChannel.length * (1.0 / SAMPLES_PER_SECOND));
    }


    /**
     * Append the given channels sound wave to the end of this wave.
     *
     * @param lchannel the left channel of the sound wave to append.
     *                 lchannel is not null.
     * @param rchannel the right channel of the sound wave to append.
     *                 rchannel is not null.
     *                 <p>
     *                 Modifies this.
     */
    @Override
    public void append(double[] lchannel, double[] rchannel) {

        double[] leftChannelAfter = new double[lchannel.length + this.leftChannel.length];
        double[] rightChannelAfter = new double[rchannel.length + this.rightChannel.length];

        System.arraycopy(this.leftChannel, 0, leftChannelAfter, 0, this.leftChannel.length);
        System.arraycopy(this.rightChannel, 0, rightChannelAfter, 0, this.rightChannel.length);

        System.arraycopy(lchannel, 0, leftChannelAfter, this.leftChannel.length, lchannel.length);
        System.arraycopy(rchannel, 0, rightChannelAfter, this.rightChannel.length, rchannel.length);

        this.leftChannel = leftChannelAfter;
        this.rightChannel = rightChannelAfter;

    }


    /**
     * Appends the given sound wave to the end of this wave.
     *
     * @param other the sound wave to append to this wave.
     *              <p>
     *              Modifies this.
     */
    @Override
    public void append(SoundWave other) {

        double[] lchannel = other.getLeftChannel();
        double[] rchannel = other.getRightChannel();

        this.append(lchannel, rchannel);

    }


    /**
     * Creates a new wave by adding the given wave to this wave.
     *
     * @param other the wave to add to this wave.
     *              other is not null.
     * @return the new wave obtained by adding both waves.
     */
    @Override
    public SoundWave add(SoundWave other) {

        SoundWave otherWave;
        SoundWave thisWave;

        if (this.getLeftChannel().length > other.getLeftChannel().length) {
            otherWave = makeSameSize(other, this);
            thisWave = new ConcreteSoundWave(this.getLeftChannel(), this.getRightChannel());
        } else if (this.getLeftChannel().length < other.getLeftChannel().length) {
            thisWave = makeSameSize(this, other);
            otherWave = new ConcreteSoundWave(other.getLeftChannel(), other.getRightChannel());
        } else {
            thisWave = new ConcreteSoundWave(this.getLeftChannel(), this.getRightChannel());
            otherWave = new ConcreteSoundWave(other.getLeftChannel(), other.getRightChannel());
        }

        double[] newLChannel = new double[thisWave.getRightChannel().length];
        double[] newRChannel = new double[thisWave.getRightChannel().length];

        for (int t = 0; t < thisWave.getRightChannel().length; t++) {
            newLChannel[t] = thisWave.getLeftChannel()[t] + otherWave.getLeftChannel()[t];
            newRChannel[t] = thisWave.getRightChannel()[t] + otherWave.getRightChannel()[t];
        }

        SoundWave normalizedWave = normalize(new ConcreteSoundWave(newLChannel, newRChannel));
        return new ConcreteSoundWave(normalizedWave.getLeftChannel(), normalizedWave.getRightChannel());
    }


    /**
     * Create a new wave by adding an echo to this wave.
     *
     * @param delta > 0. delta, in seconds, is the time lag between this wave and
     *              the echo wave.
     * @param alpha > 0. alpha is the damping factor applied to the echo wave.
     * @return a new sound wave with an echo.
     */
    @Override
    public SoundWave addEcho(double delta, double alpha) {
        double[] echoLChannel = new double[(int) (this.leftChannel.length + delta)];
        double[] echoRChannel = new double[(int) (this.rightChannel.length + delta)];


        if (delta < 0) {
            for (int t = 0; t < echoRChannel.length; t++) {
                echoRChannel[t] = this.leftChannel[t - (int) delta] * alpha;
                echoLChannel[t] = this.rightChannel[t - (int) delta] * alpha;
            }
        } else {
            for (int t = 0; t < delta; t++) {
                echoRChannel[t] = 0;
                echoLChannel[t] = 0;
            }
            for (int t = 0; t < this.leftChannel.length; t++) {
                echoRChannel[t + (int) delta] = this.leftChannel[t] * alpha;
                echoLChannel[t + (int) delta] = this.rightChannel[t] * alpha;
            }
        }

        SoundWave echoWave = new ConcreteSoundWave(echoLChannel, echoRChannel);
        SoundWave wave = (new ConcreteSoundWave(this.getLeftChannel(), this.getRightChannel()));
        return wave.add(echoWave);
    }


    /**
     * Scale the amplitude of this wave by a scaling factor.
     * After scaling, the amplitude values are normalized to remain
     * between -1 and +1.
     *
     * @param scalingFactor is a value > 0.
     *                      <p>
     *                      Modifies this.
     */
    @Override
    public void scale(double scalingFactor) {
        double[] copyLeft = Arrays.copyOf(getLeftChannel(), this.leftChannel.length);
        double[] copyRight = Arrays.copyOf(getRightChannel(), this.rightChannel.length);

        for (int t = 0; t < copyLeft.length; t++) {
            copyLeft[t] *= scalingFactor;
            copyRight[t] *= scalingFactor;
        }

        SoundWave newWave = normalize(new ConcreteSoundWave(copyLeft, copyRight));

        this.leftChannel = newWave.getLeftChannel();
        this.rightChannel = newWave.getRightChannel();

    }


    /**
     * Determine if this wave fully contains the other sound wave as a pattern.
     *
     * @param other is the wave to search for in this wave.
     *              other is not null.
     * @return true if the other wave is contained in this after amplitude scaling,
     * and false if the other wave is not contained in this with any
     * possible amplitude scaling.
     */
    @Override
    public boolean contains(SoundWave other) {
        if (other.getLeftChannel().length > this.leftChannel.length) {
            return false;
        }

        if (other.getLeftChannel().length == 0) {
            return true;
        }

        double[] lchannel = other.getLeftChannel();
        double[] rchannel = other.getRightChannel();

        for (int startPoint = 0; startPoint <= (this.leftChannel.length - other.getLeftChannel().length); startPoint++) {

            boolean contains = false;
            double scaleFactor = Math.abs(this.rightChannel[startPoint] / rchannel[0]);

            for (int t = startPoint; t < startPoint + lchannel.length; t++) {
                contains = true;
                if (Math.abs(Math.abs(this.leftChannel[t] / lchannel[t - startPoint]) - scaleFactor) > SMALLEST_FLOATING_POINT_ERROR || Math.abs(this.rightChannel[t] / rchannel[t - startPoint] - scaleFactor) > SMALLEST_FLOATING_POINT_ERROR) {
                    contains = false;
                    break;
                }
            }
            if (contains) {
                return true;
            }
        }
        return false;
    }


    /**
     * Determine the similarity between this wave and another wave.
     * The similarity metric, gamma, is the sum of squares of
     * instantaneous differences.
     *
     * @param other the wave to be compared for similarity.
     *              other is not null.
     * @return the similarity between this wave and other.
     */
    @Override
    public double similarity(SoundWave other) {

        SoundWave otherWave;
        SoundWave thisWave;

        double bestGamma1 = 0.0;
        double bestGamma2 = 0.0;
        double gamma1;
        double gamma2;

        if (this.getLeftChannel().length > other.getLeftChannel().length) {
            otherWave = makeSameSize(other, this);
            thisWave = new ConcreteSoundWave(this.getLeftChannel(), this.getRightChannel());
        } else if (this.getLeftChannel().length < other.getLeftChannel().length) {
            thisWave = makeSameSize(this, other);
            otherWave = new ConcreteSoundWave(other.getLeftChannel(), other.getRightChannel());
        } else {
            thisWave = new ConcreteSoundWave(this.getLeftChannel(), this.getRightChannel());
            otherWave = new ConcreteSoundWave(other.getLeftChannel(), other.getRightChannel());
        }

        double[] betaArray = getBetaArray();

        for (double beta : betaArray) {
            gamma1 = this.gamma(thisWave.getLeftChannel(), thisWave.getRightChannel(), otherWave.getLeftChannel(), otherWave.getRightChannel(), beta);
            gamma2 = this.gamma(otherWave.getLeftChannel(), otherWave.getRightChannel(), thisWave.getLeftChannel(), thisWave.getRightChannel(), beta);

            if (gamma1 > bestGamma1) {
                bestGamma1 = gamma1;
            }

            if (gamma2 > bestGamma2) {
                bestGamma2 = gamma2;
            }
        }

        return (bestGamma2 + bestGamma1) / 2.0;
    }


    /**
     * Return the frequency of the component with the greatest amplitude
     * contribution to this wave. This component is the highest frequency obtained after
     * applying the Discrete Fourier Transform to this wave.
     *
     * @return the frequency of the wave component corresponding of highest amplitude.
     */
    @Override
    public double highestAmplitudeFrequencyComponent() {
        ComplexNumber[] leftFrequencyArray = this.fourierTransform(this.getLeftChannel());
        ComplexNumber[] rightFrequencyArray = this.fourierTransform(this.getRightChannel());

        ComplexNumber maxLeftFrequency = new ComplexNumber(0, 0);
        ComplexNumber maxRightFrequency = new ComplexNumber(0, 0);

        for (int k = 0; k < leftFrequencyArray.length; k++) {
            maxLeftFrequency = ComplexNumber.max(maxLeftFrequency, leftFrequencyArray[k]);
            maxRightFrequency = ComplexNumber.max(maxRightFrequency, rightFrequencyArray[k]);
        }

        ComplexNumber maxFrequency = ComplexNumber.max(maxRightFrequency, maxLeftFrequency);
        return maxFrequency.toReal();
    }


    /**
     * Filters this sound wave based on the filter type and the threshold frequencies specified.
     *
     * @param type        the type of filter through which the frequencies must be passed
     * @param frequencies the thresholds for filtering
     * @return a ConcreteSoundwave object with the filtered frequencies as channels
     */
    @Override
    public SoundWave filter(FilterType type, Double... frequencies) {

        if (frequencies.length > 2) {
            throw new IllegalArgumentException("Can not enter more than two filters.");
        } else if (frequencies.length == 0) {
            return new ConcreteSoundWave(this.getLeftChannel(), this.getRightChannel());
        } else {

            List<Double> thresholds = new ArrayList<>();
            Collections.addAll(thresholds, frequencies);
            Collections.sort(thresholds);

            double[] leftFrequencyArray = getFrequencyArray(fourierTransform(this.getLeftChannel()));
            double[] rightFrequencyArray = getFrequencyArray(fourierTransform(this.getRightChannel()));

            List<Double> leftChannelList = new ArrayList<>();
            List<Double> rightChannelList = new ArrayList<>();

            double[] rightChannelArray;
            double[] leftChannelArray;

            SoundWave filteredWave;

            switch (type) {
                case LOWPASS -> {
                    if (thresholds.size() == 1) {
                        addFrequencyToList(leftFrequencyArray, leftChannelList, thresholds.get(0), -1, LOWPASS);
                        addFrequencyToList(rightFrequencyArray, rightChannelList, thresholds.get(0), -1, LOWPASS);

                        rightChannelArray = new double[rightChannelList.size()];
                        leftChannelArray = new double[leftChannelList.size()];

                        copyListToArray(rightChannelList, rightChannelArray);
                        copyListToArray(leftChannelList, leftChannelArray);

                        filteredWave = new ConcreteSoundWave(leftChannelArray, rightChannelArray);

                    } else {
                        throw new IllegalArgumentException("Invalid frequencies for the given type");
                    }
                }
                case BANDPASS -> {
                    if (thresholds.size() > 1) {
                        addFrequencyToList(leftFrequencyArray, leftChannelList, thresholds.get(0), thresholds.get(1), BANDPASS);
                        addFrequencyToList(rightFrequencyArray, rightChannelList, thresholds.get(0), thresholds.get(1), BANDPASS);

                        rightChannelArray = new double[rightChannelList.size()];
                        leftChannelArray = new double[leftChannelList.size()];

                        copyListToArray(rightChannelList, rightChannelArray);
                        copyListToArray(leftChannelList, leftChannelArray);

                        filteredWave = new ConcreteSoundWave(leftChannelArray, rightChannelArray);
                    } else {
                        throw new IllegalArgumentException("Invalid frequencies for the given type");
                    }
                }
                case HIGHPASS -> {
                    if (thresholds.size() == 1) {
                        addFrequencyToList(leftFrequencyArray, leftChannelList, -1, thresholds.get(0), HIGHPASS);
                        addFrequencyToList(rightFrequencyArray, rightChannelList, -1, thresholds.get(0), HIGHPASS);

                        rightChannelArray = new double[rightChannelList.size()];
                        leftChannelArray = new double[leftChannelList.size()];

                        copyListToArray(rightChannelList, rightChannelArray);
                        copyListToArray(leftChannelList, leftChannelArray);

                        filteredWave = new ConcreteSoundWave(leftChannelArray, rightChannelArray);
                    } else {
                        throw new IllegalArgumentException("Invalid frequencies for the given type");
                    }
                }
                default -> throw new IllegalArgumentException("Invalid filter type");
            }
            return filteredWave;
        }
    }



    /*
     * Generate a fractal waveform from the given sound wave
     * (this is for fun!)
     * @param period the fractal periodicity, >= 1
     * @return a fractalized ConcreteSoundWave
    public ConcreteSoundWave fractalize(int period) {
        final long SCALE = (long) period * SAMPLES_PER_SECOND;
        double[] lchannel = this.getLeftChannel();
        double[] rchannel = this.getRightChannel();

        double[] newLChannel = Arrays.stream(lchannel).mapToLong(t -> (int)(SCALE * t))
                .map(t -> t & (t >>> 3) % SCALE)
                .mapToDouble(t -> (double) t / SCALE)
                .toArray();
        double[] newRChannel = Arrays.stream(rchannel).mapToLong(t -> (long)(SCALE * t))
                .map(t -> t & (t >>> 3) % SCALE)
                .mapToDouble(t -> (double) t / SCALE)
                .toArray();
        // One could also try:
        // t & (t >> 3) & (t >> 8) % SCALE;
        return new ConcreteSoundWave(newLChannel, newRChannel);
    }
    */


    //HELPER FUNCTIONS:

    /**
     * Takes samples as functions of time and maps it to the frequency domain.
     * (i.e. performs a Fourier Transform to obtain the frequencies
     * of the sine waves composing the compound sound wave)
     *
     * @param displacementArray the array corresponding to samples
     *                          on which the Fourier Transformation will be applied
     * @return array with fourier transformed frequencies
     */
    private ComplexNumber[] fourierTransform(double[] displacementArray) {
        double N = displacementArray.length;
        ArrayList<ComplexNumber> frequencyList = new ArrayList<>();
        ComplexNumber euler = new ComplexNumber(0, 0);
        for (int k = 0; k < N; k++) {
            frequencyList.add(k, new ComplexNumber(0, 0));
            for (int t = 0; t < N; t++) {
                frequencyList.set(k, frequencyList.get(k).add(euler.eulerRepresentation((-1 * 2 * Math.PI * k * t) / N).multiply(displacementArray[t])));
            }
        }
        ComplexNumber[] frequencyArray = new ComplexNumber[frequencyList.toArray().length];
        for (int k = 0; k < frequencyList.toArray().length; k++) {
            frequencyArray[k] = frequencyList.get(k);
        }
        return frequencyArray;
    }


    /**
     * Calculates gamma, the intermediate similarity metric for the two given sound waves.
     *
     * @param leftChannelOne  the left channel of the first wave.
     * @param rightChannelOne the left channel of the second wave.
     * @param leftChannelTwo  the right channel of the first wave.
     * @param rightChannelTwo the left channel of the second wave.
     * @param beta            the factor by which the values of channels
     *                        of the second wave are scaled.
     *                        beta must be chosen to minimize gamma.
     * @return gamma, the intermediate similarity metric between two waves.
     */
    private double gamma(double[] leftChannelOne, double[] rightChannelOne, double[] leftChannelTwo, double[] rightChannelTwo, double beta) {
        return 1.0 / (1 + this.summator(rightChannelOne, rightChannelTwo, beta) + this.summator(leftChannelOne, leftChannelTwo, beta));
    }


    /**
     * Calculates the summation of square of the difference between
     * corresponding values first and second channels, where the
     * second channel values are scaled by beta.
     *
     * @param channelOne the first channel of samples.
     * @param channelTwo the second channel of samples.
     * @param beta       the factor by which the values of channels
     *                   of the second wave are scaled.
     *                   beta must be chosen to minimize gamma.
     * @return the summation of the squares of differences between the
     * first channel values and second channel values scaled by beta.
     */
    private double summator(double[] channelOne, double[] channelTwo, double beta) {

        double summation = 0.0;

        for (int t = 0; t < channelOne.length; t++) {
            summation += Math.pow((channelOne[t] - beta * channelTwo[t]), 2);
        }

        return summation;
    }


    /**
     * Creates a new wave with the same sample values as the smaller wave
     * and zero valued samples at the end such that its duration
     * is the same as the duration of the bigger wave.
     *
     * @param smallWave the wave with the smaller duration.
     * @param bigWave   the wave with the bigger duration.
     * @return a new wave with the same sample values as the smaller wave
     * and the same duration as the bigger wave.
     */
    private static SoundWave makeSameSize(SoundWave smallWave, SoundWave bigWave) {

        SoundWave newSoundWave = new ConcreteSoundWave(smallWave.getLeftChannel(), smallWave.getRightChannel());

        int diff = bigWave.getLeftChannel().length - smallWave.getLeftChannel().length;
        newSoundWave.append(new double[diff], new double[diff]);

        return new ConcreteSoundWave(newSoundWave.getLeftChannel(), newSoundWave.getRightChannel());
    }


    /**
     * Normalizes both channels of the given wave.
     *
     * @param wave the wave to be normalized.
     * @return the normalized wave.
     */
    private static SoundWave normalize(SoundWave wave) {

        double[] lchannel = wave.getLeftChannel();
        double[] rchannel = wave.getRightChannel();

        double maxLeft = Arrays.stream(lchannel).max().orElse(0.0);
        double maxRight = Arrays.stream(rchannel).max().orElse(0.0);

        double minLeft = Arrays.stream(lchannel).min().orElse(0.0);
        double minRight = Arrays.stream(rchannel).min().orElse(0.0);

        double left = Math.abs(Math.max(Math.abs(maxLeft), Math.abs(minLeft)));
        double right = Math.abs(Math.max(Math.abs(maxRight), Math.abs(minRight)));

        SoundWave leftWave = new ConcreteSoundWave(lchannel, new double[rchannel.length]);
        SoundWave rightWave = new ConcreteSoundWave(new double[lchannel.length], rchannel);

        if (left > 1.0) {
            leftWave.scale((1.0 / left));
        }
        if (right > 1.0) {
            rightWave.scale((1.0 / right));
        }

        return new ConcreteSoundWave(leftWave.getLeftChannel(), rightWave.getRightChannel());
    }


    /**
     * Converts the complex number frequency array to a real number array.
     *
     * @param fourierTransformedArray the frequency array with Complex Number frequencies
     *                                (cannot be null)
     * @return frequencies corresponding to the real values of the complex number frequencies.
     */
    private static double[] getFrequencyArray(ComplexNumber[] fourierTransformedArray) {

        double[] frequencies = new double[fourierTransformedArray.length];
        for (int i = 0; i < fourierTransformedArray.length; i++) {
            frequencies[i] = fourierTransformedArray[i].toReal();
        }
        return frequencies;
    }


    /**
     * Copies all the values from a list to an array.
     *
     * @param sourceList the list from which the values must be copied.
     * @param destArray  the array to which the values must be copied.
     *                   <p>
     *                   Modifies dest.
     */
    private static void copyListToArray(List<Double> sourceList, double[] destArray) {
        int index = 0;
        for (Double frequency : sourceList) {
            destArray[index] = frequency;
            index++;
        }
    }

    /**
     * Adds the frequencies that satisfy the filer requirements to a given list.
     *
     * @param sourceFrequencyArray the array from which the values must be copied.
     * @param destFrequencyList    the list to which the values must be copied.
     * @param lowpassThreshold     the threshold frequency for the LOWPASS filter.
     * @param highpassThreshold    the threshold frequency for the HIGHPASS filter.
     * @param type                 the type of filter (1 --> LOWPASS, 2 --> BANDPASS, 3 --> HIGHPASS).
     *                             <p>
     *                             Modifies src.
     */
    private static void addFrequencyToList(double[] sourceFrequencyArray, List<Double> destFrequencyList, double lowpassThreshold, double highpassThreshold, int type) {
        int index = 0;
        for (double frequency : sourceFrequencyArray) {
            if (type == 1) {
                if ((frequency < lowpassThreshold)) {
                    destFrequencyList.add(index, frequency);
                    index++;
                }
            } else if (type == 2) {
                if ((frequency > lowpassThreshold) && (frequency < highpassThreshold)) {
                    destFrequencyList.add(index, frequency);
                    index++;
                }
            } else {
                if ((frequency > highpassThreshold)) {
                    destFrequencyList.add(index, frequency);
                    index++;
                }
            }
        }
    }


    /**
     * Returns an array of beta values such that beta > 0 and <= 100.
     *
     * @return an array of valid beta values.
     */
    public static double[] getBetaArray() {
        double beta = BETA_STEP;
        double[] betaArray = new double[(int) (BETA_MAX / BETA_STEP)];
        for (int index = 0; index < (int) (BETA_MAX / BETA_STEP); index++) {
            betaArray[index] = beta;
            beta += BETA_STEP;
        }
        return betaArray;
    }
}
