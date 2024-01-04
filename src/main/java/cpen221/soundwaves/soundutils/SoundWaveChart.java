package cpen221.soundwaves.soundutils;

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

import java.util.stream.IntStream;

/**
 * <p><strong>Overview.</strong>
 * This is a utility type for some simple drawing operations.
 * </p>
 *
 * <p><strong>Drawing Charts.</strong>
 * One can draw simple XY charts using the following methods:
 * <ul>
 *     <li>{@link #drawWave(double[], double[])}, which takes the values for the x- and y-axes;</li>
 *     <li>{@link #drawWave(double[])}, which takes as argument the values for the y-axis and uses integer values from 0 to the length of the argument - 1 for the x-axis.</li>
 * </ul>
 * </p>
 *
 * <p><strong>Updating a chart.</strong>
 * One can update a chart using the {@link #updateDrawing(double[])} method,
 * which takes a new set of values for the y-axis and shifts the values
 * along the x-axis by the length of the previous number of points that were
 * drawn.
 * </p>
 *
 * @author Sathish Gopalakrishnan
 */
public class SoundWaveChart {

    SwingWrapper<XYChart> swing;
    private XYChart chart;
    private int offset = 0;
    private boolean initialized = false;

    public void drawWave(double[] time, double[] amplitudes) {
        chart = QuickChart.getChart("Sound Wave", "X", "Y", "Sound Wave", time, amplitudes);
        swing = new SwingWrapper<XYChart>(chart);
        if (!initialized) {
            initialized = true;
        }
        swing.displayChart();
    }

    public void drawWave(double[] amplitudes) {
        double[] time = getXTime(amplitudes);
        offset += amplitudes.length;
        this.drawWave(time, amplitudes);
    }

    private double[] getXTime(double[] amplitudes) {
        int oldOffset = offset;
        offset += amplitudes.length;
        return IntStream.range(oldOffset, oldOffset + amplitudes.length)
            .mapToDouble(t -> (double) t).toArray();
    }

    public void updateDrawing(double[] amplitudes) {
        if (!initialized) {
            this.drawWave(amplitudes);
        } else {
            double[] time = getXTime(amplitudes);
            chart.updateXYSeries("Sound Wave", time, amplitudes, null);
            swing.repaintChart();
        }
    }
}
