package jfws.util.math.interpolation;

public class LinearInterpolator implements Interpolator1d {

	public static final int START = 1;
	public static final int END = 2;

	@Override
	public double interpolate(double[] p, double x) {
		double diff = p[END] - p[START];
		return p[START] + x * diff;
	}
}
