package cpen221.soundwaves.soundutils;

import ca.ubc.ece.cpen221.mp1.utils.MP3Player;

/**
 * <p><strong>Overview.</strong>
 * This utility type allows one to work with simple audio files
 * in the following formats (WAV, MIDI, MP3) with the appropriate
 * filename extensions.
 * </p>
 *
 * <p>
 * The main methods are:
 * <ul>
 *     <li>{@link #isEmpty()}, and</li>
 *     <li>{@link #readLeftNext()}</li>.
 *     <li>{@link #readRightNext()}</li>.
 * </ul>
 * </p>
 * @author Sathish Gopalakrishnan
 */
public class AudioFile {
    private final String fileType;
    private final String fileName;
    private MP3Player mp3;
    private boolean isEmpty;

    /**
     * Create a new AudioFile instance for a file with the provided filename.
     * The existence of the file is often <strong>not verified</strong> until a
     * {@link #readLeftNext()} or {@link #readRightNext()} invocation.
     *
     * @param fileName is not null.
     */
    public AudioFile(String fileName) {
        this.fileType = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        this.fileName = fileName;
        isEmpty = false;

        switch (fileType) {
            case "wav":
            case "midi":
                break;
            case "mp3":
                MP3Player.open(fileName);
                break;
            default:
                throw new IllegalArgumentException("Invalid file type");
        }
    }

    /**
     * Verify if the file is empty or not.
     *
     * @return true if the file is empty or if we have reached the end-of-file
     * and false otherwise.
     */
    public boolean isEmpty() {
        return isEmpty;
    }

    /**
     * Obtain the next set of left channel audio samples. If the file does not exist then one may
     * see an <code>RuntimeException</code>.
     *
     * @return the next set of audio samples as long as {@link #isEmpty()} is false.
     */
    public double[] readLeftNext() {
        switch (fileType) {
            case "wav":
            case "midi":
                if (isEmpty()) {
                    throw new RuntimeException("End of file reached");
                }
                return Audio.read(fileName);
            case "mp3":
                if (MP3Player.isEmpty()) {
                    throw new RuntimeException("End of file reached");
                }
                double[] lchannel = MP3Player.getLeftChannel();
                if (MP3Player.isEmpty()) {
                    isEmpty = true;
                    MP3Player.close();
                }
                return lchannel;
            default:
                return null;
        }
    }

    /**
     * Obtain the next set of right channel audio samples. If the file does not exist then one may
     * see an <code>RuntimeException</code>.
     *
     * @return the next set of audio samples as long as {@link #isEmpty()} is false.
     */
    public double[] readRightNext() {
        switch (fileType) {
            case "wav":
            case "midi":
                if (isEmpty()) {
                    throw new RuntimeException("End of file reached");
                }
                return Audio.read(fileName);
            case "mp3":
                if (MP3Player.isEmpty()) {
                    throw new RuntimeException("End of file reached");
                }
                double[] lchannel = MP3Player.getRightChannel();
                if (MP3Player.isEmpty()) {
                    isEmpty = true;
                    MP3Player.close();
                }
                return lchannel;
            default:
                return null;
        }
    }
}
